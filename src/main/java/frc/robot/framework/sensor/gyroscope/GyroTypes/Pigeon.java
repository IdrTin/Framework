package frc.robot.framework.sensor.gyroscope.GyroTypes;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.framework.sensor.gyroscope.GyroBase;

public class Pigeon implements GyroBase{
    WPI_PigeonIMU m_gyro;
    public Pigeon(int port){
        m_gyro = new WPI_PigeonIMU(port);
    }
    public void test(){
        
    }
    public double getGyroRate(){
        return m_gyro.getRate();
    }
    public double getGyroAngle(){
        return m_gyro.getAngle();
    }
    @Override
    public double getGyroAccel(String axis) {
        
        return 0;
    }
    @Override
    public double getGyroAngle(String axis) {
        
        return 0;
    }
    @Override
    public double getGyroRate(String axis) {
        
        return 0;
    }
    @Override
    public double getMagneticField(String axis) {
        
        return 0;
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        // builder.setSmartDashboardType("Motor Controller");
        // builder.setActuator(false);
        builder.addDoubleProperty("GyroAngle", this::getGyroAngle, null);
        // builder.addDoubleProperty("Y", this::get, this::set);
        // builder.addDoubleProperty("X", this::get, this::set);
    }
}
