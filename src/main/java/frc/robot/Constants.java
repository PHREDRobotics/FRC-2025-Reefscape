package frc.robot;

import static edu.wpi.first.units.Units.Volts;

import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Voltage;

public class Constants {
    public static final class PhysicalConstants {
        public static final double kRobotMassPounds = 120;

        public static final double kBumperLength = 32;
        public static final double kTrackLength = 24;

        public static final double kMaxSpeed = 2 * Math.floor(Math.PI); // 6
        public static final double kMaxAngularSpeed = 6;
    }

    public static final class SwerveConstants {
        public static final double kWheelRadius = 0.0508;
        public static final int kEncoderResolution = 4096;

        public static final double kDtSeconds = 0.02;

        public static final int kBackLeftDriveMotorCANId = 26;
        public static final int kFrontLeftDriveMotorCANId = 21;
        public static final int kFrontRightDriveMotorCANId = 11;
        public static final int kBackRightDriveMotorCANId = 16;

        public static final int kBackLeftTurnMotorCANId = 27;
        public static final int kFrontLeftTurnMotorCANId = 22;
        public static final int kFrontRightTurnMotorCANId = 12;
        public static final int kBackRightTurnMotorCANId = 17;

        public static final double kDrivingMotorReduction = 8;
        public static final double kTurningMotorReduction = 21;

        public static final Translation2d kFrontLeftLocationInches = new Translation2d(12.25, 12.25);
        public static final Translation2d kFrontRightLocationInches = new Translation2d(12.25, -12.25);
        public static final Translation2d kBackLeftLocationInches = new Translation2d(-12.25, 12.25);
        public static final Translation2d kBackRightLocationInches = new Translation2d(-12.25, -12.25);

        public static final Translation2d kFrontLeftLocationMeters = new Translation2d(
                Units.inchesToMeters(Constants.SwerveConstants.kFrontLeftLocationInches.getX()),
                Units.inchesToMeters(Constants.SwerveConstants.kFrontLeftLocationInches.getY()));
        public static final Translation2d kFrontRightLocationMeters = new Translation2d(
                Units.inchesToMeters(Constants.SwerveConstants.kFrontRightLocationInches.getX()),
                Units.inchesToMeters(Constants.SwerveConstants.kFrontRightLocationInches.getY()));
        public static final Translation2d kBackLeftLocationMeters = new Translation2d(
                Units.inchesToMeters(Constants.SwerveConstants.kBackLeftLocationInches.getX()),
                Units.inchesToMeters(Constants.SwerveConstants.kBackLeftLocationInches.getY()));
        public static final Translation2d kBackRightLocationMeters = new Translation2d(
                Units.inchesToMeters(Constants.SwerveConstants.kBackRightLocationInches.getX()),
                Units.inchesToMeters(Constants.SwerveConstants.kBackRightLocationInches.getY()));

        public static final SwerveDriveKinematics kKinematics = new SwerveDriveKinematics(
                Constants.SwerveConstants.kFrontLeftLocationMeters, Constants.SwerveConstants.kFrontRightLocationMeters,
                Constants.SwerveConstants.kBackLeftLocationMeters, Constants.SwerveConstants.kBackRightLocationMeters);
    }

    public static final class SimConstants {
        public static final Voltage kDrivingFrictionVolts = Volts.of(0.2);
        public static final Voltage kTurningFrictionVolts = Volts.of(0.2);
    }

    public static final class GyroConstants {
        public static final NavXComType kComType = NavXComType.kMXP_SPI;
    }

    public static final class ControllerConstants {
        public static final double kFlightStickXDeadband = 0.25;
        public static final double kFlightStickYDeadband = 0.1;
        public static final double kFlightStickZDeadband = 0.15;

        public static final double kXboxDeadband = 0.15;

        public static final double kThrottleMultiplier = 1;
    }

    public static final class CoralConstants {
        public static final int kCoralCANId = 46;

        public static final double kCoralIntakeSpeed = 0.08;
        public static final double kCoralIntakeTime = 1.0;
        public static final double kCoralOuttakeSpeed = -0.5;
        public static final double kCoralL1OuttakeSpeed = -0.25;
        public static final double kCoralL4OuttakeSpeed = -0.25;
        public static final double kCoralOuttakeTime = 1.0;
    }

    public static final class AlgaeConstants {
        public static final int kLeftAlgaeCANId = 42;
        public static final int kRightAlgaeCANId = 41;

        public static final double kAlgaeSpeed = 0.75;
        public static final double kAlgaeTime = 2;
    }

    public static final class PneumaticsConstants {
        public static final int kPneumaticsCANId = 40;

        public static final int kSolenoidInput = 14;
        public static final int kSolenoidOutput = 13;
    }

    public static final class ElevatorConstants {
        public static final double kP = 0.6;
        public static final double kI = 0.0;
        public static final double kD = 0.0;

        public static final double kVelocityFF = 0.2;

        public static final int kElevatorCANId = 32;

        public static final double kCoralLevel1 = 0;
        public static final double kCoralLevel2 = 3.23;
        public static final double kCoralLevel3 = 13.4;
        public static final double kCoralLevel4 = 30.7;
        public static final double kHumanPlayerStationLevel = 5.14;

        // Physical constants
        public static final double kEncoderTicksPerRotation = 42;
        public static final double kElevatorGearRatio = 4;
        public static final double kChainDistancePerRevolution = 4;
        public static final double kElevatorDistancePerChainDistance = 2 / 3;

        // The final calculation of encoder ticks to centimeters
        public static final double kEncoderTicksToCentimeters = kEncoderTicksPerRotation
                * kElevatorGearRatio
                / kChainDistancePerRevolution
                / kElevatorDistancePerChainDistance // Convert from inches to centimeters
                / 2.54;

        // Need to double check this value
        public static final double kVoltageMultiplier = 1.5;

        public static final double kMaxMotorSpeed = 750;
        public static final double kMaxAcceleration = 1000;
    }

    public final static class NeoMotorConstants {
        static final double kFreeSpeedRpm = 5676;
    }

    public static final class VisionConstants {
        public static final double kLimelightMountAngleDegrees = 0.0;
        public static final double kLimelightLensHeightInches = 0.0;
        public static final double kAreaToCentimeters = 150;
        public static final double kMetersFromAprilTag = 2;

        public static final double k2pi = Math.PI * 2;

        public static final Pose2d kAprilTag1 = new Pose2d(657.37 * 0.0254, 25.80 * 0.0254,
                new Rotation2d(126 * k2pi / 360));
        public static final Pose2d kAprilTag2 = new Pose2d(657.37 * 0.0254, 291.20 * 0.0254,
                new Rotation2d(234 * k2pi / 360));
        public static final Pose2d kAprilTag3 = new Pose2d(455.15 * 0.0254, 317.15 * 0.0254,
                new Rotation2d(270 * k2pi / 360));
        public static final Pose2d kAprilTag4 = new Pose2d(365.20 * 0.0254, 241.64 * 0.0254,
                new Rotation2d(0 * k2pi / 360));
        public static final Pose2d kAprilTag5 = new Pose2d(365.20 * 0.0254, 75.39 * 0.0254,
                new Rotation2d(0 * k2pi / 360));
        public static final Pose2d kAprilTag6 = new Pose2d(530.49 * 0.0254, 130.17 * 0.0254,
                new Rotation2d(300 * k2pi / 360));
        public static final Pose2d kAprilTag7 = new Pose2d(546.87 * 0.0254, 158.50 * 0.0254,
                new Rotation2d(0 * k2pi / 360));
        public static final Pose2d kAprilTag8 = new Pose2d(530.49 * 0.0254, 186.83 * 0.0254,
                new Rotation2d(60 * k2pi / 360));
        public static final Pose2d kAprilTag9 = new Pose2d(497.77 * 0.0254, 186.83 * 0.0254,
                new Rotation2d(120 * k2pi / 360));
        public static final Pose2d kAprilTag10 = new Pose2d(481.39 * 0.0254, 158.50 * 0.0254,
                new Rotation2d(180 * k2pi / 360));
        public static final Pose2d kAprilTag11 = new Pose2d(497.77 * 0.0254, 130.17 * 0.0254,
                new Rotation2d(240 * k2pi / 360));

        // THE ONLY ACTUALLY IMPLEMENTED APRILTAG OF CURRENT DATE (2/8/2025, 10:32AM)
        public static final Pose2d kAprilTag12 = new Pose2d(33.51 * 0.0254, 25.80 * 0.0254,
                new Rotation2d(54 * k2pi / 360));
        public static final Pose2d kAprilTag13 = new Pose2d(33.51 * 0.0254, 291.20 * 0.0254,
                new Rotation2d(306 * k2pi / 360));
        public static final Pose2d kAprilTag14 = new Pose2d(325.68 * 0.0254, 241.64 * 0.0254,
                new Rotation2d(180 * k2pi / 360));
        public static final Pose2d kAprilTag15 = new Pose2d(325.68 * 0.0254, 75.39 * 0.0254,
                new Rotation2d(180 * k2pi / 360));
        public static final Pose2d kAprilTag16 = new Pose2d(235.73 * 0.0254, -0.15 * 0.0254,
                new Rotation2d(90 * k2pi / 360));
        public static final Pose2d kAprilTag17 = new Pose2d(160.39 * 0.0254, 130.17 * 0.0254,
                new Rotation2d(240 * k2pi / 360));
        public static final Pose2d kAprilTag18 = new Pose2d(144.00 * 0.0254, 158.50 * 0.0254,
                new Rotation2d(180 * k2pi / 360));
        public static final Pose2d kAprilTag19 = new Pose2d(160.39 * 0.0254, 186.83 * 0.0254,
                new Rotation2d(120 * k2pi / 360));
        public static final Pose2d kAprilTag20 = new Pose2d(193.10 * 0.0254, 186.83 * 0.0254,
                new Rotation2d(60 * k2pi / 360));
        public static final Pose2d kAprilTag21 = new Pose2d(209.49 * 0.0254, 158.50 * 0.0254,
                new Rotation2d(0 * k2pi / 360));
        public static final Pose2d kAprilTag22 = new Pose2d(193.10 * 0.0254, 130.17 * 0.0254,
                new Rotation2d(300 * k2pi / 360));
    }
}