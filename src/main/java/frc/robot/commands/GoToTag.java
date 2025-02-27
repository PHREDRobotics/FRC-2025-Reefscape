package frc.robot.commands;

import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Command that makes the robot go to a tag idiot;
 */
public class GoToTag extends Command {
    private VisionSubsystem m_vision_subsystem;
    private SwerveSubsystem m_swerve_subsystem;

    private static final TrapezoidProfile.Constraints X_CONSTRAINTS = new TrapezoidProfile.Constraints(
            VisionConstants.kMaxSpeed, VisionConstants.kMaxAcceleration);
    private static final TrapezoidProfile.Constraints Y_CONSTRAINTS = new TrapezoidProfile.Constraints(
            VisionConstants.kMaxSpeed, VisionConstants.kMaxAcceleration);
    private static final TrapezoidProfile.Constraints OMEGA_CONSTRAINTS = new TrapezoidProfile.Constraints(
            VisionConstants.kMaxAngularSpeed, VisionConstants.kMaxAngularAcceleration);

    private final ProfiledPIDController xController = new ProfiledPIDController(VisionConstants.kXYP,
            VisionConstants.kXYI, VisionConstants.kXYI, X_CONSTRAINTS);
    private final ProfiledPIDController yController = new ProfiledPIDController(VisionConstants.kXYP,
            VisionConstants.kXYI, VisionConstants.kXYI, Y_CONSTRAINTS);
    private final ProfiledPIDController omegaController = new ProfiledPIDController(VisionConstants.kRP,
            VisionConstants.kRI, VisionConstants.kRI, OMEGA_CONSTRAINTS);

    private double xSpeed;
    private double ySpeed;
    private double turningSpeed;

    private int targetTag;

    private boolean isFinished;

    // private static final int TAG_TO_CHASE = 5;
    // private static final Transform3d TAG_TO_GOAL = new Transform3d(
    // new Translation3d(1.0, 0.0, 0.0),
    // new Rotation3d(0.0, 0.0, Math.PI));

    /**
     * Creates a new GoToTag.
     *
     * @param vision_subsystem Vision subsystem
     * @param swerve_subsystem Swerve subsystem
     * @param target_tag       The tag id to drive to
     */
    public GoToTag(VisionSubsystem vision_subsystem, SwerveSubsystem swerve_subsystem, int target_tag) {
        m_vision_subsystem = vision_subsystem;
        m_swerve_subsystem = swerve_subsystem;

        xController.setTolerance(VisionConstants.kPositionTolerance);
        yController.setTolerance(VisionConstants.kPositionTolerance);
        omegaController.setTolerance(Units.degreesToRadians(VisionConstants.kAngleTolerance));
        omegaController.enableContinuousInput(-Math.PI, Math.PI);

        targetTag = target_tag;

        if (targetTag == -1) {
            isFinished = true;
        }

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_vision_subsystem);
        addRequirements(swerve_subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // lastTarget(or whatever we call it)=null

    }

    /*
     * The fact that this is the execute code seems a little overkill
     * Seems like there could be a better way to do this.
     */
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // 1. get current pos
        Pose2d currentPose2d = m_swerve_subsystem.getPose();
        // 2. get target pos off of current targeted apriltag
        Pose2d targetPose2d = new Pose2d(); // just in case of errors

        targetPose2d = getTargetPose(VisionConstants.kAprilTags[targetTag - 1]);

        TrapezoidProfile.State targetxState = new TrapezoidProfile.State(targetPose2d.getX(), 0);
        TrapezoidProfile.State targetyState = new TrapezoidProfile.State(targetPose2d.getY(), 0);
        TrapezoidProfile.State targetomegaState = new TrapezoidProfile.State(targetPose2d.getRotation().getRadians(),
                0);
        // 3. set speeds using pid
        xSpeed = xController.calculate(currentPose2d.getX(), targetxState, X_CONSTRAINTS);
        ySpeed = yController.calculate(currentPose2d.getY(), targetyState, Y_CONSTRAINTS);
        turningSpeed = omegaController.calculate(currentPose2d.getRotation().getRadians(), targetomegaState,
                OMEGA_CONSTRAINTS);
        // 4. Output speeds to wheels
        m_swerve_subsystem.drive(
                () -> xSpeed,
                () -> ySpeed,
                () -> turningSpeed,
                () -> false);

        // Smart dash varibs. The string ones proably work
        SmartDashboard.putNumber("Automove/X-Speed", xSpeed);
        SmartDashboard.putNumber("Automove/Y-Speed", ySpeed);
        SmartDashboard.putNumber("Automove/Turn-Speed", turningSpeed);
        SmartDashboard.putString("Automove/Target-Pose", targetPose2d.toString());
        SmartDashboard.putString("Automove/Current-Pose", currentPose2d.toString());
    }

    /**
     * @param interrupted
     */
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    private Pose2d getTargetPose(Pose2d tag) {
        Pose2d targetPose = new Pose2d(
                tag.getX()
                        + VisionConstants.kMetersFromAprilTag * Math.cos(tag.getRotation().getRadians()),
                tag.getY()
                        + VisionConstants.kMetersFromAprilTag * Math.sin(tag.getRotation().getRadians()),
                tag.getRotation().rotateBy(new Rotation2d(Math.PI)));

        return targetPose;
    }
}