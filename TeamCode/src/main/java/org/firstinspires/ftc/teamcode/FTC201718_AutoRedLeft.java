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

    public int CurrSide = 1;
    public int OpNumber = 10;

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
        relicTrackables.activate();
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
        sleep(1000);
        double OffSet;
        int LeftBallColor;
        OffSet = 0;
        LeftBallColor = 0;

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "LEFT");
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "CENTER");
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "RIGHT");
        }
        else
        {
            telemetry.addData("SAW:" , "%s visible" , "UNKOWN");
        }

        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(850);
        ServoArm.down();
        ColorDetectMove(CurrSide);

        encoderDriveDistance(SPEED_NORMAL , -23, TOUT_MEDIUM);

        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            //Change value below for left column
            encoderDriveDistance(SPEED_NORMAL , -18, TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            //Change value below for middle column
            encoderDriveDistance(SPEED_NORMAL , -10, TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            //Change value below for right column
            encoderDriveDistance(SPEED_NORMAL , -2, TOUT_MEDIUM);
        }
        else
        {
            //Change value below for left column
            encoderDriveDistance(SPEED_NORMAL , -2 , TOUT_MEDIUM);
        }


        encoderTurnInPlace(SPEED_TURN_TILE , -90 , TOUT_LONG);
        encoderDriveDistance(SPEED_SLOW , 6 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_NORMAL , -5 , TOUT_MEDIUM);
        BlockGrabber.open();
    }


}
