// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PneumaticsConstants;

public class ClimbSubsystem extends SubsystemBase {
  /** Creates a new ClimbSubsystem. */
  public static PneumaticHub pH;
  public static DoubleSolenoid doubleSolenoidClimber;

  public ClimbSubsystem() {

    // private final DoubleSolenoid m_doubleSolenoid =
    // new DoubleSolenoid(PneumaticsModuleType.REVPH, 1, 2);
    /**
     * An example method querying a boolean state of the subsystem (for example, a
     * digital sensor).
     *
     * @return value of some boolean subsystem state, such as a digital sensor.
     */

    pH = new PneumaticHub(PneumaticsConstants.kPneumaticsCANID);

    pH.enableCompressorDigital();

    doubleSolenoidClimber = pH.makeDoubleSolenoid(PneumaticsConstants.kSolenoidInput,
        PneumaticsConstants.kSolenoidOutput);
    addChild("Double Solenoid Climber", doubleSolenoidClimber);

    doubleSolenoidClimber.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void extendPneumaticClimber() {
    doubleSolenoidClimber.set(DoubleSolenoid.Value.kForward);
  }

  public void retractPneumaticClimber() {
    doubleSolenoidClimber.set(DoubleSolenoid.Value.kReverse);
  }
}