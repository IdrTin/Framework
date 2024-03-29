package frc.robot.framework.sensor.gyroscope.GyroTypes;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.framework.sensor.gyroscope.GyroBase;

public class ADXRS_450 implements GyroBase{
    ADXRS450_Gyro m_gyro;
    public ADXRS_450(){
        m_gyro = new ADXRS450_Gyro();
        
    }
    public ADXRS_450(String port){
        SPI.Port m_port = SPI.Port.valueOf(port);
        m_gyro = new ADXRS450_Gyro(m_port);
    }
    public double getGyroRate(){
        return m_gyro.getRate();
    }
    public double getGyroAngle(){
        return m_gyro.getAngle();
    }
    public double getGyroAccel(String axis) {
        return 0;
    }
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
