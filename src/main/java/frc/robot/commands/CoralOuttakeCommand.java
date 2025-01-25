// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.CoralSubsystem;
import edu.wpi.first.wpilibj2.command.Command;


/** An Intake command that uses an Intake subsystem. */
public class CoralOuttakeCommand extends Command {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final CoralSubsystem coralSubsystem;

  /**
   * Creates a new OuttakeCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public CoralOuttakeCommand(CoralSubsystem subsystem) {
    coralSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    coralSubsystem.startOuttake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }
//   @Override
//   public void superShoot() {
//   }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    coralSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return CoralSubsystem.outtakeIsTimeDone();
   //return true;
  }

}