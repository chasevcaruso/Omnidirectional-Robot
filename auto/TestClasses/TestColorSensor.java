package edu.elon.robotics.auto.TestClasses;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "Test Color Sensor")
public class TestColorSensor extends AutoCommon {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        driveToCalibrateLightSensor(0.3);

        telemetry.addData("alpha", robot.colorSensor.alpha());
        telemetry.addData("max", robot.maxBrightness);
        telemetry.addData("min", robot.minBrightness);
        telemetry.update();
        System.out.println("Max: " + robot.maxBrightness);
        System.out.println("Min: " + robot.minBrightness);
        if (!opModeIsActive()) return;
        sleep(1000);
    }
}
