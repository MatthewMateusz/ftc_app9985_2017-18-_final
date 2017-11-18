package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "RedAuto_WIP")
public class FTC201718_AutoRed extends FTC201718_Automation
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
        int LeftBallColor;
        LeftBallColor = 0;

        // Move ServoArm down and detect color and based on the color rotate
        CylpherGraber(1);
        sleep(1000);
        LiftArmSecond(750);
        sleep(500);
        ServoArmDown(true);
        sleep(1500);
        LeftBallColor = LeftBallColorDetectOneSensor();
        if (LeftBallColor == -1)
        {
            encoderTurnInPlace(0.5 , -30 , 3);
            //encoderDriveAside(1 , -8 , 3);
        }
        else
        {
            encoderTurnInPlace(0.5 , 30 , 3);
            //encoderDriveAside(1 , 8 , 3);
        }
        ServoArmDown(false);
    }


}
