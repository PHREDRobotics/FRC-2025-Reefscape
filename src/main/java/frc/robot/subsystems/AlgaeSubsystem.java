package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.ShooterConstants;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class AlgaeSubsystem extends SubsystemBase {
    private static final Timer timer = new Timer();
    public SparkMax leftAlgaeSparkMax = new SparkMax(ShooterConstants.kLeftShooterControllerPort, MotorType.kBrushless);
    public SparkMax rightAlgaeSparkMax = new SparkMax(ShooterConstants.kRightShooterControllerPort, MotorType.kBrushless);

    public double algaeSpeed = ShooterConstants.kShooterSpeed;

    public void Intake() {
        timer.reset();
        leftAlgaeSparkMax.set(speedConvert(algaeSpeed));
        rightAlgaeSparkMax.set(speedConvert(algaeSpeed));
    }

    public double speedConvert(double inSpeed) {
        if (inSpeed < 0.2 && inSpeed > -0.2) {
            return 0.0;
        }
        return inSpeed;
    }
    
    public void stopIntake() {
        leftAlgaeSparkMax.set(0);
        rightAlgaeSparkMax.set(0);
    }

    public static boolean shooterIsTimeDone() {
        return timer.hasElapsed(ShooterConstants.kShooterTime);
    }
    // Timer doesn't do much currently, but we'll still reset it for command use.

    @Override
    public void periodic() {
        // Slider variables
        algaeSpeed = SmartDashboard.getNumber("Algae Speed", algaeSpeed);
        SmartDashboard.putNumber("Algae Speed", algaeSpeed);

        // This will be called once per scheduler run
    }
}
