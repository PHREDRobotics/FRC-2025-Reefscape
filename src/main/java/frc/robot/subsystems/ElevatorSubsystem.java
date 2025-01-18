package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;

import java.util.function.DoubleSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
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
    private PIDController pidController;
    private TrapezoidProfile profile;
    private TrapezoidProfile.State goal;
    private TrapezoidProfile.State setpoint;


    public ElevatorSubsystem() {
        leftElevator = new SparkMax(Constants.LiftConstants.kLeftLiftControllerPort, MotorType.kBrushless);
        rightElevator = new SparkMax(Constants.LiftConstants.kRightLiftControllerPort, MotorType.kBrushless);
        leftEncoder = leftElevator.getEncoder();
        rightEncoder = rightElevator.getEncoder();



        setpoint = new TrapezoidProfile.State();
        pidController = new PIDController(Constants.ElevatorConstants.kP, Constants.ElevatorConstants.kI, Constants.ElevatorConstants.kD);
        profile = new TrapezoidProfile(new TrapezoidProfile.Constraints(max_velocity, max_acceleration));
        goal = new TrapezoidProfile.State();
        setpoint = new TrapezoidProfile.State();



        kDt = 0.2;

        kS = 0;
        kG = .1;
        kV = 0;
        kA = 0;

        max_velocity = 10;
        max_acceleration = 5;


      
        
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
 
     public void moveElevator(DoubleSupplier power){
        rightElevator.set(power.getAsDouble());
        leftElevator.set(power.getAsDouble());
     }

     
    public void setRawPower(double power) {
        voltage = power * Constants.ArmConstants.kVoltageMultiplier;
        leftElevator.setVoltage(voltage);
        rightElevator.setVoltage(voltage);
    }

    public void setSpeed(DoubleSupplier speed) {
        double vroom = speed.getAsDouble();
        leftElevator.set(vroom);
        rightElevator.set(vroom);
    }

   // public boolean limitSwitchTriggered() {
     //   return m_limit_switch.get();
    //}
    public void periodic() {
        kDt = kDt + 1 / 50;
    }
}
