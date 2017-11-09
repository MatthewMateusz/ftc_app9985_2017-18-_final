package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;

/**
 * Created by Matthew on 9/24/2017.
 */



public class FTC201718_Actuators_Setup
{
    /* Declare motors on robot
    Exameple: public DcMotor *name* = null;
    */
    public DcMotor FrontLeft  = null;
    public DcMotor FrontRight = null;
    public DcMotor RearLeft   = null;
    public DcMotor RearRight  = null;
    public DcMotor YFrontArm  = null;

    public static final double COUNTS_PER_MOTOR_REV     = 1440; // eg: TERIX Motor Encoders
    public static final double DRIVE_GEAR_REDUCTION     = 1.0; // This is < if geared UP
    public static final double WHEEL_DIAMETER_INCHES    = 4.5 * (24.5/24.0); //wheel circumference with real-life travel distance adjustment
    public static final double WHEEL_SEPERATION_INCHES  = 16.0; //
    public static final double COUNTS_PER_INCHES        = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.14159269);
    public static final double INCHES_PER_ANGLE_INPLACE = (11.8/90.0); //calibration affected by the distance between the driving wheels
    public static final double INCHES_PER_ANGLE_DRAG    = ( (WHEEL_SEPERATION_INCHES * 3.14159269 / 2.0) / 90.0); // calibration affected by the distance between the driving wheels

    //Declares servos on robot
    public Servo ServoArm         = null;
    public Servo LeftGlyphHolder  = null;
    public Servo RightGlyphHolder = null;

    //Servo starting positions
    double ServoArmPostition = 0.5; // 0 - 1 where 0 is 0 degrees and 1 is 180 degrees

    // Hardware mapping setup
    HardwareMap hwMap = null;

    //Constructor
    public FTC201718_Actuators_Setup()
    {

    }

    // Initialize standard hardware interfaces
    public void init(HardwareMap ahwmap)
    {
        // Save reference to Hardware map
        hwMap = ahwmap;

        //Define and Initialize Motors
        /* Example: *name* = hwMap.dcMotor.get("*name of motor in phone*");
        *  Example: *name*.setDirection(DcMotor.Direction.|FORWARD or REVERSE|)
        */



        FrontLeft  = hwMap.dcMotor.get("front_left");
        FrontRight = hwMap.dcMotor.get("front_right");
        RearLeft   = hwMap.dcMotor.get("rear_left");
        RearRight  = hwMap.dcMotor.get("rear_right");
        YFrontArm  = hwMap.dcMotor.get("Y_Arm");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        RearRight.setDirection(DcMotor.Direction.REVERSE);
        RearLeft.setDirection(DcMotor.Direction.REVERSE);

        //Set all motors to zero power
        /*
         Example: *name*.setPower(0);
         */
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        RearLeft.setPower(0);
        RearRight.setPower(0);
        YFrontArm.setPower(0);

        //Set all motors to run without encoders
        //May want to use RUN_USING_ENCODERS if encoders are installed
        /*
         Example: *name*.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
         */
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        YFrontArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //Define and Initalize all installed servos
        ServoArm         = hwMap.servo.get("servo_arm");
        LeftGlyphHolder  = hwMap.servo.get("servo_leftHolder");
        RightGlyphHolder = hwMap.servo.get("servo_rightHolder");

        //Set Servo Positions
        ServoArm.setPosition(-0.1);
        LeftGlyphHolder.setPosition(0.325);
        RightGlyphHolder.setPosition(0.65);
    }
}
