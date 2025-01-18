package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorManualLift extends Command{
  DoubleSupplier power;
    ElevatorSubsystem elevator_subsystem;

    /**
     * 
     * @param leftLiftPower power for the left lift motor
     * @param rightLiftPower power for the right lift motor
     * @param liftSubsystem
     */
    public ElevatorManualLift(DoubleSupplier power, ElevatorSubsystem elevatorSubsystem) {
        this.power = power;
        this.elevator_subsystem = elevatorSubsystem;

        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize() {
        elevator_subsystem.resetEncoders();
    }

    @Override
    public void execute() {
        this.elevator_subsystem.moveElevator(this.power);
    }

  
}
