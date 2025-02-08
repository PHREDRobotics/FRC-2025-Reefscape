// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.composition;

// import frc.robot.subsystems.SwerveSubsystem;
// import frc.robot.commands.*;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// /** An example command that uses an example subsystem. */
// public class ManualTagAuto extends SequentialCommandGroup {
//   @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
//   private final SwerveSubsystem m_SwerveSubsystem;

//   /**
//    * Creates a new ExampleCommand.
//    *
//    * @param subsystem The subsystem used by this command.
//    */
//   public ManualTagAuto(SwerveSubsystem swerve) {
//     m_SwerveSubsystem = swerve;
//     // Use addRequirements() here to declare subsystem dependencies.
//     addRequirements(swerve);

//     addCommands(
//         // Resets odometery and heading
//         new InstantCommand(() -> m_SwerveSubsystem.zeroHeading()),
//         new GoToPose2d(swerve, new Translation2d(3.0, 0.0)));
//   }
// }