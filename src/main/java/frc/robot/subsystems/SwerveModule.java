// package frc.robot.subsystems;

// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.SparkBase.PersistMode;
// import com.revrobotics.spark.SparkBase.ResetMode;
// import com.revrobotics.RelativeEncoder;
// import com.revrobotics.spark.SparkClosedLoopController;
// import com.revrobotics.spark.SparkLowLevel.MotorType;

// import com.revrobotics.AbsoluteEncoder;
// import edu.wpi.first.math.kinematics.SwerveModulePosition;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.kinematics.SwerveModuleState;
// import frc.robot.Configs;
// import frc.robot.Constants;

// /**
//  * Constructs a MAXSwerveModule and configures the driving and turning motor,
//  * encoder, and PID controller. This configuration is specific to the REV
//  * MAXSwerve Module built with NEOs, SPARKS MAX, and a Through Bore
//  * Encoder.
//  */
// public class SwerveModule {

//   private final SparkMax driveSparkMax;
//   private final SparkMax turningSparkMax;

//   private final RelativeEncoder driveEncoder;
//   private final AbsoluteEncoder turningEncoder;

//   private final SparkClosedLoopController drivingClosedLoopController;
//   private final SparkClosedLoopController turningClosedLoopController;

//   private double chassisAngularOffset = 0;
//   private SwerveModuleState moduleDesiredState = new SwerveModuleState(0.0, new Rotation2d());

//   public SwerveModule(
//       int driveMotorCANId,
//       int turningMotorCANId,
//       double moduleAngularOffset) {

//     // Driving SparkMax
//     driveSparkMax = new SparkMax(driveMotorCANId, MotorType.kBrushless);
//     driveEncoder = driveSparkMax.getEncoder();
//     drivingClosedLoopController = driveSparkMax.getClosedLoopController();

//     driveSparkMax.configure(Configs.MAXSwerveModule.drivingConfig, ResetMode.kResetSafeParameters,
//         PersistMode.kPersistParameters);

//     // Turning SparkMax
//     turningSparkMax = new SparkMax(turningMotorCANId, MotorType.kBrushless);
//     turningEncoder = turningSparkMax.getAbsoluteEncoder();
//     turningClosedLoopController = turningSparkMax.getClosedLoopController();

//     turningSparkMax.configure(Configs.MAXSwerveModule.turningConfig, ResetMode.kResetSafeParameters,
//         PersistMode.kPersistParameters);

//     chassisAngularOffset = moduleAngularOffset;
//     moduleDesiredState.angle = new Rotation2d(turningEncoder.getPosition());
//     driveEncoder.setPosition(0);

//   }

//   public double getDrivePosition() {
//     return -driveEncoder.getPosition();
//   }

//   /**
//    * Returns current turn position in range -pi to pi
//    */
//   // Before it was
//   // return turningSparkMax.getEncoder().getPosition();

//   public double getTurningPosition() {
//     if (turningEncoder.getPosition() >= 0) {
//       return turningEncoder.getPosition();

//     } else {
//       return Constants.k2pi + turningEncoder.getPosition();
//     }
//     // ModuleConstants.kTurningMotorRotationPerSteerRotation;
//   }

//   public SwerveModulePosition getPosition() {
//     return new SwerveModulePosition(
//         driveEncoder.getPosition(),
//         new Rotation2d(turningEncoder.getPosition() - chassisAngularOffset));
//   }

//   public double getDriveVelocity() {
//     return driveEncoder.getVelocity();
//   }

//   public double getTurningVelocity() {
//     return turningEncoder.getVelocity();
//   }

//   public void resetEncoders() {
//     driveEncoder.setPosition(0);
//   }

//   public SwerveModuleState getState() {
//     return new SwerveModuleState(getDriveVelocity(),
//         new Rotation2d(turningEncoder.getPosition() - chassisAngularOffset));
//   }

//   public void setDesiredState(SwerveModuleState desiredState) {
//     SwerveModuleState correctedDesiredState = new SwerveModuleState();
//     correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
//     correctedDesiredState.angle = desiredState.angle.plus(Rotation2d.fromRadians(chassisAngularOffset));
//     // changed fromRadians to fromDegrees

//     // Optimize the reference state to avoid spinning further than 90 degrees.
//     correctedDesiredState.optimize(new Rotation2d(turningEncoder.getPosition()));

//     driveSparkMax.getClosedLoopController().setReference(correctedDesiredState.speedMetersPerSecond,
//         SparkMax.ControlType.kVelocity);
//     turningSparkMax.getClosedLoopController().setReference(correctedDesiredState.angle.getRadians(),
//         SparkMax.ControlType.kPosition);

//     moduleDesiredState = desiredState;
//   }

//   public void stop() {
//     driveSparkMax.set(0);
//     turningSparkMax.set(0);
//   }
// }