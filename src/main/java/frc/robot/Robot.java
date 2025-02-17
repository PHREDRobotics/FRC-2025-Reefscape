// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.IOException;
import java.nio.file.Path;

import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.seasonspecific.reefscape2025.ReefscapeAlgaeOnField;
import org.ironmaple.simulation.seasonspecific.reefscape2025.ReefscapeCoralAlgaeStack;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  private final SendableChooser<RobotContainer.AutoSwitcher> autoChooser = new SendableChooser<>();

  String trajectoryJSON = "pathplanner/paths/offtheline.json";
  Trajectory trajectory = new Trajectory();
  StructArrayPublisher<Pose3d> notePoses = NetworkTableInstance.getDefault()
      .getStructArrayTopic("MyPoseArray", Pose3d.struct)
      .publish();

  public Robot() {
    m_robotContainer = new RobotContainer();

    if (!Robot.isReal()) {
      SimulatedArena.getInstance().addGamePiece(new ReefscapeAlgaeOnField(new Translation2d(2, 2)));
      SimulatedArena.getInstance().addGamePiece(new ReefscapeAlgaeOnField(new Translation2d(2, 3)));
      SimulatedArena.getInstance().addGamePiece(new ReefscapeCoralAlgaeStack(new Translation2d(3, 2)));
    }

    try {
      Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
      trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory", ex.getStackTrace());
    }
  }

  @Override
  public void robotInit() {
    autoChooser.setDefaultOption("Empty", RobotContainer.AutoSwitcher.NOTHING);
    autoChooser.addOption("Bottom Coral Left", RobotContainer.AutoSwitcher.BottomCoralLeft);
    autoChooser.addOption("Bottom Coral Center", RobotContainer.AutoSwitcher.BottomCoralCenter);
    autoChooser.addOption("Bottom Coral Right", RobotContainer.AutoSwitcher.BottomCoralRight);
    autoChooser.addOption("Off-the-line", RobotContainer.AutoSwitcher.OFF_THE_LINE);
    SmartDashboard.putData("Auto mode", autoChooser);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void disabledExit() {
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand(autoChooser.getSelected());

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void autonomousExit() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @SuppressWarnings("unused")
  @Override
  public void simulationPeriodic() {
    SimulatedArena.getInstance().simulationPeriodic();

    Pose3d[] notesPoses = SimulatedArena.getInstance()
        .getGamePiecesArrayByType("Algae");
    notePoses.accept(SimulatedArena.getInstance()
        .getGamePiecesArrayByType("Algae"));
  }

  @Override
  public void teleopExit() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void testExit() {
  }
}