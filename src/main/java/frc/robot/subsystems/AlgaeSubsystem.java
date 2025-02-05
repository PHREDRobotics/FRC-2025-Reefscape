// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import edu.wpi.first.wpilibj.Timer;
// import frc.robot.Constants.AlgaeConstants;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.SparkMax;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// public class AlgaeSubsystem extends SubsystemBase {
//     private static final Timer timer = new Timer();
//     public SparkMax leftAlgaeSparkMax = new SparkMax(AlgaeConstants.kLeftAlgaeControllerPort, MotorType.kBrushless);
//     public SparkMax rightAlgaeSparkMax = new SparkMax(AlgaeConstants.kRightAlgaeControllerPort, MotorType.kBrushless);

//     public double algaeSpeed = AlgaeConstants.kAlgaeSpeed;

//     public void Intake() {
//         timer.reset();
//         leftAlgaeSparkMax.set(speedConvert(algaeSpeed)); // In last years code this was similar to pickUpNote() in IntakeSubsystem.
//         rightAlgaeSparkMax.set(speedConvert(-algaeSpeed));
//     }

//     public void Outtake() {
//         timer.reset();
//         leftAlgaeSparkMax.set(speedConvert(-algaeSpeed));
//         rightAlgaeSparkMax.set(speedConvert(algaeSpeed));
//     }

//     public double speedConvert(double inSpeed) {
//         if (inSpeed < 0.2 && inSpeed > -0.2) {
//             return 0.0;
//         }
//         return inSpeed;
//     }
    
//     public void stopIntake() {
//         leftAlgaeSparkMax.set(0);
//         rightAlgaeSparkMax.set(0);
//     }

//     public static boolean algaeIsTimeDone() {
//         return timer.hasElapsed(AlgaeConstants.kAlgaeTime);
//     }
//     // Timer doesn't do much currently, but we'll still reset it for command use.

//     @Override
//     public void periodic() {
//         // Slider variables
//         algaeSpeed = SmartDashboard.getNumber("Algae Speed", algaeSpeed);
//         SmartDashboard.putNumber("Algae Speed", algaeSpeed);

//         // This will be called once per scheduler run
//     }
// }
