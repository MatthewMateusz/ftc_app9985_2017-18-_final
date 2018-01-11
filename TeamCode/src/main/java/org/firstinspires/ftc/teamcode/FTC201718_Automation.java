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
    public static final double SPEED_NORMAL = 0.3;
    public static final double SPEED_SLOW   = 0.1;

    //Turn Constants
    public static final double SPEED_TURN_PLAT = 0.1;
    public static final double SPEED_TURN_TILE = 0.2;
    public static final double ANGLE_90   = 90.0;
    public static final double TURN_LEFT  = -ANGLE_90;
    public static final double TURN_RIGHT = ANGLE_90;

    //Timeout Constants
    public static final double TOUT_SHORT  = 3;
    public static final double TOUT_MEDIUM = 5;
    public static final double TOUT_LONG   = 10;

    //Variables to be used for later
    public VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer vuforia  = null;
    public VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
    public VuforiaTrackable target;
    public VuforiaTrackableDefaultListener listener;
    VuforiaTrackable relicTemplate = null;

    public OpenGLMatrix lastKnownLocation;
    public OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY = "AUYFfMb/////AAAAGeK9R/Mswk3ko4WgwY69fsB3D/KziaC/ZBui6bKvAUjjnhKoPyiDs0+TfVP3vMkYQ4Q0Amo4yosMAH9Xs0k+HX5MHGkhFbGLrDYj5zUN8NinByqcruRQZJuuISEHn1TfD5Fpa9psUmylGexAIwVB6WMfYTL2eKg4EE5mAaRsPgRKZnk/SjMzitYtthDxFusHftOK0N8vywIVSX79mBGmdy6+XUqLLa72zYXUvCrs9lov+xGuC06dUrmpFHl7uwt75QBVb5qyvbsruC4Bfnezzz1S747xiTHQz7Q86q1ZCix2V3AmxQxUuqhlYXDiC6uBseB3npuzuRtNxyCpn6+p3L1qv+Y1axec01BAOUATpSvy";

    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    //Declare OpMode data
    FTC201718_Actuators_Setup actuators = new FTC201718_Actuators_Setup();
    FTC201718_Sensors_Setups sensors = new FTC201718_Sensors_Setups();

    public ServoArm ServoArm = new ServoArm();
    public BlockGrabber BlockGrabber = new BlockGrabber();
    public Swing Swing = new Swing();

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

    public void encoderDrive4 ( double power, double FrontLeftEncoderTicks, double FrontRightEncoderTicks, double RearLeftEncoderTicks, double RearRightEncoderTicks, double timeoutS)
    {
        actuators.FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        actuators.RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newRearLeftTarget;
        int newRearRightTarget;

        //Ensures that the opmode is still active
        if (opModeIsActive())
        {
            idle();

            newFrontLeftTarget = actuators.FrontLeft.getCurrentPosition() + (int) (FrontLeftEncoderTicks);
            newFrontRightTarget = actuators.FrontRight.getCurrentPosition() + (int) (FrontRightEncoderTicks);
            newRearLeftTarget = actuators.RearLeft.getCurrentPosition() + (int) (RearLeftEncoderTicks);
            newRearRightTarget = actuators.RearRight.getCurrentPosition() + (int) (RearRightEncoderTicks);

            actuators.FrontLeft.setTargetPosition(newFrontLeftTarget);
            actuators.FrontRight.setTargetPosition(newFrontRightTarget);
            actuators.RearLeft.setTargetPosition(newRearLeftTarget);
            actuators.RearRight.setTargetPosition(newRearRightTarget);

            //Run to position turned on
            actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.FrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.RearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            actuators.RearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Reset the timeout and start motion
            runtime.reset();
            power = Range.clip(Math.abs(power) , 0.0 , 1.0);
            actuators.FrontLeft.setPower(power);
            actuators.FrontRight.setPower(power);
            actuators.RearLeft.setPower(power);
            actuators.RearRight.setPower(power);

            idle();

            //Keep log
            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (actuators.FrontLeft.isBusy() && actuators.FrontRight.isBusy() && actuators.RearLeft.isBusy() && actuators.RearRight.isBusy()) // && -> || --correction for the case of turn and drag
                    )
            {
                actuators.FrontLeft.setPower(power);
                actuators.FrontRight.setPower(power);
                actuators.RearLeft.setPower(power);
                actuators.RearRight.setPower(power);

                // Display it for the driver.
                telemetry.addData("Status", "encoderDrive4");
                telemetry.addData("Path1", "Running to %7d :%7d :%7d :%7d", newFrontLeftTarget , newFrontRightTarget , newRearLeftTarget , newRearRightTarget);
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
            idle();

            // Turn off RUN_TO_POSITION
            actuators.FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            actuators.FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            actuators.RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            actuators.RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            idle();
        }
    }

    public void encoderDriveDistance(double speed, double distanceInches, double timeoutS) {
        telemetry.addData("Status", "encoderDriveDistance");
        telemetry.update();
        if (speed < 0.0) {
            // speed for the encoderDrive(...) must be always positive!
            speed = -speed;
            distanceInches = -distanceInches;
        }
        double encoder_distance = distanceInches * FTC201718_Actuators_Setup.COUNTS_PER_INCHES;
        encoderDrive4(speed, encoder_distance, encoder_distance, encoder_distance, encoder_distance, timeoutS);
    }

    public void encoderTurnInPlace(double speed, double degrees, double timeoutS) {
        telemetry.addData("Status", "encoderTurnInPlace");
        telemetry.update();
        if (speed < 0.0) {
            // speed for the encoderDrive(...) must be always positive!
            speed = -speed;
            degrees = -degrees;
        }

        double encoder_distance = 0;
        if (degrees >= 0)
        {
            encoder_distance = degrees * actuators.COUNTS_PER_ANGLE_POS;
        }
        else
        {
            encoder_distance = degrees * actuators.COUNTS_PER_ANGLE_NEG;
        }


        encoderDrive4(speed , encoder_distance , -encoder_distance , encoder_distance , -encoder_distance , timeoutS);
    }


    public void encoderDriveAside(double speed, double inches, double timeoutS) {
        telemetry.addData("Status", "encoderTurnInPlace");
        telemetry.update();
        if (speed < 0.0) {
            // speed for the encoderDrive(...) must be always positive!
            speed = -speed;
            inches = -inches;
        }
        double encoder_distance = inches * actuators.COUNTS_PER_ASIDE;

        encoderDrive4(speed , encoder_distance , -encoder_distance , -encoder_distance , encoder_distance , timeoutS);
    }


    public void waitForStartAndDisplayWhileWaiting()
    {
        sensors.LeftColorSensor.enableLed(true);
        sensors.LeftColorSensor.enableLed(true);

        while (!isStarted() && !isStopRequested())
        {
            //insert telemetry data for sensors and other data
            telemetry.addData("Left Color:  " ,
                    "RED " + sensors.LeftColorSensor.red() +
                    "  GRN " + sensors.LeftColorSensor.green() +
                    "  BLU " + sensors.LeftColorSensor.blue());

          //  telemetry.addData("Right Color:  " ,
                   // "RED " + sensors.RightColorSensor.red() +
                  //  "  GRN " + sensors.RightColorSensor.green() +
                 //   "  BLU " + sensors.RightColorSensor.green());

            //Touch sensor data

            telemetry.update();
            idle();
        }
        sensors.LeftColorSensor.enableLed(false);
        sensors.LeftColorSensor.enableLed(false);
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

        sensors.LeftColorSensor.enableLed(false);
        //sensors.RightColorSensor.enableLed(false);
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
    /*
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
    */

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

    public int LeftBallColorDetectMultiSensor ()
    {
        sensors.LeftColorSensor.enableLed(true);
       // sensors.RightColorSensor.enableLed(true);

        int returner;
        //1 is left ball is red
        //-1 is left ball is blue
        returner = 0;

        //if (sensors.LeftColorSensor.red() > sensors.RightColorSensor.red() && sensors.LeftColorSensor.blue() < sensors.RightColorSensor.blue())
        //{
            //Left ball is red
            //Right ball is blue
            //returner = 1;
        //} else if (sensors.LeftColorSensor.red() < sensors.RightColorSensor.red() && sensors.LeftColorSensor.blue() > sensors.RightColorSensor.blue())
        //{
            //Left Ball is blue
            //Right ball is red
           // returner = -1;
        //} else
        {
            //Failed to detect
           // returner = 0;
        }

        sensors.LeftColorSensor.enableLed(false);
       // sensors.RightColorSensor.enableLed(false);
        return returner;
    }

        public int LeftBallColorDetectOneSensor ()
        {
            sensors.LeftColorSensor.enableLed(true);
            //sensors.RightColorSensor.enableLed(true);
            sleep(1000);

            int returner;
            //1 is left ball is red
            //-1 is left ball is blue
            returner = 0;

            if (sensors.LeftColorSensor.red() > sensors.LeftColorSensor.blue())
            {
                //Left ball is red
                //Right ball is blue
                returner = 1;
            }
            else if (sensors.LeftColorSensor.red() < sensors.LeftColorSensor.blue())
            {
                //Left Ball is blue
                //Right ball is red
                returner = -1;
            }
            else
            {
                //Failed to detect
                for (int i = 0; i <= 5 ; i++)
                {
                    double offSet = 0.025;
                    double currentPosition;
                    currentPosition = actuators.SwingArm.getPosition();
                    actuators.SwingArm.setPosition(currentPosition - offSet);
                    sleep(1000);

                    if (sensors.LeftColorSensor.red() > sensors.LeftColorSensor.blue())
                    {
                        //Left ball is red
                        //Right ball is blue
                        returner = 1;
                        break;
                    }
                    else if (sensors.LeftColorSensor.red() < sensors.LeftColorSensor.blue())
                    {
                        //Left Ball is blue
                        //Right ball is red
                        returner = -1;
                        break;
                    }
                    else
                    {
                        returner = 0;
                    }
                }
            }


            sensors.LeftColorSensor.enableLed(false);
            //sensors.RightColorSensor.enableLed(false);
            actuators.SwingArm.setPosition(0.5);
            sleep(1000);
            return returner;
        }

    public class BlockGrabber
    {


        public void waitForAction ()
        {
            sleep(1000);
        }

        public void open ()
        {
            actuators.LeftGlyphHolder.setPosition(actuators.BlockGabberLeft_OPEN);
            actuators.RightGlyphHolder.setPosition(actuators.BlockGabberRight_OPEN);
            waitForAction();
        }

        public void release ()
        {
            actuators.LeftGlyphHolder.setPosition(actuators.BlockGabberLeft_RELEASE);
            actuators.RightGlyphHolder.setPosition(actuators.BlockGabberRight_RELEASE);
            waitForAction();
        }


        public void close ()
        {
            actuators.LeftGlyphHolder.setPosition(actuators.BlockGabberLeft_CLOSE);
            actuators.RightGlyphHolder.setPosition(actuators.BlockGabberRight_CLOSE);
            waitForAction();
        }
    }


    public class Swing
    {
        public void waitForAction ()
        {
            sleep(1000);
        }
        public void waitForActionFast ()
        {
            sleep(500);
        }

        public void left() {
            actuators.SwingArm.setPosition(0);
            waitForAction();
        }

        public void center()
        {
            actuators.SwingArm.setPosition(0.5);
            waitForAction();
        }

        public void right() {
            actuators.SwingArm.setPosition(1);
            waitForAction();
        }

        public void offSet(double offSet)
        {
            double currentPosition = 0;
            currentPosition = actuators.SwingArm.getPosition();

            actuators.SwingArm.setPosition(currentPosition + offSet);
        }
    }

    public class ServoArm
    {



        public void waitForAction ()
        {
            sleep(1000);
        }

        public void down ()
        {
            actuators.ServoArm.setPosition(actuators.TailDown - 0.1);
            waitForAction();
            actuators.ServoArm.setPosition(actuators.TailDown);
            waitForAction();
        }

        public void up  ()
        {
            actuators.ServoArm.setPosition(actuators.TailUp);
            waitForAction();
        }
        public void mid ()
        {
            actuators.ServoArm.setPosition(0.5);
        }
    }


    public void LiftArmSecond(int Milliseconds)
    {
        runtime.reset();
        actuators.YFrontArm.setPower(0.5);
        while ( !sensors.limitArmUp.isPressed() && (runtime.milliseconds() <= Milliseconds) )
        {
            idle();
        }
        actuators.YFrontArm.setPower(0);
        sleep(1000);
    }

    public void ColorDetectMove (int CurrSide) // 0 is for blue and 1 is for red
    {
        int BallColor = LeftBallColorDetectOneSensor();

        if (CurrSide == 0)
        {
            if (BallColor == 1) //Left ball is red
            {
                Swing.left();
                ServoArm.mid();
                Swing.center();
                ServoArm.up();
            }
            else if (BallColor == -1) //Left ball is blue
            {
                Swing.right();
                ServoArm.mid();
                Swing.center();
                ServoArm.up();
            }
            else
            {
                telemetry.addData("AUTO: " , "Failed To Detect Color");
                ServoArm.up();
            }
        }
        else
        {
            if (BallColor == -1) //Left ball is red
            {
                Swing.left();
                ServoArm.mid();
                Swing.center();
                ServoArm.up();
            }
            else if (BallColor == 1) //Left ball is blue
            {
                Swing.right();
                ServoArm.mid();
                Swing.center();
                ServoArm.up();
            }
            else
            {
                telemetry.addData("AUTO: " , "Failed To Detect Color");
                ServoArm.up();
            }
        }
    }
}
