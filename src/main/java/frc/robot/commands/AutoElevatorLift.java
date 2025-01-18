package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ElevatorSubsystem;


public class AutoElevatorLift extends Command{

    private final ElevatorSubsystem m_elevator;
    private final Double m_elevator_pos;

    /**
     * Operate the arm subsystem
     * @param armPos Default positions can be found in the Constants
     * @param arm 
     */
    public AutoElevatorLift(Double elevatorPos, ElevatorSubsystem elevator) {
        m_elevator_pos = elevatorPos;
        m_elevator = elevator;
        addRequirements(m_elevator);
    }

    @Override
    public void execute() {
        m_elevator.moveToPosition(m_elevator_pos);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted == false) {
            m_elevator.moveToPosition(Constants.ElevatorConstants.kLevel1);
        }
    }
}
