package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ResetElevator extends Command {
    ElevatorSubsystem elevator_subsystem;

    @Override
    public void initialize() {
        elevator_subsystem.setRawPower(-0.3);
    }

    @Override
    public void execute() {
        // nothing for now, this will run periodically..
    }

    @Override
    public void end(boolean interrupted) {
        elevator_subsystem.resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return elevator_subsystem.isLimitSwitchPressed();
    }
}
