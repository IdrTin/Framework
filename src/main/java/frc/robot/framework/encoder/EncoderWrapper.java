package frc.robot.framework.encoder;

import org.w3c.dom.*;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

public class EncoderWrapper implements EncoderBase {
    private EncoderBase encoder;

    public EncoderWrapper(Element element) {

        String encoderType = element.getAttribute("vendor");
        try {
            int portOne = Integer.parseInt(element.getAttribute("port_one"));
            int portTwo = 0;
            if (!element.getAttribute("port_two").isEmpty()) {
                portTwo = Integer.parseInt(element.getAttribute("port_two"));
            }
            encoder = getEncoderType(encoderType, portOne, portTwo);

        } catch (Exception e) {
            throw new NumberFormatException(
                    "Invalid Format on EncoderWrapper vendor:" + encoderType + " portOne: "
                            + element.getAttribute("port_one") +
                            " portTwo: " + element.getAttribute("port_two"));
        }
        try {
            if (element.hasAttribute("setPosition")) {
                encoder.setPosition(Double.parseDouble(element.getAttribute("setPosition")));
            }
        } catch (Exception e) {
            throw new NumberFormatException("Invalid Format on setPosition on EcnoderWrapper vendor: " + encoderType
                    + " setPosition:" + element.getAttribute("setPosition"));
        }
        parseXMLHelper(element);
    }

    private void parseXMLHelper(Element element) {
        if (element.hasAttribute("inverted") && Boolean.parseBoolean(element.getAttribute("inverted"))) {
            encoder.setInverted(true);
        }
        try {
            if (!element.getAttribute("distance_per_pulse").isEmpty()) {
                encoder.setDistancePerPulse(Double.parseDouble(element.getAttribute("distance_per_pulse")));
            }
        } catch (Exception e) {
            throw new NumberFormatException(
                    "Invalid Format on distance_per_pulse" + element.getAttribute("distance_per_pulse"));
        }
    }

    public EncoderWrapper(Element element, EncoderBase encoderBase) {
        encoder = encoderBase;
        if (!element.getAttribute("distance_per_pulse").isEmpty()) {
            encoder.setDistancePerPulse(Double.parseDouble(element.getAttribute("distance_per_pulse")));
        }
        parseXMLHelper(element);
    }

    private EncoderBase getEncoderType(String encoderType, int portOne, int portTwo) {
        if (encoderType.toLowerCase().equals("toughbox") || encoderType.toLowerCase().equals("775")) {
            return new DIOEncoder(portOne, portTwo, false, EncodingType.k4X);
        } else if (encoderType.toLowerCase().equals("sparkmax") || encoderType.toLowerCase().equals("talonsrx")) {
            System.out.println(
                    "CAN Encoder found, initalize by passing EncoderBase into the consturctor: " + encoderType);
            return null;
        } else if (encoderType.toLowerCase().equals("dutycycle")) {
            return new DutyCycleEncoder(portOne);

        } else {
            System.out.println("Unknown vendor encoder type: " + encoderType);
            return null;
        }
    }

    public int getTicks() {
        return encoder.getTicks();
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }

    public double getPosition() {
        return encoder.getPosition();
    }

    public double getAbsolutePosition() {
        return encoder.getAbsolutePosition();
    }

    public void resetEncoder() {
        encoder.resetEncoder();
    }

    @Override
    public void setDistancePerPulse(double factor) {
        encoder.setDistancePerPulse(factor);
    }

    @Override
    public void setInverted(boolean inverted) {
        encoder.setInverted(inverted);

    }

    @Override
    public void setPosition(double position) {
        encoder.setPosition(position);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        // builder.setSmartDashboardType("Motor Controller");
        // builder.setActuator(true);
        // builder.setSafeState(this::disable);
        builder.addDoubleProperty("Pos", this::getPosition, this::setPosition);
        builder.addDoubleProperty("AbsPos", this::getAbsolutePosition, null);
        builder.addDoubleProperty("Vel", this::getVelocity, null);
    }
}