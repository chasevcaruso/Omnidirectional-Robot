package edu.elon.robotics.auto.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "Test IMU Driving")
public class TestIMUDriving extends AutoCommon {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        //drive 400cm
        telemetry.addData("Driving", 400 + "cm at ");
        telemetry.update();
        driveIMU(400, 0.5);
        if (!opModeIsActive()) return;
        System.out.println("[DRIVE] requested: " + 400 + "cm, final: " + robot.convertTicksToDistance(robot.motorLeft.getCurrentPosition()));
    }
}
