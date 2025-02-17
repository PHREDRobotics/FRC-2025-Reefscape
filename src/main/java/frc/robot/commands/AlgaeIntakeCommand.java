package frc.robot.commands;

import frc.robot.subsystems.AlgaeSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

public class AlgaeIntakeCommand extends Command {
    private final AlgaeSubsystem cmdSubsystem;

    /**
     * Creates a new command
     * 
     * @param subsystem The subsystem used by this command
     */
    public AlgaeIntakeCommand(AlgaeSubsystem subsystem) {
        cmdSubsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        cmdSubsystem.Intake();
    }

    @Override
    public void execute() {
        // Periodically runs; nothing for now.
    }

    @Override
    public void end(boolean interrupted) {
        cmdSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        //return cmdSubsystem.isAlgaeLoaded();
        return true;
         /**
          * isFinished() will return isAlgaeLoaded() which checks if the limit switch (or beam break) for the algae is pressed.
          */
    }
} 