package frc.robot.framework.robot;

import com.revrobotics.AnalogInput;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import frc.robot.framework.algorithm.PIDWrapper;
import frc.robot.framework.algorithm.Pids;
import frc.robot.framework.encoder.EncoderWrapper;
import frc.robot.framework.encoder.Encoders;
import frc.robot.framework.motor.MotorBase;
import frc.robot.framework.motor.MotorWrapper;
import frc.robot.framework.motor.MotorGroup;
import frc.robot.framework.motor.Motors;
import frc.robot.framework.sensor.accelerometer.ACLWrapper;
import frc.robot.framework.sensor.accelerometer.Accelerometers;
import frc.robot.framework.sensor.analoginput.AnalogInputs;
import frc.robot.framework.sensor.analoginput.AnaloginWrapper;
import frc.robot.framework.sensor.digitalinput.DigitalInWrapper;
import frc.robot.framework.sensor.digitalinput.DigitalInputs;
import frc.robot.framework.sensor.gyroscope.GyroWrapper;
import frc.robot.framework.sensor.gyroscope.Gyroscopes;
import frc.robot.framework.sensor.potentiometer.PotentiometerWrapper;
import frc.robot.framework.sensor.potentiometer.Potentiometers;
import frc.robot.framework.sensor.ultrasonic.UltrasonicWrapper;
import frc.robot.framework.sensor.ultrasonic.Ultrasonics;
import frc.robot.framework.servo.ServoWrapper;
import frc.robot.framework.servo.Servos;
import frc.robot.framework.solenoid.SolenoidWrapper;
import frc.robot.framework.solenoid.Solenoids;
import frc.robot.framework.util.ShuffleboardFramework;
import frc.robot.framework.util.ShuffleboardFramework.ShuffleboardBase;

/**
 * [Out] is a class containing static methods for controlling all outputs from
 * the robot. This includes, but is not limited to, motors and solenoids.
 */

public class SubsystemCollection implements RobotXML {
    public final String subsystemName;
    public Motors motors;
    public Servos servos;
    public Solenoids solenoids;
    public Accelerometers accelerometers;
    public Gyroscopes gyroscopes;
    public DigitalInputs digitalInputs;
    public AnalogInputs analogInputs;
    public Potentiometers potentiometers;
    public Encoders encoders;
    public Ultrasonics ultrasonics;
    public Pids pids;
    private Element systemElement;
    private ShuffleboardBase tab;

    public SubsystemCollection(Element element) {
        subsystemName = element.getAttribute("id");
        ReadXML(element);
    }

    public SubsystemCollection(Element element, String subsystemName) {
        this.subsystemName = subsystemName;
        ReadXML(element);
    }

    public String getAttribute(String attribute) {
        return systemElement.getAttribute(attribute);
    }

    public void ReadXML(Element system) {
        systemElement = system;
        motors = new Motors(subsystemName);
        servos = new Servos(subsystemName);
        solenoids = new Solenoids(subsystemName);
        accelerometers = new Accelerometers(subsystemName);
        gyroscopes = new Gyroscopes(subsystemName);
        potentiometers = new Potentiometers(subsystemName);
        encoders = new Encoders(subsystemName);
        ultrasonics = new Ultrasonics(subsystemName);
        analogInputs = new AnalogInputs(subsystemName);
        pids = new Pids(subsystemName);
        tab =ShuffleboardFramework.getSubsystem(subsystemName);

        NodeList children = system.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentChild = children.item(i);
            if (currentChild.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element childElement = (Element) currentChild;
            String id = childElement.getAttribute("id");
            if (childElement.getTagName().equals("motor")) {
                var motorTemp = new MotorWrapper(childElement, false, this);
                motors.put(id, motorTemp);
                tab.addSendableToTab(id, motorTemp, BuiltInWidgets.kMotorController);
            } else if (childElement.getTagName().toLowerCase().equals("motorgroup")) {
                var motorTemp = new MotorWrapper(childElement, true, this);
                motors.put(id, motorTemp);
                tab.addSendableToTab(id, motorTemp, BuiltInWidgets.kMotorController);
                for (MotorBase eachMotor : ((MotorGroup)motorTemp.getMotor()).getAllMotors()) {
                    tab.addSendableToTab(eachMotor.toString(), eachMotor, BuiltInWidgets.kMotorController);
                }
            } else if (childElement.getTagName().toLowerCase().equals("servo")) {
                servos.put(id, new ServoWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("encoder")) {
                var encoderTemp = new EncoderWrapper(childElement);
                encoders.put(id, encoderTemp);
                tab.addSendableToTab(id, encoderTemp);
            } else if (childElement.getTagName().toLowerCase().equals("solenoid")) {
                solenoids.put(id, new SolenoidWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("acl")
                    || childElement.getTagName().toLowerCase().equals("accelerometer")) {
                accelerometers.put(id, new ACLWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("analoginput")) {
                analogInputs.put(id, new AnaloginWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("dio") || childElement.getTagName().equals("limitswitch")) {
                digitalInputs.put(id, new DigitalInWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("gyro") || childElement.getTagName().equals("gyroscopes")) {
                var gyro = new GyroWrapper(childElement);
                gyroscopes.put(id, gyro);
                tab.addSendableToTab(id, gyro);
            } else if (childElement.getTagName().toLowerCase().equals("pid")){
                pids.put(id, new PIDWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("pot")
                    || childElement.getTagName().equals("potientiometers")) {
                potentiometers.put(id, new PotentiometerWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("ut") || childElement.getTagName().equals("ultrasonic")) {
                ultrasonics.put(id, new UltrasonicWrapper(childElement));
            } else if (childElement.getTagName().toLowerCase().equals("module")){
                //skip
            } 
            else {
                System.out.println("Unknown XML element: " + childElement.getTagName().toLowerCase() + " on subsystem: "
                        + system.getTagName());
            }
        }
    }

    @Override
    public void ReloadConfig() {
        

    }

}