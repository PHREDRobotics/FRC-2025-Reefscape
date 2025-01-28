package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.studica.frc.AHRS;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SwerveSubsystem extends SubsystemBase {
    private final Translation2d m_frontLeftLocationMeters = new Translation2d(
            Units.inchesToMeters(Constants.PhysicalConstants.kFrontLeftLocationInches.getX()),
            Units.inchesToMeters(Constants.PhysicalConstants.kFrontLeftLocationInches.getY()));
    private final Translation2d m_frontRightLocationMeters = new Translation2d(
            Units.inchesToMeters(Constants.PhysicalConstants.kFrontRightLocationInches.getX()),
            Units.inchesToMeters(Constants.PhysicalConstants.kFrontRightLocationInches.getY()));
    private final Translation2d m_backLeftLocationMeters = new Translation2d(
            Units.inchesToMeters(Constants.PhysicalConstants.kBackLeftLocationInches.getX()),
            Units.inchesToMeters(Constants.PhysicalConstants.kBackLeftLocationInches.getY()));
    private final Translation2d m_backRightLocationMeters = new Translation2d(
            Units.inchesToMeters(Constants.PhysicalConstants.kBackRightLocationInches.getX()),
            Units.inchesToMeters(Constants.PhysicalConstants.kBackRightLocationInches.getY()));

    private final SwerveModule m_frontLeft;
    private final SwerveModule m_frontRight;
    private final SwerveModule m_backLeft;
    private final SwerveModule m_backRight;

    private final AHRS m_gyro = new AHRS(Constants.GyroConstants.kComType);

    private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
            m_frontLeftLocationMeters, m_frontRightLocationMeters, m_backLeftLocationMeters, m_backRightLocationMeters);

    private final SwerveDriveOdometry m_odometry;

    private final SwerveDrivePoseEstimator m_poseEstimator = new SwerveDrivePoseEstimator(
            m_kinematics,
            m_gyro.getRotation2d(),
            getModulePositions(),
            new Pose2d(),
            VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
            VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));

    RobotConfig config;

    private final Field2d m_poseEstimatorField;

    public SwerveSubsystem() {
        if (RobotBase.isReal()) {
            m_frontLeft = new SparkSwerveModule(Constants.SwerveConstants.kFrontLeftDriveMotorCANId,
                    Constants.SwerveConstants.kFrontLeftTurnMotorCANId);
            m_frontRight = new SparkSwerveModule(Constants.SwerveConstants.kFrontRightDriveMotorCANId,
                    Constants.SwerveConstants.kFrontRightTurnMotorCANId);
            m_backLeft = new SparkSwerveModule(Constants.SwerveConstants.kBackLeftDriveMotorCANId,
                    Constants.SwerveConstants.kBackLeftTurnMotorCANId);
            m_backRight = new SparkSwerveModule(Constants.SwerveConstants.kBackRightDriveMotorCANId,
                    Constants.SwerveConstants.kBackRightTurnMotorCANId);
        } else {
            m_frontLeft = new SparkSwerveModuleSim(Constants.SwerveConstants.kFrontLeftDriveMotorCANId,
                    Constants.SwerveConstants.kFrontLeftTurnMotorCANId);
            m_frontRight = new SparkSwerveModuleSim(Constants.SwerveConstants.kFrontRightDriveMotorCANId,
                    Constants.SwerveConstants.kFrontRightTurnMotorCANId);
            m_backLeft = new SparkSwerveModuleSim(Constants.SwerveConstants.kBackLeftDriveMotorCANId,
                    Constants.SwerveConstants.kBackLeftTurnMotorCANId);
            m_backRight = new SparkSwerveModuleSim(Constants.SwerveConstants.kBackRightDriveMotorCANId,
                    Constants.SwerveConstants.kBackRightTurnMotorCANId);

            // public final SwerveSimulation m_swerveSimulation = new SwerveSimulation();
        }

        m_gyro.reset();

        m_odometry = new SwerveDriveOdometry(
                m_kinematics,
                m_gyro.getRotation2d(),
                getModulePositions());

        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AutoBuilder.configure(
                () -> getPose(),
                (pose) -> resetOdometry(pose),
                () -> getSpeeds(false),
                (speeds, feedforwards) -> drive(
                        speeds,
                        () -> false),
                new PPHolonomicDriveController(
                        new PIDConstants(0.6, 0.2, 0),
                        new PIDConstants(1.0, 0, 0)),
                config,
                () -> {
                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Blue;
                    }
                    return false;
                },
                this);

        m_poseEstimatorField = new Field2d();
        SmartDashboard.putData("Pose estimation field", m_poseEstimatorField);
    }

    public void drive(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rot, BooleanSupplier fieldOriented) {
        var swerveModuleStates = m_kinematics.toSwerveModuleStates(
                ChassisSpeeds.discretize(
                        fieldOriented.getAsBoolean()
                                ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                        xSpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                        ySpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                        rot.getAsDouble() * Constants.PhysicalConstants.kMaxAngularSpeed,
                                        m_gyro.getRotation2d())
                                : new ChassisSpeeds(
                                        xSpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                        ySpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                        rot.getAsDouble() * Constants.PhysicalConstants.kMaxAngularSpeed),
                        Constants.SwerveConstants.kDtSeconds));

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.PhysicalConstants.kMaxSpeed);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_backLeft.setDesiredState(swerveModuleStates[2]);
        m_backRight.setDesiredState(swerveModuleStates[3]);

        if (RobotBase.isSimulation()) {
            // m_swerveSimulation.update(getSpeeds(false), fieldOriented.getAsBoolean());
        }
    }

    public void drive(ChassisSpeeds speeds, BooleanSupplier fieldOriented) {
        var swerveModuleStates = m_kinematics.toSwerveModuleStates(
                ChassisSpeeds.discretize(
                        fieldOriented.getAsBoolean()
                                ? ChassisSpeeds.fromFieldRelativeSpeeds(speeds, m_gyro.getRotation2d())
                                : speeds,
                        Constants.SwerveConstants.kDtSeconds));

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.PhysicalConstants.kMaxSpeed);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_backLeft.setDesiredState(swerveModuleStates[2]);
        m_backRight.setDesiredState(swerveModuleStates[3]);

        if (RobotBase.isSimulation()) {
            // m_swerveSimulation.update(getSpeeds(false), fieldOriented.getAsBoolean());
        }
    }

    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        m_odometry.resetPose(pose);
    }

    public void updateOdometry() {
        m_odometry.update(
                m_gyro.getRotation2d(),
                getModulePositions());

        m_poseEstimator.update(
                m_gyro.getRotation2d(),
                getModulePositions());

        boolean doRejectUpdate = false;

        LimelightHelpers.SetRobotOrientation("limelight",
                m_poseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
        if (Math.abs(m_gyro.getRate()) > 720) { // if our angular velocity is greater than 720 degrees per second,
                                                // ignore vision updates 👍
            doRejectUpdate = true;
        }
        if (mt2.tagCount == 0) {
            doRejectUpdate = true;
        }
        if (!doRejectUpdate) {
            m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, 9999999));
            m_poseEstimator.addVisionMeasurement(
                    mt2.pose,
                    mt2.timestampSeconds);
        }
    }

    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
                m_frontLeft.getPosition(),
                m_frontRight.getPosition(),
                m_backLeft.getPosition(),
                m_backRight.getPosition()
        };
    }

    public ChassisSpeeds getSpeeds(boolean fieldRelative) {
        ChassisSpeeds robotRelativeSpeeds = m_kinematics.toChassisSpeeds(new SwerveModuleState[] {
                m_frontLeft.getState(),
                m_frontRight.getState(),
                m_backLeft.getState(),
                m_backRight.getState() });

        if (fieldRelative) {
            return robotRelativeSpeeds;
        }

        return ChassisSpeeds.fromRobotRelativeSpeeds(
                robotRelativeSpeeds, getPose().getRotation());
    }

    public void periodic() {
        updateOdometry();

        m_poseEstimatorField.setRobotPose(m_poseEstimator.getEstimatedPosition());

        SmartDashboard.putNumber("F.L. Drive motor temp", m_frontLeft.getDriveTemp());

        SmartDashboard.putNumber("Swerve/FrontLeft/Speed", m_frontLeft.getState().speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve/FrontLeft/Angle", m_frontLeft.getPosition().angle.getDegrees());
        SmartDashboard.putNumber("Swerve/FrontRight/Speed", m_frontRight.getState().speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve/FrontRight/Angle", m_frontRight.getPosition().angle.getDegrees());
        SmartDashboard.putNumber("Swerve/BackLeft/Speed", m_backLeft.getState().speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve/BackLeft/Angle", m_backLeft.getPosition().angle.getDegrees());
        SmartDashboard.putNumber("Swerve/BackRight/Speed", m_backRight.getState().speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve/BackRight/Angle", m_backRight.getPosition().angle.getDegrees());

        SmartDashboard.putString("Speeds", getSpeeds(true).toString());

        SmartDashboard.updateValues();
    }

    @Override
    public void simulationPeriodic() {
        updateOdometry();
    }
}