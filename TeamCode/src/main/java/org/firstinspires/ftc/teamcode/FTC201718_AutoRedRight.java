package org.firstinspires.ftc.teamcode;

import com.google.blocks.ftcrobotcontroller.runtime.Block;
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

@Autonomous (name = "RedAuto Right Platform")
public class FTC201718_AutoRedRight extends FTC201718_Automation
{
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate = null;

    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;

    public int CurrSide = 1;
    public int OpNumber = 11;
    public int BlockOffset = 8;

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

        relicTrackables.activate();
        sleep(1000);
        //visionTargets.activate();

        //Add autonomous code here
        MoveAlpha();


        telemetry.addData("Status" , "Complete");
    }


    public void MoveAlpha ()
    {
        double OffSet;
        int LeftBallColor;
        LeftBallColor = 0;

        //Detect the 'images' on the side
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "LEFT");
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            OffSet = 7.45;
            telemetry.addData("SAW:" , "%s visible" , "CENTER");
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "RIGHT");
        }
        else
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%s visible" , "FAILED");
        }
        telemetry.update();

        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(750);
        ServoArm.down();
        ColorDetectMove(CurrSide);


        encoderDriveDistance(SPEED_NORMAL , -25, TOUT_MEDIUM);
        encoderTurnInPlace(SPEED_TURN_TILE , 90 , TOUT_LONG);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            encoderDriveDistance(SPEED_NORMAL , 16.5 , TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            encoderDriveDistance(SPEED_NORMAL , 10.5 , TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            encoderDriveDistance(SPEED_NORMAL , 6.5 , TOUT_MEDIUM);
        }
        else
        {
            encoderDriveDistance(SPEED_NORMAL , 6.5 , TOUT_MEDIUM);
        }

        encoderTurnInPlace(SPEED_TURN_TILE , 90 , TOUT_LONG);
        encoderDriveDistance(SPEED_NORMAL , 9 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_NORMAL , -5 , TOUT_MEDIUM);
        BlockGrabber.open();
    }


}
