package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ResetElevator extends Command {
    ElevatorSubsystem m_elevatorSubsystem;

    public ResetElevator(ElevatorSubsystem elevatorSubsystem) {
        m_elevatorSubsystem = elevatorSubsystem;
    }

    @Override
    public void initialize() {
        m_elevatorSubsystem.setSpeed(-0.01);
    }

    @Override
    public void execute() {
        // nothing for now, this will run periodically..
    }

    @Override
    public void end(boolean interrupted) {
        m_elevatorSubsystem.setSpeed(0);
        m_elevatorSubsystem.resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return m_elevatorSubsystem.isLimitSwitchPressed();
    }
}