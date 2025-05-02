package edu.elon.robotics.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name="Timed Drive", group = "labs")
public class TimedDrive extends AutoCommon{

    @Override
    public void runOpMode() throws InterruptedException{
        super.runOpMode();

        waitForStart();
        //driveForTime(0.5,2500);
    }
}
