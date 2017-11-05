package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

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

    //Variables to be used for later
    public VuforiaLocalizer vuforiaLocalizer;
    public VuforiaLocalizer.Parameters parameters;
    public VuforiaTrackables visionTargets;
    public VuforiaTrackable target;
    public VuforiaTrackableDefaultListener listener;

    public OpenGLMatrix lastKnownLocation;
    public OpenGLMatrix phoneLocation;

    private static final String VUFORIA_KEY = "AUYFfMb/////AAAAGeK9R/Mswk3ko4WgwY69fsB3D/KziaC/ZBui6bKvAUjjnhKoPyiDs0+TfVP3vMkYQ4Q0Amo4yosMAH9Xs0k+HX5MHGkhFbGLrDYj5zUN8NinByqcruRQZJuuISEHn1TfD5Fpa9psUmylGexAIwVB6WMfYTL2eKg4EE5mAaRsPgRKZnk/SjMzitYtthDxFusHftOK0N8vywIVSX79mBGmdy6+XUqLLa72zYXUvCrs9lov+xGuC06dUrmpFHl7uwt75QBVb5qyvbsruC4Bfnezzz1S747xiTHQz7Q86q1ZCix2V3AmxQxUuqhlYXDiC6uBseB3npuzuRtNxyCpn6+p3L1qv+Y1axec01BAOUATpSvy";

    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

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

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive())
        {

            // Determine new target position, and pass to motor controller
            newLeftTarget = actuators.FrontLeft.getCurrentPosition() + (int) (leftInches * FTC201718_Actuators_Setup.COUNTS_PER_INCHES);
            newRightTarget = actuators.FrontRight.getCurrentPosition() + (int) (rightInches * FTC201718_Actuators_Setup.COUNTS_PER_INCHES);



            actuators.FrontLeft.setTargetPosition(newLeftTarget);
            actuators.FrontRight.setTargetPosition(newRightTarget);
            actuators.RearLeft.setTargetPosition(newLeftTarget);
            actuators.RearRight.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.FrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.RearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.RearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            actuators.FrontLeft.setPower(speed);
            actuators.FrontRight.setPower(speed);
            actuators.RearLeft.setPower(speed);
            actuators.RearRight.setPower(speed);

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && (runtime.seconds() < timeoutS) ||
                    (actuators.FrontLeft.isBusy() || actuators.FrontRight.isBusy() || actuators.RearLeft.isBusy() || actuators.RearRight.isBusy()) // && -> || --correction for the case of turn and drag
                    )
            {

                // Display it for the driver.
                telemetry.addData("Status", "encoderDrive");
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d",
                        actuators.FrontLeft.getCurrentPosition(), actuators.FrontRight.getCurrentPosition(), actuators.RearLeft.getCurrentPosition(), actuators.RearRight.getCurrentPosition());
                telemetry.update();
                idle();
            }

            // Stop all motion;
            actuators.FrontLeft.setPower(0);
            actuators.FrontRight.setPower(0);
            actuators.RearLeft.setPower(0);
            actuators.RearRight.setPower(0);

            // Turn off RUN_TO_POSITION
            actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            actuators.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            actuators.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            actuators.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void encoderDriveDistance(double speed, double distance, double timeoutS) {
        telemetry.addData("Status", "encoderDriveDistance");
        telemetry.update();
        if (speed < 0.0) {
            // speed for the encoderDrive(...) must be always positive!
            speed = -speed;
            distance = -distance;
        }
        encoderDrive(speed, distance, distance, timeoutS);
    }

    public void encoderTurnInPlace(double speed, double degrees, double timeoutS) {
        telemetry.addData("Status", "encoderTurnInPlace");
        telemetry.update();
        if (speed < 0.0) {
            // speed for the encoderDrive(...) must be always positive!
            speed = -speed;
            degrees = -degrees;
        }
        encoderDrive(speed, degrees * actuators.INCHES_PER_ANGLE_INPLACE, -degrees * actuators.INCHES_PER_ANGLE_INPLACE, timeoutS);
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
        actuators.YFrontArm.setPower(0);
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
        actuators.YFrontArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        actuators.YFrontArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void setupVuforia(String AssetName , String TargetName)
    {
        //Seup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId); // To remove camera view from the scree, remove the R.
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the approprate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset(AssetName); // Ex. RelicVuMark
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS , 4);

        // Setup the target to be tracked
        target = visionTargets.get(0); // 0 corresponds to the wheels target
        target.setName(TargetName); // target name ex. RelicRecovery
        target.setLocation(createMatrix(0 , 500 , 0 , 90 , 0 , 90));

        //Set the phone location on robot
        phoneLocation = createMatrix(0 , 255 , 0 , 90 , 0 , 0);

        //Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation , parameters.cameraDirection);
    }

    //Creates a matrix for determining the locations and orientations of objects
    //Units are millimeters for x, y, and z, and degrees for u, v, and w
    public OpenGLMatrix createMatrix (float x , float y , float z , float u , float v , float w)
    {
        return OpenGLMatrix.translation(x , y , z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC , AxesOrder.XYZ , AngleUnit.DEGREES , u , v , w
                ));
    }

    //Formats a matrix into a readable string
    public String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }

    public int LeftBallColorDetect ()
    {

        int returner;
        //1 is left ball is red
        //-1 is left ball is blue
        returner = 0;

        if (sensors.LeftColorSensor.red() > sensors.RightColorSensor.red() && sensors.LeftColorSensor.blue() < sensors.RightColorSensor.blue())
        {
            //Left ball is red
            //Right ball is blue
            returner = 1;
        }
        else if (sensors.LeftColorSensor.red() < sensors.RightColorSensor.red() && sensors.LeftColorSensor.blue() > sensors.RightColorSensor.blue())
        {
            //Left Ball is blue
            //Right ball is red
            returner = -1;
        }
        else
        {
            //Failed to detect
            returner = 0;
        }


        return returner;
    }

    public void CylpherGraber (boolean Open)
    {
        if (Open)
        {
            actuators.LeftGlyphHolder.setPosition(0.325);
            actuators.RightGlyphHolder.setPosition(0.65);
        }
        else
        {
            actuators.LeftGlyphHolder.setPosition(1);
            actuators.RightGlyphHolder.setPosition(-0.1);
        }
    }
}
