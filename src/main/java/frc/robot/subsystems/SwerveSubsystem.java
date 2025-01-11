package frc.robot.subsystems;

//import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.controls.LogitechPro;

public class SwerveSubsystem extends SubsystemBase {
  private final SwerveModule frontLeft = new SwerveModule(
      DriveConstants.kFrontLeftDriveMotorPort,
      DriveConstants.kFrontLeftTurningMotorPort,
      DriveConstants.kFrontLeftDriveEncoderReversed,
      DriveConstants.kFrontLeftTurningEncoderReversed,
      DriveConstants.kFrontLeftModuleChassisAngularOffset,
      DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed,
      DriveConstants.kFrontLeftDriveInverted);

  private final SwerveModule frontRight = new SwerveModule(
      DriveConstants.kFrontRightDriveMotorPort,
      DriveConstants.kFrontRightTurningMotorPort,
      DriveConstants.kFrontRightDriveEncoderReversed,
      DriveConstants.kFrontRightTurningEncoderReversed,
      DriveConstants.kFrontRightModuleChassisAngularOffset,
      DriveConstants.kFrontRightDriveAbsoluteEncoderReversed,
      DriveConstants.kFrontRightDriveInverted);

  public final SwerveModule backLeft = new SwerveModule(
      DriveConstants.kBackLeftDriveMotorPort,
      DriveConstants.kBackLeftTurningMotorPort,
      DriveConstants.kBackLeftDriveEncoderReversed,
      DriveConstants.kBackLeftTurningEncoderReversed,
      DriveConstants.kBackLeftModuleChassisAngularOffset,
      DriveConstants.kBackLeftDriveAbsoluteEncoderReversed,
      DriveConstants.kBackLeftDriveInverted);

  private final SwerveModule backRight = new SwerveModule(
      DriveConstants.kBackRightDriveMotorPort,
      DriveConstants.kBackRightTurningMotorPort,
      DriveConstants.kBackRightDriveEncoderReversed,
      DriveConstants.kBackRightTurningEncoderReversed,
      DriveConstants.kBackRightModuleChassisAngularOffset,
      DriveConstants.kBackRightDriveAbsoluteEncoderReversed,
      DriveConstants.kBackRightDriveInverted);

  private final AHRS gyro = new AHRS(AHRS.NavXComType.kMXP_SPI);
  // private final SimDeviceSim simGyro = new SimDeviceSim();
  private final Field2d m_field = new Field2d();

  private final LogitechPro m_joyStick;

  // private final SwerveDriveOdometry odometer = new
  // SwerveDriveOdometry(DriveConstants.kDriveKinematics,
  // new Rotation2d(), getModulePositions());

  SwerveDriveOdometry odometer = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics, gyro.getRotation2d(),
      getModulePositions(), new Pose2d(0.0, 0.0, new Rotation2d()));

  public SwerveModulePosition[] getModulePositions() {
    return new SwerveModulePosition[] {
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
    };
  }

  public SwerveSubsystem(LogitechPro joystick) {
    m_joyStick = joystick;
    SmartDashboard.putData("Field", m_field);
    new Thread(() -> {
      try {
        Thread.sleep(1000);
        zeroHeading();
        initModules();
      } catch (Exception e) {
      }
    }).start();
  }

  public void zeroHeading() {
    // gyro.zeroYaw();
    gyro.reset();
    frontLeft.resetEncoders();
    frontRight.resetEncoders();
    backLeft.resetEncoders();
    backRight.resetEncoders();

  }

  public double getHeading() {
    if (gyro.isMagnetometerCalibrated()) {
      // We will only get valid fused headings if the magnetometer is calibrated
      return gyro.getFusedHeading();
    }

    // We have to invert the angle of the NavX so that rotating the robot
    // counter-clockwise makes the angle increase.
    // if (-gyro.getYaw() >= 0) {
    return gyro.getYaw();

    // } else {
    // return 360 + -gyro.getYaw();
    // }
  }

  public Rotation2d getRotation2d() {
    return Rotation2d.fromDegrees(getHeading());
  }

  public Pose2d getPose() {
    return odometer.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    odometer.resetPosition(getRotation2d(), getModulePositions(), getPose());
  }

  // -------------------------------------------------
  // ------------- Update Dashboard ----------------
  @Override
  public void periodic() {
    odometer.update(getRotation2d(), getModulePositions());

    SmartDashboard.putNumber("throttle", m_joyStick.getThrottl());

    SmartDashboard.putString("Robot Heading (Rotation2d)", gyro.getRotation2d().toString());
    SmartDashboard.putNumber("Robot Heading (Degrees)", getHeading());
    SmartDashboard.putNumber("Front Left Turning Position", frontLeft.getTurningPosition() * (180 / Math.PI));
    SmartDashboard.putNumber("Front Right Turning Position", frontRight.getTurningPosition() * (180 / Math.PI));
    SmartDashboard.putNumber("Back Left Turning Position", backLeft.getTurningPosition() * (180 / Math.PI));
    SmartDashboard.putNumber("Back Right Turning Position", backRight.getTurningPosition() * (180 / Math.PI));

    SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());

    SmartDashboard.putBoolean("Should we blame Hardware/Electrical?", true);
  }

  @Override
  public void simulationPeriodic() {
    odometer.update(new Rotation2d(-Math.PI), getModulePositions());
    m_field.setRobotPose(odometer.getPoseMeters());
  }

  public void initModules() {
    SwerveModuleState state = new SwerveModuleState(0, new Rotation2d());
    frontLeft.setDesiredState(state);
    frontRight.setDesiredState(state);
    backLeft.setDesiredState(state);
    backRight.setDesiredState(state);
  }

  public void stopModules() {
    frontLeft.stop();
    frontRight.stop();
    backLeft.stop();
    backRight.stop();
  }
  // state.angle.getRadians());

  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
    frontLeft.setDesiredState(desiredStates[0]);
    frontRight.setDesiredState(desiredStates[1]);
    backLeft.setDesiredState(desiredStates[2]);
    backRight.setDesiredState(desiredStates[3]);
  }

}