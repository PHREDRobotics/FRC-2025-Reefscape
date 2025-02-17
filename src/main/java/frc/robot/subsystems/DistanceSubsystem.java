package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DistanceSubsystem extends SubsystemBase {

    static final int kUltrasonicPingPort = 0;
    static final int kUltrasonicEchoPort = 1;

    // Ultrasonic sensors tend to be quite noisy and susceptible to sudden outliers,
    // so measurements are filtered with a 5-sample median filter
    private final MedianFilter m_filter = new MedianFilter(5);
    // Smartdashboard editable variable
    private double m_distanceFromWall = 6;
    private final Ultrasonic m_ultrasonic = new Ultrasonic(kUltrasonicPingPort, kUltrasonicEchoPort);

    public void periodic(){
        double measurement = m_ultrasonic.getRangeMM();
        double filteredMeasurement = m_filter.calculate(measurement);
    m_distanceFromWall=SmartDashboard.getNumber("Distance From Wall Target:", m_distanceFromWall);
    SmartDashboard.putNumber("Distance From Wall Target:", m_distanceFromWall);
    SmartDashboard.putNumber("Distance From Wall:", filteredMeasurement);
    }

}
