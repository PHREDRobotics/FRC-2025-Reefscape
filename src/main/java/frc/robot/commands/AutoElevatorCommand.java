package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

/**
 * A command for moving the arm
 */
public class AutoElevatorCommand extends Command {
    private final ElevatorSubsystem m_ElevatorSubsystem;
    private final Double m_elevator_pos;

    /**
     * Operate the arm subsystem
     * 
     * @param armPos Default positions can be found in the Constants
     * @param arm
     */
    public AutoElevatorCommand(Double elevatorPos, ElevatorSubsystem elevator) {
        m_elevator_pos = elevatorPos;
        m_ElevatorSubsystem = elevator;
        addRequirements(m_ElevatorSubsystem);
    }

    @Override
    public void execute() {
        m_ElevatorSubsystem.moveToPosition(m_elevator_pos);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

 
}