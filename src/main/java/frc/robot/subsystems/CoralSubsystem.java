package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CoralConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class CoralSubsystem extends SubsystemBase {
  private static final Timer timer = new Timer();
  public SparkMax coralMotorSparkMax = new SparkMax(CoralConstants.kCoralSparkMaxCanID, MotorType.kBrushless);
  public SparkLimitSwitch forwardLimit = coralMotorSparkMax.getForwardLimitSwitch();
  
  

  public double intakeSpeed = CoralConstants.kCoralIntakeSpeed;
  public double outtakeSpeed = CoralConstants.kCoralOuttakeSpeed;

  public CoralSubsystem() {
    super();
    SmartDashboard.putString("Outtake Speed: ", String.valueOf(outtakeSpeed));
  }

  public void startOuttake() {
    timer.reset();
    coralMotorSparkMax.set(outtakeSpeed);
    timer.start();
  }

  public double speedConvert(double inSpeed) {
    //if (inSpeed < 0.2 && inSpeed > -0.2) {
    //  return 0.0;
    //} else {
      return inSpeed;
    //}
  }
  
  public void stop() {
    coralMotorSparkMax.set(0);
  }

 public void startIntake(){
  coralMotorSparkMax.set(intakeSpeed);
 }

  public boolean isCoralLoaded() {
    return !forwardLimit.isPressed();
  }

  public static boolean outtakeIsTimeDone() {
    return timer.hasElapsed(Constants.CoralConstants.kCoralOuttakeTime);

  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Pressed?", isCoralLoaded());
    SmartDashboard.putBoolean("Manual Override Press", SmartDashboard.getBoolean("Manual Override Press", false));
    SmartDashboard.putBoolean("Is Limit Switch Triggered?", !forwardLimit.isPressed());
    

  // Slider things VARIABLES
  outtakeSpeed=SmartDashboard.getNumber("Outtake Speed", outtakeSpeed);
  intakeSpeed=SmartDashboard.getNumber("Intake Speed", intakeSpeed);
  SmartDashboard.putNumber("Outtake Speed", outtakeSpeed);
  SmartDashboard.putNumber("Intake Speed", intakeSpeed);
  // This method will be called once per scheduler run
  // We will have a pull in fast and slow and a push out fast and slow
  // When we pull in we will use the beam break sensor to stop the motor
  }

}