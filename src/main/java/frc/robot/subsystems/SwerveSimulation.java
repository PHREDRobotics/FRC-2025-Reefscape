package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.Volts;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.dyn4j.geometry.Rotation;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.GyroSimulation;
import org.ironmaple.simulation.drivesims.SelfControlledSwerveDriveSimulation;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;
import org.ironmaple.simulation.drivesims.configs.SwerveModuleSimulationConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class SwerveSimulation {
    private final SelfControlledSwerveDriveSimulation m_swerveSimulation;
    private final Field2d m_field;

    private final GyroIO m_gyro;

    public SwerveSimulation(GyroIO gyroSim) {
        m_gyro = gyroSim;

        SwerveModuleSimulationConfig moduleConfig = new SwerveModuleSimulationConfig(
                DCMotor.getNEO(1),
                DCMotor.getNEO(1),
                Constants.SwerveConstants.kDrivingMotorReduction,
                Constants.SwerveConstants.kTurningMotorReduction,
                Volts.of(2),
                Volts.of(2),
                Meters.of(Constants.SwerveConstants.kWheelRadius),
                KilogramSquareMeters.of(0.2),
                1.2);

        DriveTrainSimulationConfig driveTrainConfig = new DriveTrainSimulationConfig(
                Pounds.of(Constants.PhysicalConstants.kRobotMassPounds),
                Inches.of(Constants.PhysicalConstants.kBumperLength),
                Inches.of(Constants.PhysicalConstants.kBumperLength),
                Inches.of(Constants.PhysicalConstants.kFrontLeftLocationInches.getX() * 2),
                Inches.of(Constants.PhysicalConstants.kFrontLeftLocationInches.getY() * 2),
                () -> new SwerveModuleSimulation(moduleConfig),
                () -> new GyroSimulation(2, 2));

        m_swerveSimulation = new SelfControlledSwerveDriveSimulation(
                new SwerveDriveSimulation(driveTrainConfig, new Pose2d(0, 0, new Rotation2d())));

        m_field = new Field2d();
        SmartDashboard.putData("Sim Robot", m_field);

        SimulatedArena.getInstance().addDriveTrainSimulation(m_swerveSimulation.getDriveTrainSimulation());
    }

    public void update(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rot, BooleanSupplier fieldOriented, Rotation2d gyroRotation) {
        ChassisSpeeds moduleSpeeds = ChassisSpeeds.discretize(
                fieldOriented.getAsBoolean()
                        ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                xSpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                ySpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                -rot.getAsDouble() * Constants.PhysicalConstants.kMaxAngularSpeed,
                                gyroRotation)
                        : new ChassisSpeeds(
                                xSpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                ySpeed.getAsDouble() * Constants.PhysicalConstants.kMaxSpeed,
                                -rot.getAsDouble() * Constants.PhysicalConstants.kMaxAngularSpeed),
                Constants.SwerveConstants.kDtSeconds);
        m_swerveSimulation.runChassisSpeeds(moduleSpeeds,
                new Translation2d(),
                false, true);
        m_swerveSimulation.periodic();
        m_field.setRobotPose(this.m_swerveSimulation.getActualPoseInSimulationWorld());
    }
}
