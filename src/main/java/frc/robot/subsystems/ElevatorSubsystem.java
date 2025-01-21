package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;

import java.util.function.DoubleSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfigAccessor;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorSubsystem extends SubsystemBase {

    private SparkMax leftElevator;
    private SparkMax rightElevator;
    private RelativeEncoder leftEncoder;
    private RelativeEncoder rightEncoder;
    private SparkClosedLoopController m_LeftpidController;
    private SparkClosedLoopController m_RightpidController;
    private double kS;
    private double kG;
    private double kV;
    private double kA;
    private double kDt;
    private MotorType m_motor_type;
    private DigitalInput m_limit_switch;
    private double max_velocity;
    private double max_acceleration;
    private double voltage;
    private SparkBase sparkClosedLoopController;
    private TrapezoidProfile profile;
    private TrapezoidProfile.State goal;
    private TrapezoidProfile.State setpoint;

    public ElevatorSubsystem() {
        leftElevator = new SparkMax(Constants.LiftConstants.kLeftLiftControllerPort, MotorType.kBrushless);
        rightElevator = new SparkMax(Constants.LiftConstants.kRightLiftControllerPort, MotorType.kBrushless);

        leftEncoder = leftElevator.getEncoder();
        rightEncoder = rightElevator.getEncoder();

        // initialize the PID controller
        m_LeftpidController = leftElevator.getClosedLoopController();
        m_RightpidController = rightElevator.getClosedLoopController();
            
        
        
       
    }

    public void resetEncoders() {
        leftElevator.set(0);
        rightElevator.set(0);

        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public double getLeftEncoder() {
        return leftEncoder.getPosition();
    }

    public double getRightEncoder() {
        return rightEncoder.getPosition();
    }

    /**
     * Set the power of the left lift motor
     * 
     * @param left_power Value between 0 and 1
     */

    public void moveElevator(DoubleSupplier power) {
        rightElevator.set(power.getAsDouble());
        leftElevator.set(power.getAsDouble());
    }

    public void moveToPosition(double position) {

        m_LeftpidController.setReference(position, SparkBase.ControlType.kPosition);
        m_RightpidController.setReference(position, SparkBase.ControlType.kPosition);
    }

    public void setSpeed(DoubleSupplier speed) {
        double vroom = speed.getAsDouble();
        leftElevator.set(vroom);
        rightElevator.set(vroom);
    }

    // public boolean limitSwitchTriggered() {
    // return m_limit_switch.get();
    // }
    public void periodic() {
    }
}
