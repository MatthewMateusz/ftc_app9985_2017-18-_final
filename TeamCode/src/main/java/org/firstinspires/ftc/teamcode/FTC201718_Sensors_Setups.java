package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Matthew on 9/24/2017.
 */

public class FTC201718_Sensors_Setups
{
    public static final double TailUp   = -0.1;
    public static final double TailDown = 0.7;

    //Public sensor members

    //Touch Sensors
      //public ModernRoboticsTouchSensor *name* = null;
    public ModernRoboticsTouchSensor limitArmUp  = null;
    public ModernRoboticsTouchSensor limitArmDown = null;


    //Optical Distance Sensor (Bright or dark) sensor
      //public OpticalDistanceSensor *name* = null;

    //Color Sensor
    public ColorSensor LeftColorSensor  = null;
    public ColorSensor RightColorSensor = null;

    //MRGyro sensor, NOTE: will be set back to NULL if problems with initalization or calibration
      //public ModernRoboticsI2cGyro gyroSensor = null;

    //MR Range Sensors
      //public ModernRoboticsI2cRangeSensor *name* = null;

    //Hardware setup
    HardwareMap hwMap = null;

    //Constructor
    public FTC201718_Sensors_Setups ()
    {

    }

    //Init standard Hardware interfaces
    public void init(HardwareMap ahwMap)
    {
        hwMap = ahwMap;

        //Defining Touch Sensors
          //*Program name* = (ModernRoboticsTouchSensor) hwmap.touchSensor.get("Phone Name");
        limitArmUp   = (ModernRoboticsTouchSensor) hwMap.touchSensor.get("arm_limitUp");
        limitArmDown = (ModernRoboticsTouchSensor) hwMap.touchSensor.get("arm_limitDown");

        //Defining 'Bight or Dark' sensors and enableing led
          //*Program name* = hwmap.opticalDistanceSensor.get("*Phone Name*");
          //*Program name*.enabledLed(false); //Change false to true to enable LED

        //Define Color sensor and Enable led
          //*Program name* = hwmap.colorSensor.get("*Phone Name*");
          //*Program name*.enabledLed(false); //Change false to true to enable LED

        LeftColorSensor  = hwMap.colorSensor.get("left_colorSensor");
        RightColorSensor = hwMap.colorSensor.get("right_colorSensor");

        LeftColorSensor.enableLed(false);
        RightColorSensor.enableLed(false);

        //Define MRGyro
          //

        //Define Range Sensors
          //
    }
}
