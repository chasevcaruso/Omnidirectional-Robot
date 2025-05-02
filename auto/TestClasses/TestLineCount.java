package edu.elon.robotics.auto.TestClasses;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "Test Line Count")
public class TestLineCount extends AutoCommon {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        countLines();
        sleep(3000);

    }
}