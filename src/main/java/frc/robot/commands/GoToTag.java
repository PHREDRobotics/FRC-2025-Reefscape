package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private int targetTag;

    // private static final int TAG_TO_CHASE = 5;
    // private static final Transform3d TAG_TO_GOAL = new Transform3d(
    // new Translation3d(1.0, 0.0, 0.0),
    // new Rotation3d(0.0, 0.0, Math.PI));

    public GoToTag(VisionSubsystem vision_subsystem, SwerveSubsystem swerve_subsystem, int target_tag) {
        m_vision_subsystem = vision_subsystem;
        m_swerve_subsystem = swerve_subsystem;

        xController.setTolerance(0.2);
        yController.setTolerance(0.2);
        omegaController.setTolerance(Units.degreesToRadians(3));
        omegaController.enableContinuousInput(-Math.PI, Math.PI);

        targetTag = target_tag;

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
        switch (targetTag) {
            case 1:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag1);
                break;
            case 2:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag2);
                break;
            case 3:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag3);
                break;
            case 4:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag4);
                break;
            case 5:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag5);
                break;
            case 6:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag6);
                break;
            case 7:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag7);
                break;
            case 8:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag8);
                break;
            case 9:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag9);
                break;
            case 10:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag10);
                break;
            case 11:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag11);
                break;
            case 12:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag12);
                break;
            case 13:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag13);
                break;
            case 14:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag14);
                break;
            case 15:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag15);
                break;
            case 16:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag16);
                break;
            case 17:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag17);
                break;
            case 18:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag18);
                break;
            case 19:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag19);
                break;
            case 20:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag20);
                break;
            case 21:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag21);
                break;
            case 22:
                targetPose2d = getTargetPose(VisionConstants.kAprilTag22);
                break;
        }

        TrapezoidProfile.State targetxState = new TrapezoidProfile.State(targetPose2d.getX(), 0);
        TrapezoidProfile.State targetyState = new TrapezoidProfile.State(targetPose2d.getY(), 0);
        TrapezoidProfile.State targetomegaState = new TrapezoidProfile.State(targetPose2d.getRotation().getRadians(),
                0);
        // 3. set speeds using pid
        xSpeed = xController.calculate(currentPose2d.getX(), targetxState, X_CONSTRAINTS);
        ySpeed = yController.calculate(currentPose2d.getY(), targetyState, Y_CONSTRAINTS);
        turningSpeed = omegaController.calculate(currentPose2d.getRotation().getRadians(), targetomegaState,
                OMEGA_CONSTRAINTS);
        // 4. make chassis speeds
        m_swerve_subsystem.drive(() -> xSpeed,() -> ySpeed,() -> turningSpeed,() -> true);
        // 5. ignore this junk:
        // ChassisSpeeds chassisSpeeds;
        // chassisSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(
        // xSpeed, ySpeed, turningSpeed, m_swerve_subsystem.getPose().getRotation());
        // // 5. Convert chassis speeds to individual module states
        // m_swerve_subsystem.drive(chassisSpeeds, () -> false);

        // Smart dash varibs. The string ones proably work
        SmartDashboard.putNumber("X-Speed", xSpeed);
        SmartDashboard.putNumber("Y-Speed", ySpeed);
        SmartDashboard.putNumber("Turn-Speed", turningSpeed);
        SmartDashboard.putString("Target-Pose", targetPose2d.toString());
        SmartDashboard.putString("Current-Pose", currentPose2d.toString());
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