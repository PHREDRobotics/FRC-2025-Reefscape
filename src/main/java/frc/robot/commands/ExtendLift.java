// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ClimbSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Command for extending the lift.
 */
public class ExtendLift extends Command {
  private final ClimbSubsystem m_subsystem;

  /**
   * Creates a new ExtendLift command.
   *
   * @param subsystem Climb subsystem
   */
  public ExtendLift(ClimbSubsystem subsystem) {
    m_subsystem = subsystem;
    
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.extendPneumaticClimber();
  }

  
  /** 
   * @return boolean
   */
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}