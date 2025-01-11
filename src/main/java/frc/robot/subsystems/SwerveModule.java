package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import java.io.ObjectInputFilter.Config;

import com.revrobotics.AbsoluteEncoder;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.Constants.ModuleConstants;

/**
 * Constructs a MAXSwerveModule and configures the driving and turning motor,
 * encoder, and PID controller. This configuration is specific to the REV
 * MAXSwerve Module built with NEOs, SPARKS MAX, and a Through Bore
 * Encoder.
 */
public class SwerveModule {

  private final SparkMax driveSparkMax;
  private final SparkMax turningSparkMax;

  private final RelativeEncoder driveEncoder;
  private final AbsoluteEncoder turningEncoder;

  private double chassisAngularOffset = 0;
  private SwerveModuleState moduleDesiredState = new SwerveModuleState(0.0, new Rotation2d());

  public SwerveModule(
      int driveMotorId,
      int turningMotorId,
      boolean driveMotorReversed,
      boolean turningMotorReversed,
      double moduleOffset,
      boolean absoluteEncoderReversed,
      boolean driveMotorInverted) {

    SparkMaxConfig driveConfig = new SparkMaxConfig();
    driveSparkMax = new SparkMax(driveMotorId, MotorType.kBrushless);
    driveEncoder = driveSparkMax.getEncoder();
    driveConfig
        .inverted(driveMotorReversed)
        .smartCurrentLimit(50)
        .idleMode(IdleMode.kBrake);
    driveConfig.encoder
        .positionConversionFactor(moduleOffset)
        .velocityConversionFactor(1000);
    driveConfig.closedLoop
        .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
        .pid(ModuleConstants.kDrivingP, ModuleConstants.kDrivingI, ModuleConstants.kDrivingD);

    driveSparkMax.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    SparkMaxConfig turnConfig = new SparkMaxConfig();
    turningSparkMax = new SparkMax(turningMotorId, MotorType.kBrushless);
    turningEncoder = turningSparkMax.getAbsoluteEncoder();
    turnConfig
        .inverted(driveMotorReversed)
        .idleMode(IdleMode.kBrake);
    turnConfig.absoluteEncoder
        .positionConversionFactor(moduleOffset)
        .velocityConversionFactor(1000);
    turnConfig.closedLoop
        .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
        .pid(ModuleConstants.kTurningP, ModuleConstants.kTurningI, ModuleConstants.kTurningD);

    turningSparkMax.configure(turnConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    chassisAngularOffset = moduleOffset;
    moduleDesiredState.angle = new Rotation2d(turningEncoder.getPosition());
    driveSparkMax.getEncoder().setPosition(0);

  }

  public double getDrivePosition() {
    return -driveEncoder.getPosition();
  }

  /**
   * Returns current turn position in range -pi to pi
   */

  // Before it was
  // return turningSparkMax.getEncoder().getPosition();

  public double getTurningPosition() {
    if (turningEncoder.getPosition() >= 0) {
      return turningEncoder.getPosition();

    } else {
      return Constants.k2pi + turningEncoder.getPosition();
    }
    // ModuleConstants.kTurningMotorRotationPerSteerRotation;
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getDrivePosition(),
        new Rotation2d(turningEncoder.getPosition() - chassisAngularOffset));
  }

  public double getDriveVelocity() {
    return driveEncoder.getVelocity();
  }

  public double getTurningVelocity() {
    return turningEncoder.getVelocity();
  }

  public void resetEncoders() {
    driveEncoder.setPosition(0);
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(getDriveVelocity(),
        new Rotation2d(turningSparkMax.getAbsoluteEncoder().getPosition() - chassisAngularOffset));
  }

  public void setDesiredState(SwerveModuleState desiredState) {
    SwerveModuleState correctedDesiredState = new SwerveModuleState();
    correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
    correctedDesiredState.angle = desiredState.angle.plus(Rotation2d.fromRadians(chassisAngularOffset));
    // changed fromRadians to fromDegrees

    SwerveModuleState optimizedDesiredState = SwerveModuleState.optimize(correctedDesiredState,
        new Rotation2d(turningEncoder.getPosition()));

    driveSparkMax.getClosedLoopController().setReference(optimizedDesiredState.speedMetersPerSecond,
        SparkMax.ControlType.kVelocity);
    turningSparkMax.getClosedLoopController().setReference(optimizedDesiredState.angle.getRadians(),
        SparkMax.ControlType.kPosition);
    // changed getRadians to getDegrees

    // MIGHT OF DONE A THING UP THERE IM NOT SURE HAVENT BEEN ABLE TO TEST
    // YET!!!!!!!!
  }

  public void stop() {
    driveSparkMax.set(0);
    turningSparkMax.set(0);
  }
}