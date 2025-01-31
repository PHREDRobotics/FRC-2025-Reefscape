// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.commands.DriveCommand;
import frc.robot.controls.LogitechPro;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.commands.PathPlannerAuto;

public class RobotContainer {
    SwerveSubsystem m_swerveSubsystem;

    LogitechPro m_driverJoystick;
    CommandXboxController m_xboxController;

    public enum AutoSwitcher { // enum to switch between different auto modes
        OFF_THE_LINE
    }

    public RobotContainer() {
        m_swerveSubsystem = new SwerveSubsystem();

        m_driverJoystick = new LogitechPro(0);
        m_xboxController = new CommandXboxController(1);

        configureBindings();
    }

    private void configureBindings() {
        // Triggers
        Trigger fieldOrientedTrigger = new Trigger(() -> m_driverJoystick.getTrigger());

        // Axes
        DoubleSupplier driveAxis = () -> m_driverJoystick.getPitch();
        DoubleSupplier strafeAxis = () -> m_driverJoystick.getRoll();
        DoubleSupplier turnAxis = () -> m_driverJoystick.getYaw();
        DoubleSupplier throttleAxis = () -> m_driverJoystick.getCorrectedThrottle();

        m_swerveSubsystem.setDefaultCommand(new DriveCommand(
                m_swerveSubsystem,
                driveAxis,
                strafeAxis,
                turnAxis,
                throttleAxis,
                () -> !fieldOrientedTrigger.getAsBoolean())); // will be robot-centric if held down
    }

    public Command getAutonomousCommand(AutoSwitcher autoMode) {
        switch (autoMode) {
            default:
            case OFF_THE_LINE: {
                return new PathPlannerAuto("Basic off-the-line auto");
            }
        }
    }
}