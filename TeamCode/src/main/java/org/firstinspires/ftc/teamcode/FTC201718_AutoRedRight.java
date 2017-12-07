package org.firstinspires.ftc.teamcode;

import com.google.blocks.ftcrobotcontroller.runtime.Block;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "RedAuto Right Platform")
public class FTC201718_AutoRedRight extends FTC201718_Automation
{
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
        if (LeftBallColor == -1) //Left ball is red
        {
            Swing.left();
            Swing.center();
            ServoArm.up();
        }
        else if (LeftBallColor == 1) //Left ball is blue
        {
            Swing.right();
            Swing.center();
            ServoArm.up();
        }
        else
        {
            telemetry.addData("AUTO: " , "Failed To Detect Color");
            ServoArm.up();
        }

        encoderDriveDistance(SPEED_NORMAL , -12 , TOUT_MEDIUM);
        encoderDriveAside(SPEED_SLOW , -4 , TOUT_LONG);
        encoderDriveDistance(SPEED_NORMAL , -8 + OffSet, TOUT_MEDIUM);
        encoderDriveAside(SPEED_SLOW , -6 + OffSet , TOUT_LONG); // OffSet needs to be negative
        encoderTurnInPlace(SPEED_TURN_TILE , 180 , TOUT_LONG);
        encoderDriveDistance(SPEED_SLOW , 9 , TOUT_LONG);
        BlockGrabber.release();
        encoderDriveDistance(SPEED_NORMAL , -4 , TOUT_MEDIUM);
    }


}
