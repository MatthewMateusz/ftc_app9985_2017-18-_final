package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "CalAuto_WIP")
public class FTC201718_AutoCalibrate extends FTC201718_Automation
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
        // encoderDriveDistance(0.2, 24.0,  3.0);
        // encoderDriveDistance(0.2, -48.0, 5.0);
        // encoderTurnInPlace(0.2, 90.0, 10.0);
        //encoderTurnInPlace(0.2, 90.0, 10.0);
        //encoderTurnInPlace(0.2, 90.0, 10.0);
        //encoderTurnInPlace(0.2, 90.0, 10.0);
        // encoderTurnInPlace(0.2, -360.0, 10.0);
        //encoderDriveAside(0.2, 24.0, 10.0);

        /*
        BlockGrabber.open();
        sleep(5000);
        BlockGrabber.close();
        sleep(5000);
        BlockGrabber.release();
        sleep(5000);
        */


        telemetry.addData("Status" , "Complete");
    }
}
