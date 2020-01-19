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

import java.io.IOException;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorSensorV3.RawColor;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import frc.robot.ColorServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */


/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

public class Robot extends TimedRobot {
  private ColorServer colorSender;

  private Constants constants = new Constants();
  /**
   * Change the I2C port below to match the connection of your color sensor
   */
  private final I2C.Port i2cPort = I2C.Port.kOnboard;



  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a parameter.
   * The device will be automatically initialized with default parameters.
   */

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);


  //color Match colors
  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final Color kBlueTarget = ColorMatch.makeColor(0.219, 0.465, 0.315); //0.113, 0.422, 0.463
  private final Color kGreenTarget = ColorMatch.makeColor(0.236, 0.483, 0.280); //0.163, 0.580, 0.256
  private final Color kRedTarget = ColorMatch.makeColor(0.269, 0.463, 0.265); // 0.521, 0.345, 0.133
  private final Color kYellowTarget = ColorMatch.makeColor(0.276, 0.489, 0.233); //0.312, 0.564, 0.122
  private final Color kWhiteTarget = ColorMatch.makeColor(0.250, 0.483, 0.265); //0.250, 0.483, 0.265

  //Order of the color on the color wheel

  String[] controlPanelColor = {"Yellow", "Blue", "Green", "Red", "Yellow", "Blue", "Green", "Red"};
  int wheelPosition = 0;
  int wheelRotation = 0;



  @Override
  public void robotInit() {
    colorSender = new ColorServer();

    // Initialize TCP connection
    try {
      colorSender.init();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      // System.out.println("Didn't work. :(");
      e.printStackTrace();
    }

    wheelPosition = 0;
    wheelRotation = 0;
    //adding targets for colormatching
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);   
    m_colorMatcher.addColorMatch(kWhiteTarget);

  }

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




     //get Color
    Color rawDetectedColor = m_colorSensor.getColor();
    Color detectedColor = m_colorSensor.getColor();



  //System.out.println("Raw Detected Color:  Red:"+rawDetectedColor.red+" Green"+rawDetectedColor.green+" Blue"+rawDetectedColor.blue);


    String colorString;
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);



    //get Color Looking at
    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }



    //check that color is not unknown
    if (colorString!="Unknown"){

      //loop until we find the color we are looking at
      while (true){
        
        //check if we reached the curent color the sensor sees
        if (controlPanelColor[wheelPosition] == colorString){
          //if we do exit
          break;
        }
        
        wheelPosition++;
        //check if we completed a rotation
        if (wheelPosition == controlPanelColor.length ){
          wheelPosition = 0;
          wheelRotation++;
        }

      }

    }

    System.out.println("Rotations: " + wheelRotation+ " Wheel Position: " + wheelPosition + " Confidence: " +     match.confidence);
    


     //Callibration
    double red = rawDetectedColor.red * 1.0/0.2509;  /// 0.250;
    double green = rawDetectedColor.green * 1.0/0.473; // / 0.474;
    double blue = rawDetectedColor.blue * 1.0/0.274; // / 0.275;

   /* double c = (maxC-red)/maxC;
    double m = (maxC-green)/maxC;
    double y = (maxC-blue)/maxC;
    double k = (1-maxC);

    c*=100;
    m*=100;
    y*=100;
    k*=100;*/


    /*red /= maxC;
    green /= maxC;
    blue /= maxC;*/

    /*c/=constants.CYANCALIBRATION;
    m/=constants.MAGENTACALIBRATION;
    y/=constants.YELLOWCALIBRATION;*/
    
   
    double maxC = Math.max(Math.max(red, green), blue);


    //Normalization here
    
    red/=maxC;
    green/=maxC;
    blue/=maxC;

    /*SmartDashboard.putNumber("Cyan", c);
    SmartDashboard.putNumber("Magenta", m);
    SmartDashboard.putNumber("Yellow", y);
    SmartDashboard.putNumber("Black", k);*/

    //Send RGB values to client(laptop)
    try {
      colorSender.colorSend(red *255 , green*255, blue*255);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // r 0.250, g 0.474 b 0.275


    //System.out.println(detectedColor.red*255+" "+detectedColor.green*255+" "+detectedColor.blue*255);

    /*try {
      colorSender.rgbSend("" + (255 * red), "" + (255 * green), "" + (255 * blue));
      //System.out.println(colorSender);
    } catch (IOException e) {
      e.printStackTrace();
    }*/


   //System.out.println((int)(cyan*100) + " " + (int)(magenta*100) + " " + (int)(yellow*100) + " " + (int)(black*100)); 

  }
}