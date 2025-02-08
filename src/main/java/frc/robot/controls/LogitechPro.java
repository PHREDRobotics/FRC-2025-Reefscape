package frc.robot.controls;

//import edu.wpi.first.networktables.NetworkTableValue;
// import frc.robot.controls.FlightStick;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.DriveConstants;

/**
 * A FlightStick specifically calibrated for the Logitech Extreme 3D Pro
 * joystick.
 * 
 * @author zach
 *
 */
public class LogitechPro extends FlightStick {
  private static final int NUM_BUTTONS = 12;

  /* Axes Definitions */
  public static final int PITCH_AXIS = 1;
  public static final int ROLL_AXIS = 0;
  public static final int YAW_AXIS = 2;
  public static final int THROTTLE_AXIS = 3;

  /**
   * Creates a new Logitech Extreme 3D Pro on the given port.
   * 
   * @param port the port the Logitech Extreme 3D Pro is assigned in the driver
   *             station.
   */
  public LogitechPro(int port) {
    super(port, "Logitech Extreme 3D Pro");
    JoystickButton[] buttons = new JoystickButton[NUM_BUTTONS + 1];
    for (int i = 1; i < NUM_BUTTONS + 1; i++) {
      buttons[i] = new JoystickButton(this, i);
    }
  }

  // @Override
  public double getPitch() {
    return -getAxis(PITCH_AXIS);
  }

  // @Override
  public double getNormalPitch() {
    return 1 - getNormalizedAxis(PITCH_AXIS);
  }

  // @Override
  public double getYaw() {
    return getAxis(YAW_AXIS);
  }

  // @Override
  public double getNormalYaw() {
    return getNormalizedAxis(YAW_AXIS);
  }

  // @Override
  public double getRoll() {
    return getAxis(ROLL_AXIS);
  }

  // @Override
  public double getNormalRoll() {
    return getNormalizedAxis(ROLL_AXIS);
  }

  @Override
  public double getThrottl() {
   return ((1+(-1 * getAxis(THROTTLE_AXIS)))/2);
  }

  // @Override
  public double getNormalThrottle() {
    return 1 - getNormalizedAxis(THROTTLE_AXIS);
  }

  public boolean getTrigger() {
    return getButton(1);
  }

  public double getXSlow() {
    return -getX() * (getTrigger() == true ? Math.abs(getX()) : 1);
  }

  public double getYSlow() {
    return -getY() * (getTrigger() == true ? Math.abs(getX()) : 1);
  }

  // @Override
  protected void putDashboard() {
    SmartDashboard.putNumber("Channel", port);
    SmartDashboard.putNumber("Pitch Axis", getPitch());
    SmartDashboard.putNumber("Roll Axis", getRoll());
    SmartDashboard.putNumber("Yaw Axis", getYaw());
    SmartDashboard.putNumber("Throttle Axis", getThrottle());
    SmartDashboard.putNumber("Normal Pitch", getNormalPitch());
    SmartDashboard.putNumber("Normal Roll", getNormalRoll());
    SmartDashboard.putNumber("Normal Yaw", getNormalYaw());
    SmartDashboard.putNumber("Normal Throttle", getNormalThrottle());

  }

  @Override
  public void initSendable(SendableBuilder builder) {

  }

}