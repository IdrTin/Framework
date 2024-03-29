package frc.robot.framework.subsystems.Motor;

import org.w3c.dom.Element;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.framework.algorithm.PIDBase;
import frc.robot.framework.algorithm.PIDWrapper;
import frc.robot.framework.algorithm.SoftwarePID;
import frc.robot.framework.encoder.EncoderBase;
import frc.robot.framework.encoder.EncoderWrapper;
import frc.robot.framework.motor.MotorBase;
import frc.robot.framework.robot.RobotXML;
import frc.robot.framework.robot.SubsystemCollection;
import frc.robot.framework.util.CommandMode;
import frc.robot.framework.util.ShuffleboardFramework;
import frc.robot.framework.util.ShuffleboardFramework.ShuffleboardBase;

public class Motor extends SubsystemBase implements RobotXML {
    private ShuffleboardBase tab;

    private SubsystemCollection subsystemColection;
    private double reference = .00;
    private CommandMode mode = CommandMode.PERCENTAGE;
    private double maxVelocity, minVelocity, maxposition, minposition, maxPower, minPower = 0;
    private Element element;
    private MotorBase motor;
    private boolean hasEncoder, hasPID = false;
    private String systemName="";

    public Motor(Element subsystem) {
        element = subsystem;
        tab = ShuffleboardFramework.getSubsystem(subsystem.getAttribute("id"));
        systemName = subsystem.getAttribute("id");
        ReadXML(subsystem);

        for (String motorId : subsystemColection.motors.GetAllMotorIDs()) {
            motor = subsystemColection.motors.getMotor(motorId);
            hasEncoder = motor.hasEncoder();
            hasPID = motor.hasPID();
            break;
        }
    }

    public CommandMode getMode() {
        return mode;
    }

    public void setReference(double reference, CommandMode mode) {
        this.reference = reference;
        this.mode = mode;
    }

    public void setReference(double reference) {
        this.reference = reference;
    }

    public double getReference() {
        return reference;
    }

    public MotorBase getMotor() {
        return motor;
    }

    @Override
    public void periodic() {
        // This gets the first motor. This is a generic subsystem so we don't control
        // more than one motor. If you need more than one, use a motor group
        if (hasEncoder) {
            // Makes sure we haven't gone past max/min positions. If we have, go to middle
            // position of those two.
            if ((motor.getPosition() > maxposition && element.hasAttribute("maxPosition"))
                    || (motor.getPosition() < minposition && element.hasAttribute("minPosition"))) {
                motor.setReference((maxposition + minposition) / 2, CommandMode.POSITION);
                System.out.println(systemName+" exceeding positional limits. Resetting");
                return;
            }

            // Clamp the values for velocity.
            reference = reference > maxVelocity ? maxVelocity : reference < minVelocity ? minVelocity : reference;
        }
        // Clamp the values for power.
        if (mode == CommandMode.PERCENTAGE) {
            reference = reference > maxPower ? maxPower : reference < minPower ? minPower : reference;
        }

        double output = 0;
        switch (mode) {
            case PERCENTAGE:
                output = reference;
                break;
            case POSITION:
                if (hasEncoder && hasPID)
                    output = motor.getPID().getPowerOutput(motor.getPosition(), reference, mode);
                else {
                    throw new UnsupportedOperationException(systemName+ "motor subsystem:"+element.getAttribute("id")+" Position requested but no pid/encoder");
                }
                break;
            case VELOCITY:
                if (hasEncoder && hasPID)
                    output = motor.getPID().getPowerOutput(motor.getPosition(), reference, mode);
                else {
                    throw new UnsupportedOperationException(systemName+ " motor subsystem:"+element.getAttribute("id")+" Position requested but no pid/encoder");
                }
                break;
        }
        if (mode != CommandMode.PERCENTAGE && (motor.getPID() instanceof PIDWrapper) && ((PIDWrapper)motor.getPID()).pidController instanceof SoftwarePID)
            motor.setReference(output, CommandMode.PERCENTAGE);
        else
            motor.setReference(output, mode);
        // System.out.println(output+","+mode);
    }

    @Override
    public void simulationPeriodic() {
    }

    @Override
    public void ReadXML(Element element) {
        // XMLUtil.prettyPrint(element);
        subsystemColection = new SubsystemCollection(element);
        if (element.hasAttribute("commandMode")) {
            switch (element.getAttribute("commandMode")) {
                case "power":
                    mode = CommandMode.PERCENTAGE;
                    break;
                case "velocity":
                    mode = CommandMode.VELOCITY;
                    break;
                case "position":
                    mode = CommandMode.POSITION;
                default:
                    throw new UnsupportedOperationException(systemName +"motor subsystem does not support commandMode:" + element.getAttribute("commandMode"));  
            }
            if (element.hasAttribute("reference")) {
                reference = Double.parseDouble(element.getAttribute("reference"));
            }
        } else {
            mode = CommandMode.PERCENTAGE;
        }
        if (element.hasAttribute("maxVelocity")) {
            maxVelocity = Double.parseDouble(element.getAttribute("maxVelocity"));
        } else {
            maxVelocity = Double.POSITIVE_INFINITY;
        }
        if (element.hasAttribute("minVelocity")) {
            minVelocity = Double.parseDouble(element.getAttribute("minVelocity"));
        } else {
            minVelocity = Double.NEGATIVE_INFINITY;
        }
        if (element.hasAttribute("maxPosition")) {
            maxposition = Double.parseDouble(element.getAttribute("maxPosition"));
        } else {
            maxposition = Double.POSITIVE_INFINITY;
        }
        if (element.hasAttribute("minPosition")) {
            minposition = Double.parseDouble(element.getAttribute("minPosition"));
        } else {
            minposition = Double.NEGATIVE_INFINITY;
        }
        if (element.hasAttribute("maxPower")) {
            maxPower = Double.parseDouble(element.getAttribute("maxPower"));
        } else {
            maxPower = 1.0;
        }
        if (element.hasAttribute("minPower")) {
            minPower = Double.parseDouble(element.getAttribute("minPower"));
        } else {
            minPower = -1.0;
        }
    }

    @Override
    public void ReloadConfig() {

    }
}