package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
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
    public void setupVuforia()
    {
        //Seup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId); // To remove camera view from the scree, remove the R.
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the approprate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("RelicVuMark");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS , 4);

        // Setup the target to be tracked
        target = visionTargets.get(0); // 0 corresponds to the wheels target
        target.setName("RelicRecovery"); // target name
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
}
