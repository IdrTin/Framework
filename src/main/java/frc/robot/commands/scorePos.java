package frc.robot.commands;

import java.util.Map;
import org.w3c.dom.Element;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.framework.robot.*;
import frc.robot.framework.subsystems.Motor.Motor;
import frc.robot.framework.util.CommandMode;
import frc.robot.framework.util.readTextFile;

public class scorePos extends CommandBase implements RobotXML {
  private String text = "noo";
  private Element myElement;
  private String command;
  private Motor arm;
  private Motor arm_chain;
  private Motor wrist;
  private Motor claw;
  private boolean isFinished = true;
  private final Timer m_timer = new Timer();

  // private Motor rotation;
  private Map<String, Double> values;

  public scorePos(Element element) {
    myElement = element;
    values = readTextFile.HashMapFromTextFile();
    SubsystemBase temp_arm = RobotInit.GetSubsystem(element.getAttribute("armSubsystemID"));
    SubsystemBase temp_arm_chain = RobotInit.GetSubsystem(element.getAttribute("armChainSubsystemID"));
    SubsystemBase temp_wrist = RobotInit.GetSubsystem(element.getAttribute("wristSubsystemID"));
    SubsystemBase temp_claw = RobotInit.GetSubsystem(element.getAttribute("clawSubsystemID"));
    // SubsystemBase temp_claw =
    // RobotInit.GetSubsystem(element.getAttribute("clawSubsystemID"));
    // SubsystemBase temp_rotation =
    // RobotInit.GetSubsystem(element.getAttribute("wristSubsystemID"));
    if (temp_arm == null || !(temp_arm instanceof Motor)
        || temp_arm_chain == null || !(temp_arm_chain instanceof Motor)
        || temp_wrist == null || !(temp_wrist instanceof Motor) || temp_claw == null || !(temp_claw instanceof Motor)) {
      System.out.println(
          "Arm Chain, Arm, or Wrist could not find Motor subsystem with id:"
              + element.getAttribute("armChainSubsystemID") + "," +
              element.getAttribute("armSubsystemID") + "," +
              element.getAttribute("wristSubsystemID") + "," + element.getAttribute("clawSubsystemID"));
      return;
    }
    arm = (Motor) temp_arm;
    this.addRequirements(arm);
    arm_chain = (Motor) temp_arm_chain;
    this.addRequirements(arm_chain);
    wrist = (Motor) temp_wrist;
    this.addRequirements(wrist);
    // claw = (Motor) temp_claw;
    // this.addRequirements(claw);
  }

  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  @Override
  public void execute() {

    System.out.println("scorePosCommand: " + text);
    if (myElement.hasAttribute("autoType")) {
      command = myElement.getAttribute("autoType");
    } else if (myElement.hasAttribute("type")) {
      command = myElement.getAttribute("type");
    }
    switch (command) {
      case "score_high":
        isFinished = false;

        arm.setReference(values.get("score_high_arm"), CommandMode.POSITION);
        if (m_timer.get() <= .25 && m_timer.get() >= .2) {
          arm_chain.setReference(values.get("score_high_armChain"), CommandMode.POSITION);
        }
        if (m_timer.get() >= .4) {
          wrist.setReference(values.get("wrist_score"), CommandMode.POSITION);
          isFinished = true;
        }
        break;
      case "score_low":
        isFinished = false;
        arm.setReference(values.get("score_low_arm"), CommandMode.POSITION);
        if (m_timer.get() <= .25 && m_timer.get() >= .2) {
          arm_chain.setReference(values.get("score_low_armChain"), CommandMode.POSITION);
        }
        if (m_timer.get() >= .4) {
          wrist.setReference(values.get("wrist_score"), CommandMode.POSITION);
          isFinished = true;
        }

        break;
      case "score_high_auto":
        isFinished = false;
        // wrist.setReference(values.get("wrist_down"), CommandMode.POSITION);
        arm.setReference(values.get("score_high_arm"), CommandMode.POSITION);
        if (m_timer.get() >= 2) {
          arm_chain.setReference(values.get("score_high_armChain"), CommandMode.POSITION);
          isFinished = true;
        }
        break;
      // case "store_med":
      // isFinished = false;
      // arm.setReference(values.get("score_medium_arm"), CommandMode.POSITION);
      // if (m_timer.get() >= .5) {
      // arm_chain.setReference(values.get("score_medium_armChain"),
      // CommandMode.POSITION);
      // isFinished = true;
      // }
      // break;
      case "transport":
        isFinished = false;

        wrist.setReference(values.get("wrist_up"), CommandMode.POSITION);
        // claw.setReference(1, CommandMode.PERCENTAGE);
        // if (m_timer.get() <= .2 && m_timer.get() >= .18) {
        // claw.setReference(0, CommandMode.PERCENTAGE);
        // }
        if (m_timer.get() <= .25 && m_timer.get() >= .2) {
          arm_chain.setReference(values.get("transport_armChain"), CommandMode.POSITION);
        }
        if (m_timer.get() >= .8) {
          arm.setReference(values.get("transport_arm"), CommandMode.POSITION);
          isFinished = true;
        }
        break;
      // isFinished = false;
      // arm_chain.setReference(values.get("transport_armChain"),
      // CommandMode.POSITION);
      // if (m_timer.get() >= .25) {
      // arm.setReference(values.get("transport_arm"), CommandMode.POSITION);
      // isFinished = true;
      // }
      // break;
      case "pickup_double":
        isFinished = false;
        arm.setReference(values.get("pickup_double_arm"), CommandMode.POSITION);
        if (m_timer.get() <= .25 && m_timer.get() >= .2) {
          arm_chain.setReference(values.get("pickup_double_armChain"), CommandMode.POSITION);
        }
        if (m_timer.get() >= .8) {
          wrist.setReference(values.get("wrist_pickup_double"), CommandMode.POSITION);
          isFinished = true;
        }
        break;
      case "transport_auto":
        arm.setReference(values.get("transport_arm"), CommandMode.POSITION);
        if (m_timer.get() >= 5) {
          arm_chain.setReference(values.get("transport_armChain"), CommandMode.POSITION);
          isFinished = true;
        }
        break;
      // case "pickup_single":
      // isFinished = false;
      // arm.setReference(values.get("pickup_single_arm"), CommandMode.POSITION);
      // if (m_timer.get() >= .5) {
      // arm_chain.setReference(values.get("pickup_single_armChain"),
      // CommandMode.POSITION);
      // isFinished = true;
      // }
      // break;
      // case "unstow":
      // isFinished = false;
      // arm.setReference(values.get("unstowed_arm"), CommandMode.POSITION);
      // if (m_timer.get() >= .5) {
      // arm_chain.setReference(values.get("unstowed_armChain"),
      // CommandMode.POSITION);
      // isFinished = true;
      // }
      // break;
      case "stay_claw":
        double encoder_value = claw.getMotor().getPosition();
        wrist.setReference(encoder_value, CommandMode.POSITION);
        break;
      default:
        break;

    }
  }

  @Override
  public void end(boolean interrupted) {
    m_timer.stop();
  }

  @Override
  public boolean isFinished() {
    return isFinished;
  }

  @Override
  public void ReadXML(Element element) {
    text = element.getAttribute("myParam");
  }

  @Override
  public void ReloadConfig() {

  }
}