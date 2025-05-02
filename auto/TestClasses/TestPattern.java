package edu.elon.robotics.auto.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "TestPattern")
public class TestPattern extends AutoCommon {

    private final double   SPEED = 0.3;
    private final long    SHORT_PAUSE = 250;
    private final long     LONG_PAUSE = 3000;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        //150cm forward
        telemetry.addData("Driving", 150 + "cm forward " + 0.0 + "cm strafe at " + SPEED + " speed");
        telemetry.update();
        driveDistance(150, 0.0, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //50cm backward
        telemetry.addData("Driving", 50 + "cm backward " + 0.0+ "cm strafe at " + SPEED + " speed");
        telemetry.update();
        driveDistance(-50,0.0,SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //counter clockwise turn 90 degrees
        telemetry.addData("Turning", 90 + " at " + SPEED + " speed");
        telemetry.update();
        turnAngle(-90, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //100cm forward
        telemetry.addData("Driving", 100 + "cm forward " + 0.0 + "cm strafe at " + SPEED + " speed");
        telemetry.update();
        driveDistance(100, 0.0, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //clockwise turn 70 degrees
        telemetry.addData("Turning", 70 + " at " + SPEED + " speed");
        telemetry.update();
        turnAngle(70, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //106.42cm backward
        telemetry.addData("Driving", 106.42 + "cm backward " + 0.0 + "cm strafe at " + SPEED + " speed");
        telemetry.update();
        driveDistance(-106.42, 0.0, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //counter clockwise turn 250 degrees
        telemetry.addData("Turning", 250 + " at " + SPEED + " speed");
        telemetry.update();
        turnAngle(-250, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //63.6cm forward
        telemetry.addData("Driving", 63.6 + "cm forward " + 0.0 + "cm strafe at " + SPEED + " speed");
        telemetry.update();
        driveDistance(63.6, 0.0, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        //counter clockwise turn 90 degrees
        telemetry.addData("Turning", 90 + " at " + SPEED + " speed");
        telemetry.update();
        turnAngle(-90, SPEED);
        if (!opModeIsActive()) return;
        sleep(SHORT_PAUSE);

        sleep(LONG_PAUSE);


    }

}