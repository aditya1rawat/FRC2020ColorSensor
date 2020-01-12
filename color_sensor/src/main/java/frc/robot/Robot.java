/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




public class Robot extends TimedRobot {

  /*
   * static double P = 0.6; static double I = 0.0; static double D = 0.0;
   */
  // The "right" side gets the steering encoder.
  // The "left" side doesn't get squat because we didn't finish everything in
  // time.
  // public static void AdityaHasABigBrain() {

  // right.configSelectedFeedbackSensor( FeedbackDevice.CTRE_MagEncoder_Relative,
  // 0, 10); // Configuration Timeout

  /*
   * setPid(P, I, D); .config_kF(1,1023.0/100.0,10); right.selectProfileSlot(0,
   * 0); right.selectProfileSlot(1,1); right.setSensorPhase(inverted1);
   * 
   * right.setSelectedSensorPosition(0);
   * 
   * right.getSensorCollection().setQuadraturePosition(0, 10); }
   * 
   * public static void setPid(double P, double I, double D) { right.config_kP(0,
   * P, 10); right.config_kI(0, I, 10); right.config_kD(0, D, 10); }
   */

  /**
   * Change the I2C port below to match the connection of your color sensor
   */
  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a parameter.
   * The device will be automatically initialized with default parameters.
   */
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
    /**
     * The method GetColor() returns a normalized color value from the sensor and
     * can be useful if outputting the color to an RGB LED or similar. To read the
     * raw color, use GetRawColor().
     * 
     * The color sensor works best when within a few inches from an object in well
     * lit conditions (the built in LED is a big help here!). The farther an object
     * is the more light from the surroundings will bleed into the measurements and
     * make it difficult to accurately determine its color.
     */
    Color detectedColor = m_colorSensor.getColor();
    /**
     * The sensor returns a raw IR value of the infrared light detected.
     */

    /**
     * Open Smart Dashboard or Shuffleboard to see the color detected by the sensor.
     **/

    double red = detectedColor.red;  /// 0.250;
    double green = detectedColor.green; // / 0.474;
    double blue = detectedColor.blue; // / 0.275;

    double maxC = Math.max(Math.max(red, green), blue);

    double c = (maxC-red)/maxC;
    double m = (maxC-green)/maxC;
    double y = (maxC-blue)/maxC;
    double k = (1-maxC);

    c*=100;
    m*=100;
    y*=100;
    k*=100;
    /*red /= maxC;
    green /= maxC;
    blue /= maxC;*/

  
    // r 0.250, g 0.474 b 0.275

    SmartDashboard.putNumber("Cyan", c);
    SmartDashboard.putNumber("Magenta", m);
    SmartDashboard.putNumber("Yellow", y);
    SmartDashboard.putNumber("Black", k);

    //System.out.println(detectedColor.red*255+" "+detectedColor.green*255+" "+detectedColor.blue*255);

    /*try {
      colorSender.rgbSend("" + (255 * red), "" + (255 * green), "" + (255 * blue));
      //System.out.println(colorSender);
    } catch (IOException e) {
      e.printStackTrace();
    }*/


   //System.out.println((int)(cyan*100) + " " + (int)(magenta*100) + " " + (int)(yellow*100) + " " + (int)(black*100));
    /**
     * In addition to RGB IR values, the color sensor can also return an 
     * infrared proximity value. The chip contains an IR led which will emit
     * IR pulses and measure the intensity of the return. When an object is 
     * close the value of the proximity will be large (max 2047 with default
     * settings) and will approach zero when the object is far away.
     * 
     * Proximity can be used to roughly approximate the distance of an object
     * or provide a threshold for when an object is close enough to provide
     * accurate color values.
     */
  

    

  }
}