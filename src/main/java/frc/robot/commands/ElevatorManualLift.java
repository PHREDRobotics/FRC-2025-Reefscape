package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorManualLift extends Command {
    DoubleSupplier power;
    ElevatorSubsystem elevator_subsystem;

    /**
     * 
     * @param leftLiftPower  power for the left lift motor
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
    }

    @Override
    public void execute() {
        double speed = power.getAsDouble();
        speed = speed * Math.abs(speed);

        if (Math.abs(speed) < OIConstants.kDeadband) {
            speed = 0;
        }

        this.elevator_subsystem.setRawPower(speed);
    }

}
