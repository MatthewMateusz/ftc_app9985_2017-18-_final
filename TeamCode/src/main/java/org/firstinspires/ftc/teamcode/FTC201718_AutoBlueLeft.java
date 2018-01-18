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

@Autonomous (name = "BlueAuto Left Platform")
public class FTC201718_AutoBlueLeft extends FTC201718_Automation
{
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia  = null;
    VuforiaTrackable relicTemplate = null;


    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;

    public int CurrSide = 0;
    public int OpNumber = 0;

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

        //Detect the 'images' on the side
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark == RelicRecoveryVuMark.LEFT)
        {
            telemetry.addData("SAW:" , "%string" , "LEFT");
            OffSet = 0;
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            telemetry.addData("SAW:" , "%string" , "CENTER");
            OffSet = 0;
        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            telemetry.addData("SAW:" , "%string" , "RIGHT");
            OffSet = 0;
        }
        else
        {
            telemetry.addData("SAW:" , "%string" , "FAILED");
            OffSet = 0;
        }
        telemetry.update();
        sleep(1000);

        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(750);
        ServoArm.down();
        ColorDetectMove(CurrSide);

        encoderDriveDistance(SPEED_NORMAL , 25 , TOUT_MEDIUM);
        encoderTurnInPlace(SPEED_TURN_TILE , 90 , TOUT_MEDIUM);
        encoderDriveDistance(SPEED_NORMAL , 2 + OffSet , TOUT_MEDIUM);
        encoderTurnInPlace(SPEED_NORMAL , 90 , TOUT_MEDIUM);
        encoderDriveDistance(SPEED_SLOW , 11 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_NORMAL , -5 , TOUT_MEDIUM);
        BlockGrabber.open();
    }



}