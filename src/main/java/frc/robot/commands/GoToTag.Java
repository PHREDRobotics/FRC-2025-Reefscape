package frc.robot.commands;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
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

  private double xSpeed;
  private double ySpeed;
  private double turningSpeed;

  private double targetTag;

  private boolean isFinished;

  // private static final int TAG_TO_CHASE = 5;
  // private static final Transform3d TAG_TO_GOAL = new Transform3d(
  //     new Translation3d(1.0, 0.0, 0.0),
  //     new Rotation3d(0.0, 0.0, Math.PI));

  public GoToTag(VisionSubsystem vision_subsystem, SwerveSubsystem swerve_subsystem, double target_tag) {
    m_vision_subsystem = vision_subsystem;
    m_swerve_subsystem = swerve_subsystem;

    xController.setTolerance(0.2);
    yController.setTolerance(0.2);
    omegaController.setTolerance(Units.degreesToRadians(3));
    omegaController.enableContinuousInput(-Math.PI, Math.PI);

    targetTag = target_tag;
    //vision_subsystem.getTargetID();

    if (targetTag == -1) {
      isFinished = true;
    }

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(vision_subsystem);
    addRequirements(swerve_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // lastTarget(or whatever we call it)=null

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // 1. get current pos
    Pose2d currentPose2d = m_swerve_subsystem.getPose();
    // 2. get target pos off of current targeted apriltag
    Pose2d targetPose2d = new Pose2d(); // just in case of errors
    if (targetTag == 1) {

    } else if (targetTag == 2) {

    } else if (targetTag == 3) {

    } else if (targetTag == 4) {

    } else if (targetTag == 5) {

    } else if (targetTag == 6) {

    } else if (targetTag == 7) {

    } else if (targetTag == 8) {

    } else if (targetTag == 9) {

    } else if (targetTag == 10) {

    } else if (targetTag == 11) {

    } else if (targetTag == 12) {
      Pose2d tag12 = VisionConstants.kAprilTag12;
      targetPose2d = new Pose2d(
          tag12.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag12.getRotation().getRadians()),
          tag12.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag12.getRotation().getRadians()),
          tag12.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 13) {

    } else if (targetTag == 14) {

    } else if (targetTag == 15) {

    } else if (targetTag == 16) {

    } else if (targetTag == 17) {

    } else if (targetTag == 18) {

    } else if (targetTag == 19) {

    } else if (targetTag == 20) {

    } else if (targetTag == 21) {

    } else if (targetTag == 22) {

    }
    TrapezoidProfile.State targetxState = new TrapezoidProfile.State(targetPose2d.getX(), 0);
    TrapezoidProfile.State targetyState = new TrapezoidProfile.State(targetPose2d.getY(), 0);
    TrapezoidProfile.State targetomegaState = new TrapezoidProfile.State(targetPose2d.getRotation().getRadians(), 0);
    // 3. set speeds using pid
    xSpeed = xController.calculate(currentPose2d.getX(), targetxState, X_CONSTRAINTS);
    ySpeed = yController.calculate(currentPose2d.getY(), targetyState, Y_CONSTRAINTS);
    xSpeed = omegaController.calculate(currentPose2d.getRotation().getRadians(), targetomegaState, OMEGA_CONSTRAINTS);
    // 4. make chassis speeds
    ChassisSpeeds chassisSpeeds;
    chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
        xSpeed, ySpeed, turningSpeed, m_swerve_subsystem.getRotation2d());
    // 5. Convert chassis speeds to individual module states
    SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
    // state.angle.getRadians());
    // 6. Output each module states to wheels
    m_swerve_subsystem.setModuleStates(moduleStates);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}