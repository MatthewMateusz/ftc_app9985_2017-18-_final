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

    public static final double COUNTS_PER_INCHES    = 90.0;         // to be calibrated empircally
    public static final double COUNTS_PER_ANGLE_NEG     = 1751.0/90.0;  // to be calibrated empircally
    public static final double COUNTS_PER_ANGLE_POS     = 1703.0/90.0;  // to be calibrated empircally
    public static final double COUNTS_PER_ASIDE     = 120.0;        // to be calibrated empircally

    //Declares servos on robot
    public Servo ServoArm         = null;
    public Servo SwingArm         = null;
    public Servo LeftGlyphHolder  = null;
    public Servo RightGlyphHolder = null;


    public static final double TailUp   = -0.1;
    public static final double TailDown = 0.9;

    public static final double BlockGabberLeft_OPEN = 0.325;
    public static final double BlockGabberRight_OPEN = 0.65;
    public static final double BlockGabberLeft_RELEASE = 0.898;
    public static final double BlockGabberRight_RELEASE = 0.113;
    public static final double BlockGabberLeft_CLOSE = 1;
    public static final double BlockGabberRight_CLOSE  = 0;

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

        //Autonomous
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        RearLeft.setDirection(DcMotor.Direction.FORWARD);
        RearRight.setDirection(DcMotor.Direction.REVERSE);


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
        SwingArm         = hwMap.servo.get("servo_swing");

        //Set Servo Positions
        ServoArm.setPosition(TailUp);
        LeftGlyphHolder.setPosition(BlockGabberLeft_OPEN);
        RightGlyphHolder.setPosition(BlockGabberRight_OPEN);
        SwingArm.setPosition(0.5);
    }
}
