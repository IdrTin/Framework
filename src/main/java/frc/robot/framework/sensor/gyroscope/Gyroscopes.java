package frc.robot.framework.sensor.gyroscope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import frc.robot.framework.util.ShuffleboardFramework;
import frc.robot.framework.util.ShuffleboardFramework.ShuffleboardBase;

public class Gyroscopes {
    private Map<String, GyroBase> gyroscopes = new HashMap<>();
    private String subsystemName;
    public Element sensorElement;
    private ShuffleboardBase tab;

    public Gyroscopes(String subsystemName) {
        this.subsystemName = subsystemName;
        tab = ShuffleboardFramework.getSubsystem(subsystemName);
    }

    public void put(String id, GyroBase gyro) {
        gyroscopes.put(id, gyro);
    }

    public Set<String> GetAllGyroscopeIDs() {
        return gyroscopes.keySet();
    }

    private GyroBase getGyroscope(String id) {
        GyroBase requestedsensor = gyroscopes.get(id);
        if (requestedsensor == null) {
            sensorError(id, subsystemName);
            return null;
        }
        return requestedsensor;
    }

    private void sensorError(String id, String subsystemName) {
        System.out.println("Gyro:" + id + " not found in Subsystem: " + subsystemName);
    }

    public double getGYROAccel(String id, String axis) {
        GyroBase requestedGYRO = getGyroscope(id);
        return tab.getEnabled(id) ? requestedGYRO.getGyroAccel(axis) : 0.0;
    }

    public double getGYROAngle(String id, String axis) {
        GyroBase requestedGYRO = getGyroscope(id);
        return tab.getEnabled(id) ? requestedGYRO.getGyroAngle(axis) : 0.0;
    }

    public double getGYRORate(String id) {
        GyroBase requestedGYRO = getGyroscope(id);
        return tab.getEnabled(id) ? requestedGYRO.getGyroRate() : 0.0;
    }

    public double getGYRORate(String id, String axis) {
        GyroBase requestedGYRO = getGyroscope(id);
        return tab.getEnabled(id) ? requestedGYRO.getGyroRate(axis) : 0.0;
    }

    public double getGYROMagneticField(String id, String axis) {
        GyroBase requestedGYRO = getGyroscope(id);
        return tab.getEnabled(id) ? requestedGYRO.getMagneticField(axis) : 0.0;
    }

}
