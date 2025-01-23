// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.GrabberConstants;
//import frc.robot.commands.DriveMotor;
//import frc.robot.commands.OuttakeCommand;
//import frc.robot.subsystems.MotorTestSubsystem;
import frc.robot.commands.*;
import frc.robot.controls.LogitechPro;
import frc.robot.subsystems.*;
// import frc.robot.subsystems.MotorTestSubsystem;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
// import frc.robot.commands.Autos;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
        // The robot's subsystems and commands are defined here...
        // private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
        // private final MotorTestSubsystem motorTestSubsystem = new
        // MotorTestSubsystem();
        private final LogitechPro joyStick = new LogitechPro(1);

        private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
        private final CoralSubsystem coralSubsystem = new CoralSubsystem();

        // private final MotorTestSubsystem motorTestSubsystem = new
        // MotorTestSubsystem();
        /*
         * DigitalInput limitSwitch = new
         * DigitalInput(Constants.ArmConstants.kLimitSwitchControllerPort);
         * private final ArmSubsystem armSubsystem = new ArmSubsystem(
         * Constants.ArmConstants.kArmControllerPort, CANSparkMax.MotorType.kBrushless,
         * limitSwitch,
         * 0.6, 0, 0, 0.02,
         * 0, 0.1, 0, 0,
         * 10, 5);
         */
        // Replace with CommandPS4Controller or CommandJoystick if needed
        private final XboxController driverJoystick = new XboxController(0);
       
       

        // DigitalInput limitSwitch = new DigitalInput(9);

        // Trigger limitTrigger = new Trigger(limitSwitch::get);

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {

                configureBindings();
        }

        /**
         * Use this method to define your trigger->command mappings. Triggers can be
         * created via the
         * {@link Trigger trigger(java.util.function.BooleanSupplier)} constructor with
         * an arbitrary
         * predicate, or via the named factories in {@link
         * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
         * {@link
         * CommandXboxController
         * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
         * PS4} controllers or
         * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
         * joysticks}.
         */
        private void configureBindings() {
                // Define triggers
                Trigger xButton = new JoystickButton(driverJoystick, Constants.OIConstants.kXButton);
                Trigger yButton = new JoystickButton(driverJoystick, Constants.OIConstants.kYButton);
                Trigger aButton = new JoystickButton(driverJoystick, Constants.OIConstants.kAButton);
                Trigger bButton = new JoystickButton(driverJoystick, Constants.OIConstants.kBButton);
                Trigger rightTrigger = new JoystickButton(driverJoystick, Constants.OIConstants.kRightTriggerAxis);
                Trigger leftTrigger = new JoystickButton(driverJoystick, Constants.OIConstants.kLeftTriggerAxis);
                Trigger start = new JoystickButton(driverJoystick, Constants.OIConstants.kStartButton);
                // Set default commands
                
                int pov = driverJoystick.getPOV();
                if (pov == 0) {
                    // D-pad up pressed
                } else if (pov == 90) {
                    // D-pad right pressed
                } else if (pov == 180) {
                    // D-pad down pressed
                } else if (pov == 270) {
                    // D-pad left pressed
                }
                

                bButton.onTrue(new CoralOuttakeCommand(coralSubsystem));
                aButton.onTrue(new CoralIntakeCommand(coralSubsystem));

         /*       swerveSubsystem.setDefaultCommand(new SwerveJoystickCmd(
                                swerveSubsystem,
                                () -> -joyStick.getPitch(),
                                () -> -joyStick.getRoll(),
                                () -> -joyStick.getYaw(),
                                () -> joyStick.getThrottl(),
                                () -> joyStick.getTrigger()));
     */
                                

                // Configure mechanical triggers
                // limitTrigger.onTrue(
                // new AutoResetArmEncoder(armSubsystem, limitSwitch.get()));

                // Configure gamepad buttons
                // xButton.onTrue(new ArmMotor(Constants.ArmConstants.kArmLow, armSubsystem));
                // yButton.onTrue(new ArmMotor(Constants.ArmConstants.kArmMid, armSubsystem));
                // bButton.onTrue(new ArmMotor(Constants.ArmConstants.kArmHigh, armSubsystem));
                // yButton.onTrue(new UnretractLift(liftSubsystem));
                // yButton.whileTrue(new ManualLiftCmd(
                //                                 () -> driverJoystick.getLeftTriggerAxis(),
                //                                 () -> driverJoystick.getRightTriggerAxis(),
                //                                 liftSubsystem));
                // xButton.onTrue(new OuttakeCommand(intakeSubsystem));
                // bButton.onTrue(new ParallelCommandGroup(new ShooterCommand(shooterSubsystem),
                //                 new OuttakeCommand(intakeSubsystem)));
                // aButton.onTrue(new IntakeCommand(intakeSubsystem));
                // // leftBumper.onTrue(new AutoResetArmEncoder(armSubsystem));
                // startButton.onTrue(new InstantCommand(() -> swerveSubsystem.zeroHeading()));
                

                /*
                 * Use this to pass the autonomous command to the main {@link Robot} class.
                 *
                 * @return the command to run in autonomous
                 */

                /**
                 * Use this to pass the autonomous command to the main {@link Robot} class.
                 *
                 * @return the command to run in autonomous
                 *
                 *         public Command getAutonomousCommand() {
                 *         // 1. Create trajectory settings
                 *         TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
                 *         AutoConstants.kMaxSpeedMetersPerSecond,
                 *         AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                 *         .setKinematics(DriveConstants.kDriveKinematics);
                 * 
                 *         Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                 *         new Pose2d(0, 0, new Rotation2d(0)),
                 *         List.of(
                 *         new Translation2d(1, 0),
                 *         new Translation2d(1, -1)),
                 *         new Pose2d(2, -1, Rotation2d.fromDegrees(180)),
                 *         trajectoryConfig);
                 * 
                 *         // 3. Define PID controllers for tracking trajectory
                 *         PIDController xController = new
                 *         PIDController(AutoConstants.kPXController, 0,
                 *         0);
                 *         PIDController yController = new
                 *         PIDController(AutoConstants.kPYController, 0,
                 *         0);
                 *         ProfiledPIDController thetaController = new ProfiledPIDController(
                 *         AutoConstants.kPThetaController, 0, 0,
                 *         AutoConstants.kThetaControllerConstraints);
                 *         thetaController.enableContinuousInput(-Math.PI, Math.PI);
                 * 
                 *         // 4. Construct command to follow trajectory
                 *         SwerveControllerCommand swerveControllerCommand = new
                 *         SwerveControllerCommand(
                 *         trajectory,
                 *         swerveSubsystem::getPose,
                 *         DriveConstants.kDriveKinematics,
                 *         xController,
                 *         yController,
                 *         thetaController,
                 *         swerveSubsystem::setModuleStates,
                 *         swerveSubsystem);
                 * 
                 *         // 5. Add some init and wrap-up, and return everything
                 *         return new SequentialCommandGroup(
                 *         new InstantCommand(() ->
                 *         swerveSubsystem.resetOdometry(trajectory.getInitialPose())),
                 *         swerveControllerCommand,
                 *         new InstantCommand(() -> swerveSubsystem.stopModules()));
                 *         }
                 */

                // return null;
        }
        
}