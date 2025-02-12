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
      Pose2d tag1 = VisionConstants.kAprilTag1;
      targetPose2d = new Pose2d(
          tag1.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag1.getRotation().getRadians()),
          tag1.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag1.getRotation().getRadians()),
          tag1.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 2) {
      Pose2d tag2 = VisionConstants.kAprilTag2;
      targetPose2d = new Pose2d(
          tag2.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag2.getRotation().getRadians()),
          tag2.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag2.getRotation().getRadians()),
          tag2.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 3) {
      Pose2d tag3 = VisionConstants.kAprilTag3;
      targetPose2d = new Pose2d(
          tag3.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag3.getRotation().getRadians()),
          tag3.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag3.getRotation().getRadians()),
          tag3.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 4) {
      Pose2d tag4 = VisionConstants.kAprilTag4;
      targetPose2d = new Pose2d(
          tag4.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag4.getRotation().getRadians()),
          tag4.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag4.getRotation().getRadians()),
          tag4.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 5) {
      Pose2d tag5 = VisionConstants.kAprilTag5;
      targetPose2d = new Pose2d(
          tag5.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag5.getRotation().getRadians()),
          tag5.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag5.getRotation().getRadians()),
          tag5.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 6) {
      Pose2d tag6 = VisionConstants.kAprilTag6;
      targetPose2d = new Pose2d(
          tag6.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag6.getRotation().getRadians()),
          tag6.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag6.getRotation().getRadians()),
          tag6.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 7) {
      Pose2d tag7 = VisionConstants.kAprilTag7;
      targetPose2d = new Pose2d(
          tag7.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag7.getRotation().getRadians()),
          tag7.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag7.getRotation().getRadians()),
          tag7.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 8) {
      Pose2d tag8 = VisionConstants.kAprilTag8;
      targetPose2d = new Pose2d(
          tag8.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag8.getRotation().getRadians()),
          tag8.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag8.getRotation().getRadians()),
          tag8.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 9) {
      Pose2d tag9 = VisionConstants.kAprilTag9;
      targetPose2d = new Pose2d(
          tag9.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag9.getRotation().getRadians()),
          tag9.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag9.getRotation().getRadians()),
          tag9.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 10) {
      Pose2d tag10 = VisionConstants.kAprilTag10;
      targetPose2d = new Pose2d(
          tag10.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag10.getRotation().getRadians()),
          tag10.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag10.getRotation().getRadians()),
          tag10.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 11) {
      Pose2d tag11 = VisionConstants.kAprilTag11;
      targetPose2d = new Pose2d(
          tag11.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag11.getRotation().getRadians()),
          tag11.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag11.getRotation().getRadians()),
          tag11.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 12) {
      Pose2d tag12 = VisionConstants.kAprilTag12;
      targetPose2d = new Pose2d(
          tag12.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag12.getRotation().getRadians()),
          tag12.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag12.getRotation().getRadians()),
          tag12.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 13) {
      Pose2d tag13 = VisionConstants.kAprilTag13;
      targetPose2d = new Pose2d(
          tag13.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag13.getRotation().getRadians()),
          tag13.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag13.getRotation().getRadians()),
          tag13.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 14) {
      Pose2d tag14 = VisionConstants.kAprilTag14;
      targetPose2d = new Pose2d(
          tag14.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag14.getRotation().getRadians()),
          tag14.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag14.getRotation().getRadians()),
          tag14.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 15) {
      Pose2d tag15 = VisionConstants.kAprilTag15;
      targetPose2d = new Pose2d(
          tag15.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag15.getRotation().getRadians()),
          tag15.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag15.getRotation().getRadians()),
          tag15.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 16) {
      Pose2d tag16 = VisionConstants.kAprilTag16;
      targetPose2d = new Pose2d(
          tag16.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag16.getRotation().getRadians()),
          tag16.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag16.getRotation().getRadians()),
          tag16.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 17) {
      Pose2d tag17 = VisionConstants.kAprilTag17;
      targetPose2d = new Pose2d(
          tag17.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag17.getRotation().getRadians()),
          tag17.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag17.getRotation().getRadians()),
          tag17.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 18) {
      Pose2d tag18 = VisionConstants.kAprilTag18;
      targetPose2d = new Pose2d(
          tag18.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag18.getRotation().getRadians()),
          tag18.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag18.getRotation().getRadians()),
          tag18.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 19) {
      Pose2d tag19 = VisionConstants.kAprilTag19;
      targetPose2d = new Pose2d(
          tag19.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag19.getRotation().getRadians()),
          tag19.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag19.getRotation().getRadians()),
          tag19.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 20) {
      Pose2d tag20 = VisionConstants.kAprilTag20;
      targetPose2d = new Pose2d(
          tag20.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag20.getRotation().getRadians()),
          tag20.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag20.getRotation().getRadians()),
          tag20.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 21) {
      Pose2d tag21 = VisionConstants.kAprilTag21;
      targetPose2d = new Pose2d(
          tag21.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag21.getRotation().getRadians()),
          tag21.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag21.getRotation().getRadians()),
          tag21.getRotation().rotateBy(new Rotation2d(Math.PI)));
    } else if (targetTag == 22) {
      Pose2d tag22 = VisionConstants.kAprilTag22;
      targetPose2d = new Pose2d(
          tag22.getX()
              + VisionConstants.kMetersFromAprilTag * Math.cos(tag22.getRotation().getRadians()),
          tag22.getY()
              + VisionConstants.kMetersFromAprilTag * Math.sin(tag22.getRotation().getRadians()),
          tag22.getRotation().rotateBy(new Rotation2d(Math.PI)));
    }
    TrapezoidProfile.State targetxState = new TrapezoidProfile.State(targetPose2d.getX(), 0);
    TrapezoidProfile.State targetyState = new TrapezoidProfile.State(targetPose2d.getY(), 0);
    TrapezoidProfile.State targetomegaState = new TrapezoidProfile.State(targetPose2d.getRotation().getRadians(), 0);
    // 3. set speeds using pid
    xSpeed = xController.calculate(currentPose2d.getX(), targetxState, X_CONSTRAINTS);
    ySpeed = yController.calculate(currentPose2d.getY(), targetyState, Y_CONSTRAINTS);
    xSpeed = omegaController.calculate(currentPose2d.getRotation().getRadians(), targetomegaState, OMEGA_CONSTRAINTS);
    // 4. make chassis speeds
    // ChassisSpeeds chassisSpeeds;
    // chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
    //     xSpeed, ySpeed, turningSpeed, m_swerve_subsystem.getRotation2d());
    // // 5. Convert chassis speeds to individual module states
    // SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
    // // state.angle.getRadians());
    // // 6. Output each module states to wheels
    // m_swerve_subsystem.setModuleStates(moduleStates);
    
    //Smart dash varibs. The string ones proably work
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
    return isFinished;
  }
}