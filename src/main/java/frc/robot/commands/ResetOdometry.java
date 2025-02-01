package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class ResetOdometry extends Command {
    SwerveSubsystem m_swerveSubsystem;
    Pose2d pose;

    public ResetOdometry(Pose2d pose, SwerveSubsystem swerveSubsystem) {
        this.m_swerveSubsystem = swerveSubsystem;

        this.pose = pose;
    }

    @Override
    public void initialize() {
        this.m_swerveSubsystem.resetOdometry(this.pose);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
