package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "BlueAuto Left Platform")
public class FTC201718_AutoBlueLeft extends FTC201718_Automation
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
        if (LeftBallColor == 1) //Left ball is red
        {
            encoderTurnInPlace(SPEED_TURN , 30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN , -30 , 3);
            encoderDriveDistance(0.2 , 3 , TOUT_SHORT);
            encoderTurnInPlace(SPEED_TURN , -90 , TOUT_SHORT);
            encoderDriveDistance(SPEED_NORMAL , 21 , TOUT_MEDIUM);
            encoderDriveAside(0.1 , 3.25  + OffSet, TOUT_LONG);
            encoderDriveDistance(0.2 , 7 , TOUT_LONG);
            CylpherGraber(0);
            encoderDriveDistance(SPEED_NORMAL , -3 , TOUT_SHORT);
        }
        else if (LeftBallColor == -1) //Left ball is blue
        {
            encoderTurnInPlace(SPEED_TURN , -30 , 3);
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN , 30 , 3);
            encoderDriveDistance(0.2 , 3 , TOUT_SHORT);
            encoderTurnInPlace(SPEED_TURN , -90 , TOUT_SHORT);
            encoderDriveDistance(SPEED_NORMAL , 21 , TOUT_MEDIUM);
            encoderDriveAside(0.1 , 3.25  + OffSet, TOUT_LONG);
            encoderDriveDistance(0.2 , 7 , TOUT_LONG);
            CylpherGraber(0);
            encoderDriveDistance(SPEED_NORMAL , -3 , TOUT_SHORT);
        }
        else
        {
            telemetry.addData("AUTO: " , "Failed To Detect Color");
            ServoArmDown(false);
            encoderTurnInPlace(SPEED_TURN , -90 , TOUT_SHORT);
            encoderDriveDistance(SPEED_NORMAL , 21 , TOUT_MEDIUM);
            encoderDriveAside(0.1 , 6.5  + OffSet, TOUT_LONG);
            encoderDriveDistance(0.2 , 7 , TOUT_LONG);
            CylpherGraber(0);
            encoderDriveDistance(SPEED_NORMAL , -3 , TOUT_SHORT);
        }


    }


}
