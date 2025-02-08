package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;

import java.util.function.DoubleSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLimitSwitch;
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

    private SparkMax elevator;
    private RelativeEncoder encoder;
    private SparkClosedLoopController m_pidController;
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
    private SparkLimitSwitch forwardLimit = elevator.getForwardLimitSwitch();

    public ElevatorSubsystem() {
        elevator = new SparkMax(Constants.ElevatorConstants.kElevatorPort, MotorType.kBrushless);

        encoder = elevator.getEncoder();

        // initialize the PID controller
        m_pidController = elevator.getClosedLoopController();

    }

    public boolean isLimitSwitchPressed() {
        return forwardLimit.isPressed();
    }

    public void resetEncoders() {
        encoder.setPosition(0);
        elevator.set(0);
    }

    public double getEncoder() {
        return encoder.getPosition();
    }

    /**
     * Set the power of the left lift motor
     * 
     * @param left_power Value between 0 and 1
     */

    public void moveElevator(DoubleSupplier power) {
        elevator.set(power.getAsDouble());
    }


    // public static final double kEncoderTicksPerRotation = 42;
    // public static final double kElevatorGearRatio = (1 / 4);
    // public static final double kGearTeethPerRotation = 16;

    // // Need to get these values and change them later
    // public static final double kGearDiameter = 0;
    // public static final double kChainDistancePerRevolution = 0;



    public void moveToPosition(double positionCentimeters) {

        m_pidController.setReference(Constants.ElevatorConstants.kEncoderTicksToCentimeters
         *  positionCentimeters, SparkBase.ControlType.kPosition);
    }

    public void setRawPower(double power) {
        voltage = power * Constants.ElevatorConstants.kVoltageMultiplier;
        elevator.setVoltage(voltage);
    }

    public void setSpeed(DoubleSupplier speed) {
        double vroom = speed.getAsDouble();
        elevator.set(vroom);
    }

    // public boolean limitSwitchTriggered() {
    // return m_limit_switch.get();
    // }
    public void periodic() {
    }
}
