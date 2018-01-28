package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.OutputFinisher;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "BlueAuto Right Platform")
public class FTC201718_AutoBlueRight extends FTC201718_Automation
{
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia  = null;
    VuforiaTrackable relicTemplate = null;

    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;
    public static final double BlockOffset = 8;

    public int CurrSide = 0;
    public int OpNumber = 1;

    @Override
    public void runOpMode() throws InterruptedException
    {



        telemetry.addData("Status" , "Init the autonomous");
        telemetry.update();
        setupHardware();

        // Start Vuforia Setup A1 (this one is going to be a long one)
        parameters.vuforiaLicenseKey = "AUYFfMb/////AAAAGeK9R/Mswk3ko4WgwY69fsB3D/KziaC/ZBui6bKvAUjjnhKoPyiDs0+TfVP3vMkYQ4Q0Amo4yosMAH9Xs0k+HX5MHGkhFbGLrDYj5zUN8NinByqcruRQZJuuISEHn1TfD5Fpa9psUmylGexAIwVB6WMfYTL2eKg4EE5mAaRsPgRKZnk/SjMzitYtthDxFusHftOK0N8vywIVSX79mBGmdy6+XUqLLa72zYXUvCrs9lov+xGuC06dUrmpFHl7uwt75QBVb5qyvbsruC4Bfnezzz1S747xiTHQz7Q86q1ZCix2V3AmxQxUuqhlYXDiC6uBseB3npuzuRtNxyCpn6+p3L1qv+Y1axec01BAOUATpSvy";

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

        //Add autonomous code here
        MoveAlpha();


        telemetry.addData("Status" , "Complete");
    }


    public void MoveAlpha ()
    {
        double OffSet;
        int LeftBallColor;
        OffSet = 0;

        //Detect the 'images' on the side
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            OffSet = 0;
            telemetry.addData("SAW:" , "%string" , "LEFT");
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            OffSet = BlockOffset;
            telemetry.addData("SAW:" , "%string" , "CENTER");
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            OffSet = (BlockOffset * 2);
            telemetry.addData("SAW:" , "%s visible" , "RIGHT");
        }
        else
        {
            OffSet = 0;

        }

        sleep(1000);


        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(750);
        ServoArm.down();
        ColorDetectMove(CurrSide);

        encoderDriveDistance(SPEED_NORMAL , 28.25 , TOUT_MEDIUM);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            //Change value below for left column
            encoderDriveDistance(SPEED_NORMAL , 0 , TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            //Change value below for middle column
            encoderDriveDistance(SPEED_NORMAL , 0 , TOUT_MEDIUM);
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            //Change value below for right column
            encoderDriveDistance(SPEED_NORMAL , 0 , TOUT_MEDIUM);
        }
        else
        {
            //Change value below for left column
            encoderDriveDistance(SPEED_NORMAL , 0 , TOUT_MEDIUM);
        }

        encoderTurnInPlace(SPEED_TURN_TILE , -93 , TOUT_LONG);
        encoderDriveDistance(SPEED_SLOW ,12 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_SLOW , -5 , TOUT_MEDIUM);
        BlockGrabber.open();
    }


}
