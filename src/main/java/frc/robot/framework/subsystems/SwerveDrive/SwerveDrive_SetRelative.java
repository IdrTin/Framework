package frc.robot.framework.subsystems.SwerveDrive;

import org.w3c.dom.Element;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.framework.controller.ControllerBase;
import frc.robot.framework.robot.*;

public class SwerveDrive_SetRelative extends CommandBase implements RobotXML {

  private Element myElement;
  private SubsystemCollection subsystemColection;
  private SwerveDrive swerveDriveSubsystem;

  public SwerveDrive_SetRelative(Element element) {
    ReadXML(element);

    SubsystemBase temp = RobotInit.GetSubsystem(element.getAttribute("subsystemID"));
    if (temp == null || !(temp instanceof SwerveDrive)) {
      throw new UnsupportedOperationException("SwerveDrive_SetRelative could not find swerve subsystem(subsystemID):" + element.getAttribute("subSystemID"));
    }
    swerveDriveSubsystem = (SwerveDrive) temp;
    this.addRequirements(swerveDriveSubsystem);

  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    try {
      if (swerveDriveSubsystem.getTeleFieldRelative()) {
        swerveDriveSubsystem.setTeleFieldRelative(false);
        System.out.println("Field Relative: false");
      } else {
        swerveDriveSubsystem.setTeleFieldRelative(true);
        System.out.println("Field Relative: true");
      }
    } catch (Exception e) {
      throw new NumberFormatException("SwerveDrive_Relative: Error in execute");
    }
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void ReadXML(Element element) {
  }

  @Override
  public void ReloadConfig() {

  }
}