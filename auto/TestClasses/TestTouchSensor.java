package edu.elon.robotics.auto.TestClasses;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "Test Touch Sensor")
public class TestTouchSensor extends AutoCommon {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        driveUntilTouch(0.5);
    }
}
