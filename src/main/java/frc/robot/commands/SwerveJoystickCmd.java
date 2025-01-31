// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands;

// import java.util.function.Supplier;
// import edu.wpi.first.math.filter.SlewRateLimiter;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.math.kinematics.SwerveModuleState;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants.DriveConstants;
// import frc.robot.Constants.OIConstants;
// import frc.robot.subsystems.SwerveSubsystem;

// /** An example command that uses an example subsystem. */
// public class SwerveJoystickCmd extends Command {
//   @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
//   private final SwerveSubsystem swerveSubsystem;
//   private final Supplier<Double> xSpdFunction, ySpdFunction, turningSpdFunction, throttleBonusFunction;
//   private final Supplier<Boolean> fieldOrientedFunction;
//   private final SlewRateLimiter xLimiter, yLimiter, turningLimiter;

//   /**
//    * Creates a new ExampleCommand.
//    *
//    * @param subsystem The subsystem used by this command.
//    */
//   public SwerveJoystickCmd(SwerveSubsystem swerveSubsystem,
//       Supplier<Double> xSpdFunction, Supplier<Double> ySpdFunction, Supplier<Double> turningSpdFunction, Supplier<Double> throttleBonusFunction,
//       Supplier<Boolean> fieldOrientedFunction) {
//     this.swerveSubsystem = swerveSubsystem;
//     this.xSpdFunction = xSpdFunction;
//     this.ySpdFunction = ySpdFunction;
//     this.turningSpdFunction = turningSpdFunction;
//     this.throttleBonusFunction = throttleBonusFunction;
//     this.fieldOrientedFunction = fieldOrientedFunction;
//     this.xLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
//     this.yLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
//     this.turningLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond);
//     addRequirements(swerveSubsystem);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
//     }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     // 1. Get real-time joystick inputs
//     double xSpeed = xSpdFunction.get();
//     double ySpeed = ySpdFunction.get();
//     double turningSpeed = turningSpdFunction.get();

//     // 2. Apply deadband
//     xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
//     ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
//     turningSpeed = Math.abs(turningSpeed) > OIConstants.kHighDeadband ? turningSpeed : 0.0;

//     // 2.5. Square/cUBE it
//     xSpeed = xSpeed * Math.abs(xSpeed);// * Math.abs(xSpeed);
//     ySpeed = ySpeed * Math.abs(ySpeed);// * Math.abs(ySpeed);
//     turningSpeed = turningSpeed * Math.abs(turningSpeed);// * Math.abs(turningSpeed);

//     // 3. Make the driving smoother
//     xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond + throttleBonusFunction.get() * DriveConstants.kTeleDriveThrottleMultiplier;
//     ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond + throttleBonusFunction.get() * DriveConstants.kTeleDriveThrottleMultiplier;
//     turningSpeed = turningLimiter.calculate(turningSpeed)
//         * DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond;



//     // 4. Construct desired chassis speeds
//     ChassisSpeeds chassisSpeeds;
//     if (fieldOrientedFunction.get()) {
//        //Relative to field
//       chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
//           xSpeed, ySpeed, turningSpeed, swerveSubsystem.getRotation2d());
//     } else {
//       // Relative to robot
//       chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turningSpeed);
//     }
//     SmartDashboard.putBoolean("Is Field Oriented", fieldOrientedFunction.get());
//     // 5. Convert chassis speeds to individual module states
//     SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
//    //state.angle.getRadians());
//     // 6. Output each module states to wheels
//    swerveSubsystem.setModuleStates(moduleStates);

//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//     swerveSubsystem.stopModules();
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }