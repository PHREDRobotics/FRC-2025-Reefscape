package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SwerveConstants;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveParser;

public class SwerveSubsystem extends SubsystemBase{
    private final SwerveDrive swerveDrive;
    private final Field2d field = new Field2d();

    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");

    Boolean fieldOriented = true;

    public SwerveSubsystem() {
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(SwerveConstants.MAX_SPEED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        swerveDrive.setHeadingCorrection(false);
        swerveDrive.setCosineCompensator(false);

        SmartDashboard.putData("Field", field);
    }

    /**
     * Command to drive the robot using translative values and heading as angular velocity.
     *
     * @param translationX     Translation in the X direction. Cubed for smoother controls.
     * @param translationY     Translation in the Y direction. Cubed for smoother controls.
     * @param angularRotationX Angular velocity of the robot to set. Cubed for smoother controls.
     * @return Drive command.
     */
    public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) {
      return run(() -> {
        // Make the robot move
        swerveDrive.drive(SwerveMath.scaleTranslation(new Translation2d(
                              translationX.getAsDouble() * swerveDrive.getMaximumChassisVelocity(),
                              translationY.getAsDouble() * swerveDrive.getMaximumChassisVelocity()), 0.8),
                          Math.pow(angularRotationX.getAsDouble(), 3) * swerveDrive.getMaximumChassisVelocity(),
                          fieldOriented,
                          false);
      });
    }

    public Command toggleFieldOrientedCommand() {
      return this.runOnce(
        () -> setFieldOriented(!fieldOriented)
        );
    }

    public Command resetGyro() {
      return this.runOnce(() -> swerveDrive.zeroGyro());
    }

    public void setFieldOriented(Boolean isFieldOriented) {
      fieldOriented = isFieldOriented;
    }

    @Override
    public void simulationPeriodic() {
      field.setRobotPose(swerveDrive.getPose());

      SmartDashboard.putNumber("Pose X: ", swerveDrive.getPose().getX());
      SmartDashboard.putNumber("Pose Y: ", swerveDrive.getPose().getY());
    }  
}