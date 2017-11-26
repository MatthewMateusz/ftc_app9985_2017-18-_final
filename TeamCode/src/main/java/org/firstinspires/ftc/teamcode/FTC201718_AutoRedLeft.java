package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "RedAuto Left Platform")
public class FTC201718_AutoRedLeft extends FTC201718_Automation
{
    public static final double ServoArm_Down = 0.7;
    public static final double ServoArm_Up   = 0;

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
        CylpherGraber(1);
        LiftArmSecond(750);
        ServoArmDown(true);
        LeftBallColor = LeftBallColorDetectOneSensor();
        if (LeftBallColor == -1)
        {
            encoderTurnInPlace(SPEED_TURN_PLAT , 30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN_PLAT , -30 , 3);
        }
        else if (LeftBallColor == 1)
        {
            encoderTurnInPlace(SPEED_TURN_PLAT , -30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN_PLAT , 60 , 3);
        }
        else
        {
            telemetry.addData("Status: " , "Failed To Detect Color");
            ServoArmDown(false);
            encoderDriveDistance(SPEED_SLOW , 4 , TOUT_MEDIUM);
        }
        encoderDriveDistance(SPEED_SLOW , 4 , TOUT_MEDIUM);
        encoderTurnInPlace(SPEED_TURN_PLAT , 90 , TOUT_MEDIUM);
        encoderDriveDistance(SPEED_NORMAL , 27 + OffSet , TOUT_LONG);
        encoderTurnInPlace(SPEED_TURN_TILE , 90 , TOUT_MEDIUM);
        encoderDriveDistance(SPEED_NORMAL , 9 , TOUT_MEDIUM);
        CylpherGraber(0.5);
        encoderDriveDistance(SPEED_SLOW , -4 , TOUT_MEDIUM);

    }


}
