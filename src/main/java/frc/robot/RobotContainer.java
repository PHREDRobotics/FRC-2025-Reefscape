// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import frc.robot.commands.AlgaeIntakeCommand;
import frc.robot.commands.AlgaeOuttakeCommand;
import frc.robot.commands.CoralIntakeCommand;
import frc.robot.commands.CoralOuttakeCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.ElevatorManualLift;
import frc.robot.commands.ResetElevator;
import frc.robot.commands.AutoElevatorCommand;
import frc.robot.commands.ExtendLift;
import frc.robot.commands.GoToTag;
import frc.robot.commands.RetractLift;
import frc.robot.commands.SwerveReset;
import frc.robot.controls.LogitechPro;

import frc.robot.subsystems.AlgaeSubsystem;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class RobotContainer {
    SwerveSubsystem m_swerveSubsystem;
    AlgaeSubsystem m_algaeSubsystem;
    // ClimbSubsystem m_climbSubsystem;
    CoralSubsystem m_coralSubsystem;
    ElevatorSubsystem m_elevatorSubsystem;
    VisionSubsystem m_visionSubsystem;

    LogitechPro m_driverJoystick;
    CommandXboxController m_xboxController;

    public enum AutoSwitcher { // enum to switch between different auto modes
        NOTHING,
        OFF_THE_LINE,
        BottomCoralLeft,
        BottomCoralCenter,
        BottomCoralRight
    }

    public RobotContainer() {
        m_swerveSubsystem = new SwerveSubsystem();
        m_algaeSubsystem = new AlgaeSubsystem();
        m_coralSubsystem = new CoralSubsystem();
        // m_climbSubsystem = new ClimbSubsystem();
        m_elevatorSubsystem = new ElevatorSubsystem();
        m_visionSubsystem = new VisionSubsystem();

        m_driverJoystick = new LogitechPro(0);
        m_xboxController = new CommandXboxController(1);

        NamedCommands.registerCommand("ElevatorL1Command",
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel1, m_elevatorSubsystem));
        NamedCommands.registerCommand("ElevatorL2Command",
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel2, m_elevatorSubsystem));
        NamedCommands.registerCommand("ElevatorL3Command",
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel3, m_elevatorSubsystem));
        NamedCommands.registerCommand("ElevatorL4Command",
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel4, m_elevatorSubsystem));

                NamedCommands.registerCommand("CoralL1OuttakeCommand", new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem,1));
                NamedCommands.registerCommand("CoralL2L3OuttakeCommand", new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem,2));
                NamedCommands.registerCommand("CoralL4OuttakeCommand", new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem,4));

        configureBindings();
    }

    private void configureBindings() {
        // Triggers
        Trigger fieldOrientedTrigger = new Trigger(() -> m_driverJoystick.getTrigger());
        Trigger resetTrigger = new Trigger(() -> m_driverJoystick.getRawButton(2));
        //Trigger tagTrigger = new Trigger(() -> m_driverJoystick.getRawButton(3));

        Trigger bButton = m_xboxController.b();
        Trigger aButton = m_xboxController.a();
        Trigger yButton = m_xboxController.y();
        Trigger xButton = m_xboxController.x();
        Trigger startButton = m_xboxController.start();
        Trigger backButton = m_xboxController.back();
        Trigger leftBumper = m_xboxController.leftBumper();
        Trigger rightBumper = m_xboxController.rightBumper();

        // Axes
        DoubleSupplier driveAxis = () -> m_driverJoystick.getPitch();
        DoubleSupplier strafeAxis = () -> m_driverJoystick.getRoll();
        DoubleSupplier turnAxis = () -> m_driverJoystick.getYaw();
        DoubleSupplier throttleAxis = () -> m_driverJoystick.getCorrectedThrottle();
        DoubleSupplier triggerLeftAxis = () -> m_xboxController.getLeftTriggerAxis();
        DoubleSupplier triggerRightAxis = () -> m_xboxController.getRightTriggerAxis();

        // D-Pad Buttons
        Trigger dPadDown = m_xboxController.povDown();
        Trigger dPadUp = m_xboxController.povUp();
        Trigger dPadLeft = m_xboxController.povLeft();
        Trigger dPadRight = m_xboxController.povRight();

        // Assign Commands

        // Xbox
        xButton.whileTrue(new AlgaeIntakeCommand(m_algaeSubsystem));
        yButton.onTrue(new AlgaeOuttakeCommand(m_algaeSubsystem));
        aButton.whileTrue(new CoralIntakeCommand(m_coralSubsystem));
        bButton.onTrue(new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem, 2)); // L2 & L3
        rightBumper.onTrue(new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem, 2));
        // startButton.onTrue(new ExtendLift(m_climbSubsystem));
        // backButton.onTrue(new RetractLift(m_climbSubsystem));
        leftBumper.onTrue(new CoralOuttakeCommand(m_coralSubsystem, m_elevatorSubsystem, 1)); // L1
        dPadDown.onTrue(new SequentialCommandGroup(new ResetElevator(m_elevatorSubsystem),
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel1, m_elevatorSubsystem)));
        dPadLeft.onTrue(new SequentialCommandGroup(new ResetElevator(m_elevatorSubsystem),
                new AutoElevatorCommand(Constants.ElevatorConstants.kHumanPlayerStationLevel, m_elevatorSubsystem)));
        dPadRight.onTrue(new SequentialCommandGroup(new ResetElevator(m_elevatorSubsystem),
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel3, m_elevatorSubsystem)));
        dPadUp.onTrue(new SequentialCommandGroup(new ResetElevator(m_elevatorSubsystem),
                new AutoElevatorCommand(Constants.ElevatorConstants.kCoralLevel4, m_elevatorSubsystem)));

        m_elevatorSubsystem
                .setDefaultCommand(new ElevatorManualLift(() -> m_xboxController.getLeftY(), m_elevatorSubsystem));

        // Joystick
        m_swerveSubsystem.setDefaultCommand(new DriveCommand(
                m_swerveSubsystem,
                driveAxis,
                strafeAxis,
                turnAxis,
                throttleAxis,
                () -> !fieldOrientedTrigger.getAsBoolean())); // will be robot-centric if held down

        resetTrigger.onTrue(new SwerveReset(m_swerveSubsystem));

        //tagTrigger.onTrue(new GoToTag(m_visionSubsystem, m_swerveSubsystem, 9));
    }

    public Command getAutonomousCommand(AutoSwitcher autoMode) {
        switch (autoMode) {
            default:
            case NOTHING: {
                return null;
            }
            case OFF_THE_LINE: {
                return new PathPlannerAuto("Basic off-the-line auto");
            }
            case BottomCoralLeft: {
                return new PathPlannerAuto("BottomCoralAutoLeft");
            }
            case BottomCoralCenter: {
                return new PathPlannerAuto("BottomCoralAutoCenter");
            }
            case BottomCoralRight: {
                return new PathPlannerAuto("BottomCoralAutoRight");
            }
        }
    }
}
