package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Matthew on 9/24/2017.
 */

abstract public class FTC201718_Automation extends LinearOpMode
{
    //Declear OpMode constants for users

    //Drive Constants
    public static final double SPEED_FULL   = 1;
    public static final double SPEED_NORMAL = 0.5;
    public static final double SPEED_SLOW   = 0.1;

    //Turn Constants
    public static final double SPEED_TURN = 0.4;
    public static final double ANGLE_90   = 90.0;
    public static final double TURN_LEFT  = -ANGLE_90;
    public static final double TURN_RIGHT = ANGLE_90;

    //Timeout Constants
    public static final double TOUT_SHORT  = 3;
    public static final double TOUT_MEDIUM = 5;
    public static final double TOUT_LONG   = 10;

    //Declare OpMode data
    FTC201718_Actuators_Setup actuators = new FTC201718_Actuators_Setup();
    FTC201718_Sensors_Setups sensors = new FTC201718_Sensors_Setups();

    //Timeout variable
    private ElapsedTime runtime = new ElapsedTime();

    public void setupHardware()
    {
        /*
        Initialize the drive system variables
        The init() method of the hardware class does all the work here
         */
        telemetry.addData("Status" , "Init Automation");
        telemetry.addData("Status" , "Init Actuators");
        telemetry.update();
        actuators.init(hardwareMap);
        telemetry.addData("Status" , "Init Automation");
        telemetry.addData("Status" , "Init Sensors");
        telemetry.update();
        sensors.init(hardwareMap);
        telemetry.addData("Status" , "Init Automation Done");
        telemetry.update();
    }

    public void waitForStartAndDisplayWhileWaiting()
    {
        while (!isStarted() && !isStopRequested())
        {
            //insert telemetry data for sensors and other data
            telemetry.addData("Left Color:  " ,
                    "RED " + sensors.LeftColorSensor.red() +
                    "  GRN " + sensors.LeftColorSensor.green() +
                    "  BLU " + sensors.LeftColorSensor.blue());

            telemetry.addData("Right Color:  " ,
                    "RED " + sensors.RightColorSensor.red() +
                    "  GRN " + sensors.RightColorSensor.green() +
                            "  BLU " + sensors.RightColorSensor.green());

            //Touch sensor data

            telemetry.update();
            idle();
        }
    }

    public void fullStop()
    {
        //Make sure to add new motors to this list NWMRT
        telemetry.addData("Status" , "fullStop");
        telemetry.update();
        actuators.FrontLeft.setPower(0);
        actuators.FrontRight.setPower(0);
        actuators.RearLeft.setPower(0);
        actuators.RearRight.setPower(0);
    }

    public void encoderReset()
    {
        //Make sure to add new motors to this list NWMRT
        telemetry.addData("Status" , "encoderReset");
        telemetry.update();
        actuators.FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



}
