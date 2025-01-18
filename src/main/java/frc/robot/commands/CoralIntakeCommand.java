package frc.robot.commands;

import frc.robot.subsystems.CoralSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class CoralIntakeCommand extends Command {
  /** An Intake command that uses an Intake subsystem. */
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final CoralSubsystem coralSubsystem;

  public CoralIntakeCommand(CoralSubsystem subsystem) {
    coralSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(coralSubsystem);

  }

  @Override
  public void initialize() {
    coralSubsystem.pickUpCoral();
  }

  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    coralSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return coralSubsystem.isCoralLoaded();
  }
}
