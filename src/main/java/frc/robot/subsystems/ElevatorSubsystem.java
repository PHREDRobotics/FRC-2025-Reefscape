package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Configs;

import java.util.function.DoubleSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem for controlling the elevator
 */
public class ElevatorSubsystem extends SubsystemBase {

    private SparkMax elevator;
    private RelativeEncoder encoder;
    private SparkClosedLoopController m_pidController;
    private SparkLimitSwitch bottomForwardLimit;
    // private DigitalInput bottomForwardLimit = new DigitalInput(1);

    private double voltage;
    private int level;

    /**
     * Creates a new elevator subsystem.
     */
    public ElevatorSubsystem() {
        elevator = new SparkMax(Constants.ElevatorConstants.kElevatorCANId, MotorType.kBrushless);

        encoder = elevator.getEncoder();

        // initialize the PID controller
        m_pidController = elevator.getClosedLoopController();
        elevator.configure(Configs.ElevatorMotor.motorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

        // topForwardLimit = elevator.getReverseLimitSwitch();
        bottomForwardLimit = elevator.getReverseLimitSwitch();
    }

    /**
     * @return boolean
     */
    public boolean isLimitSwitchPressed() {
        // if(bottomForwardLimit.isPressed() /*|| topForwardLimit.isPressed()*/){
        // return true;
        // }else{
        // return false;
        // }
        if (bottomForwardLimit.isPressed()) {
            // resetEncoders();

            return true;
        }
        return false;
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
     * @param power Value between 0 and 1
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

    public void moveToPosition(double positionTicks) {

        m_pidController.setReference(positionTicks, SparkBase.ControlType.kMAXMotionPositionControl,
                ClosedLoopSlot.kSlot0, Constants.ElevatorConstants.kArbFF);

        SmartDashboard.putNumber("Elevator/Position ticks moveToPosition()", positionTicks);

        if (positionTicks == Constants.ElevatorConstants.kCoralLevel1) {
            level = 1;
        } else if (positionTicks == Constants.ElevatorConstants.kCoralLevel2) {
            level = 2;
        } else if (positionTicks == Constants.ElevatorConstants.kCoralLevel3) {
            level = 3;
        } else if (positionTicks == Constants.ElevatorConstants.kCoralLevel4) {
            level = 4;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setRawPower(double power) {
        voltage = power * Constants.ElevatorConstants.kVoltageMultiplier;
        elevator.setVoltage(voltage);
    }

    public void setSpeed(double speed) {
        // double vroom = speed.getAsDouble();
        elevator.set(speed);
    }

    // public boolean limitSwitchTriggered() {
    // return m_limit_switch.get();
    // }
    public void periodic() {
        SmartDashboard.putNumber("Elevator/Elevator Encoder Ticks", getEncoder());
        SmartDashboard.putNumber("Elevator/Centimeters",
                getEncoder() * Constants.ElevatorConstants.kEncoderTicksToCentimeters);
        // SmartDashboard.putBoolean("Is top Limit switch triggered",
        // topForwardLimit.isPressed());

        // SmartDashboard.putBoolean("Is bottom Limit switch triggered",
        // bottomForwardLimit.isPressed());
        SmartDashboard.putBoolean("Elevator/Is bottom Limit switch triggered", isLimitSwitchPressed());

        SmartDashboard.putNumber("Elevator/Elevator Motor RPM", encoder.getVelocity());
    }
}