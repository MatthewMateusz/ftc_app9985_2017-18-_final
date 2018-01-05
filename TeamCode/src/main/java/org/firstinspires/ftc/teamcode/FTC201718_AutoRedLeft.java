package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "RedAuto Left Platform")
public class FTC201718_AutoRedLeft extends FTC201718_Automation
{
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia  = null;
    VuforiaTrackable relicTemplate = null;

    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;

    public ServoArm ServoArm = new ServoArm();
    public BlockGrabber BlockGrabber = new BlockGrabber();
    public Swing Swing = new Swing();

    @Override
    public void runOpMode() throws InterruptedException
    {



        telemetry.addData("Status" , "Init the autonomous");
        telemetry.update();
        setupHardware();

        // Start Vuforia Setup A1 (this one is going to be a long one)
        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        // End of Vuforia Setup A1

        //Add processes for Init
        //setupVuforia("RelicVuMark" , "RelicRecovery");

        //We don't know where the robot is, so set it to the orgin
        //If we don't include this, it would be null, which would cause errors later on
        //lastKnownLocation = createMatrix( 0 , 0 , 0 , 0 , 0 , 0);

        telemetry.addData("Status" , "Finished Init");
        telemetry.update();

        waitForStartAndDisplayWhileWaiting();
        telemetry.addData("Status" , "Started");
        telemetry.update();

        //visionTargets.activate();

        //Add autonomous code here
        MoveAlpha();


        telemetry.addData("Status" , "Complete");
    }


    public void MoveAlpha ()
    {
        double OffSet;
        int LeftBallColor;
        OffSet = 0;
        LeftBallColor = 0;

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            OffSet = 0;
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            OffSet = 0;
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            OffSet = 0;
        }

        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(750);
        ServoArm.down();
        LeftBallColor = LeftBallColorDetectOneSensor();
        if (LeftBallColor == -1)
        {
            Swing.left();
            Swing.center();
            ServoArm.up();
        }
        else if (LeftBallColor == 1)
        {
            Swing.right();
            Swing.center();
            ServoArm.up();
        }
        else
        {
            telemetry.addData("Status: " , "Failed To Detect Color");
            ServoArm.up();
        }

        encoderDriveAside(SPEED_SLOW , 4 , TOUT_LONG);
        encoderDriveDistance(SPEED_NORMAL , -12 , TOUT_MEDIUM);
        encoderDriveDistance(SPEED_NORMAL , -13 + OffSet , TOUT_MEDIUM); //OffSet needs to be negative
        encoderTurnInPlace(SPEED_TURN_TILE , -90 , TOUT_LONG);
        encoderDriveDistance(SPEED_SLOW , 12 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_NORMAL , -5 , TOUT_MEDIUM);
    }


}
