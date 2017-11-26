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
            encoderTurnInPlace(SPEED_TURN , 30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN , 60 , 3);
            encoderDriveAside(0.3 , -2 , TOUT_SHORT);
            encoderDriveDistance(SPEED_NORMAL , 26 + OffSet, 3); //Changed
            encoderTurnInPlace( SPEED_TURN , 90 , 3);
            encoderDriveDistance(0.2 , 9 , 5);
            CylpherGraber(0);
            encoderDriveDistance(0.2 , -6 , 2);

        }
        else if (LeftBallColor == 1)
        {
            encoderTurnInPlace(SPEED_TURN , -30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN , -60 , 3);
            encoderDriveAside(0.1 , 4 , TOUT_SHORT);
            encoderDriveDistance(SPEED_NORMAL , -27 + OffSet , 3); //Changed
            encoderTurnInPlace( SPEED_TURN , -90 , 3);
            encoderDriveDistance(0.2 , 10 , 5);
            CylpherGraber(0);
            encoderDriveDistance(0.2 , -6 , 2);

        }
        else
        {
            telemetry.addData("Status: " , "Failed To Detect Color");
            ServoArmDown(false);
            encoderDriveDistance(0.1 , 4 , TOUT_SHORT);
            encoderTurnInPlace(SPEED_TURN , 90 , 3);
            encoderDriveDistance(SPEED_NORMAL , 28 + OffSet , 3); //Changes
            encoderTurnInPlace( SPEED_TURN , 90 , 3);
            encoderDriveDistance(0.2 , 9 , 5);
            CylpherGraber(0);
            encoderDriveDistance(0.2 , -6 , 2);
        }

    }


}
