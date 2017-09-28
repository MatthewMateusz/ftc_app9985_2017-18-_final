package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew on 9/27/2017.
 */

@Autonomous (name = "TEMPLATE_Auto")
public class TEMPLATE_autonomous extends FTC201718_Automation
{

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status" , "Init the autonomous");
        telemetry.update();

        //Add processes for Init

        telemetry.addData("Status" , "Finished Init");
        telemetry.update();

        waitForStartAndDisplayWhileWaiting();
        telemetry.addData("Name" , "Started");
        telemetry.update();

        //Add autonomous code here

        telemetry.addData("Name" , "Complete");
        telemetry.update();
    }
}
