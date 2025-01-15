package frc.robot.commands;

import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Creates a new VisionCommand.
 *
 * @param subsystem The subsystem used by this command.
 */
public class GoToTag extends Command {
  private VisionSubsystem m_vision_subsystem;
  private SwerveSubsystem m_swerve_subsystem;

  private static final TrapezoidProfile.Constraints X_CONSTRAINTS = new TrapezoidProfile.Constraints(3, 2);
  private static final TrapezoidProfile.Constraints Y_CONSTRAINTS = new TrapezoidProfile.Constraints(3, 2);
  private static final TrapezoidProfile.Constraints OMEGA_CONSTRAINTS = new TrapezoidProfile.Constraints(8, 8);

  private final ProfiledPIDController xController = new ProfiledPIDController(3, 0, 0, X_CONSTRAINTS);
  private final ProfiledPIDController yController = new ProfiledPIDController(3, 0, 0, Y_CONSTRAINTS);
  private final ProfiledPIDController omegaController = new ProfiledPIDController(2, 0, 0, OMEGA_CONSTRAINTS);

  private static final int TAG_TO_CHASE = 5;
  private static final Transform3d TAG_TO_GOAL =
    new Transform3d(
      new Translation3d(1.0, 0.0, 0.0),
      new Rotation3d(0.0, 0.0, Math.PI)
    );

  public GoToTag(VisionSubsystem vision_subsystem, SwerveSubsystem swerve_subsystem) {
    m_vision_subsystem = vision_subsystem;
    m_swerve_subsystem = swerve_subsystem;

    xController.setTolerance(0.2);
    yController.setTolerance(0.2);
    omegaController.setTolerance(Units.degreesToRadians(3));
    omegaController.enableContinuousInput(-Math.PI, Math.PI);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(vision_subsystem);
    addRequirements(swerve_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //lastTarget(or whatever we call it)=null
    
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}