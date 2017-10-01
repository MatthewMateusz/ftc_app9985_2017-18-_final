package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Matthew on 9/27/2017.
 */
@Disabled
@TeleOp (name = "TEMPLATE_Teleop")
public class TEMPLATE_TeleOp extends FTC201718_Telop_Setup
{


    //Code thar runs ONCE when the driver hits INIT
    @Override
    public void init()
    {

    }

    // Code to run REAPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop()
    {

    }

    //Code to run ONCE when the driver hits PLAY
    @Override
    public void start ()
    {
        telemetry.addData("Say" , "I bet you will win");
        telemetry.update();
    }

    //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop()
    {



    }

    //Code that runs when STOP is hit
    @Override
    public void stop()
    {

    }
}
