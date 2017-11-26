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
    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;

    public ServoArm ServoArm = new ServoArm();
    public BlockGrabber BlockGrabber = new BlockGrabber();

    @Override
    public void runOpMode() throws InterruptedException
    {



        telemetry.addData("Status" , "Init the autonomous");
        telemetry.update();
        setupHardware();

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

        // Move ServoArm down and detect color and based on the color rotate
        BlockGrabber.close();
        LiftArmSecond(750);
        ServoArm.down();
        LeftBallColor = LeftBallColorDetectOneSensor();
        if (LeftBallColor == 1) //Left ball is red
        {
            encoderTurnInPlace(SPEED_TURN_PLAT , 30 , TOUT_LONG);
            ServoArm.up();
            encoderTurnInPlace(SPEED_TURN_PLAT , -30 , TOUT_LONG);
        }
        else if (LeftBallColor == -1) //Left ball is blue
        {
            encoderTurnInPlace(SPEED_TURN_PLAT , -30 , TOUT_LONG);
            ServoArm.up();
            encoderTurnInPlace(SPEED_TURN_PLAT , 30 , TOUT_LONG);
        }
        else
        {
            telemetry.addData("AUTO: " , "Failed To Detect Color");
            ServoArm.up();
        }
        encoderDriveDistance(SPEED_SLOW , 4 , TOUT_MEDIUM);
        encoderTurnInPlace(SPEED_TURN_TILE , -90 , TOUT_LONG);
        encoderDriveDistance(SPEED_NORMAL , 36 , TOUT_LONG);
        encoderDriveAside(SPEED_SLOW , 4 + OffSet , TOUT_LONG);
        encoderDriveDistance(SPEED_NORMAL , 9 , TOUT_MEDIUM);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_SLOW , -4 , TOUT_MEDIUM);
    }


}