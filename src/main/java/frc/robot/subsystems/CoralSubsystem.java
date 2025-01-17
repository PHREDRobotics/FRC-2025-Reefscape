package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CoralConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.TimeUnit;
import edu.wpi.first.wpilibj.Timer;
import javax.print.attribute.standard.RequestingUserName;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class CoralSubsystem extends SubsystemBase {
    private static final Timer timer = new Timer();
    public SparkMax coralMotorSparkMax = new SparkMax(CoralConstants.kCoralSparkMaxCanID,MotorType.kBrushless);
    //public SparkLimitSwitch m_forwardLimit = coralMotorSparkMax.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

    public double intake_speed = CoralConstants.kCoralIntakeSpeed;
    public double outtake_speed = CoralConstants.kCoralOuttakeSpeed;

    public CoralSubsystem() { 
       super();
       // SmartDashboard.putnumber("Outtake Speed", kcoralouttakeSpeed)
    }

    public void Outtake(){
    timer.reset();
    coralMotorSparkMax.set(speedConvert(-outtake_speed));
    timer.start();
    }

    public double speedConvert(double inSpeed) {
        if (inSpeed < 0.2 && inSpeed > -0.2) {
            return 0.0;
    }
    else{
        return inSpeed;
    }
}
    
  public void stopIntake() {
    coralMotorSparkMax.set(0);
  }
  public void pickUpCoral() {
    // This will stop when the beam in our beam break sensor is broken

    coralMotorSparkMax.set(outtake_speed);
  }

  //public boolean isCoralLoaded() {
  //  return forwardLimit.isPressed() || SmartDashboard.getBoolean("Manual Override Press", false);
 // }
 public static boolean outtakeIsTimeDone() {
    return timer.hasElapsed(Constants.GrabberConstants.kOuttakeTime);

  }
 /* public void ejectToShooter() {
    // This will be slower than ejectToAmp

  }*/
  
}