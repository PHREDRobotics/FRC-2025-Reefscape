// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SwerveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. (Command calls and such)
 */
public class RobotContainer {

  private SwerveSubsystem swerveSubsystem = new SwerveSubsystem();

  // CommandXboxController is not what we have used in the past, but seem to be better than the regular XboxController.
  private final CommandXboxController m_driverController =
      new CommandXboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    configureBindings();
  }
  
  /**
   * This method is used to define trigger->command mappings.
   */
  private void configureBindings() {

    Trigger startButton = new Trigger(m_driverController.start());
    Trigger yButton = new Trigger(m_driverController.y());

    startButton.toggleOnTrue(swerveSubsystem.toggleFieldOrientedCommand());
    yButton.onTrue(swerveSubsystem.resetGyro());

    swerveSubsystem.setDefaultCommand(swerveSubsystem.driveCommand(
      () -> m_driverController.getLeftY(), 
      () -> m_driverController.getLeftX(), 
      () -> -m_driverController.getRightTriggerAxis()
      ));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  /* public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  } */
}
