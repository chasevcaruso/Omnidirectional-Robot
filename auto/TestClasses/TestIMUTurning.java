package edu.elon.robotics.auto.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;

@Autonomous(name = "Test IMU Turning")
public class TestIMUTurning extends AutoCommon {

    private final double[]  ANGLE_SEQ = {90, -90, -180, 180, 45, -45,
            90, 90, 90, 90,
            -90, -90, -90, -90};
    private final double   SLOW_SPEED = 0.3;
    private final double MEDIUM_SPEED = 0.65;
    private final double   FAST_SPEED = 1.0;
    private final long    SHORT_PAUSE = 250;
    private final long     LONG_PAUSE = 3000;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        // do slow speed turning sequence
        for (double angle : ANGLE_SEQ) {
            telemetry.addData("Turning", angle + " at " + SLOW_SPEED + " speed");
            telemetry.update();
            turnIMU(angle, SLOW_SPEED);
            if (!opModeIsActive()) return;
            sleep(SHORT_PAUSE);
            System.out.println("[TURN_IMU] requested: " + angle + ", final: " + robot.getHeading());
        }

        // take a break
        sleep(LONG_PAUSE);

        // do medium speed turning sequence
        for (double angle : ANGLE_SEQ) {
            telemetry.addData("Turning", angle + " at " + MEDIUM_SPEED + " speed");
            telemetry.update();
            turnIMU(angle, MEDIUM_SPEED);
            if (!opModeIsActive()) return;
            sleep(SHORT_PAUSE);
            System.out.println("[TURN_IMU] requested: " + angle + ", final: " + robot.getHeading());
        }

        // take a break
        sleep(LONG_PAUSE);

        // do fast speed turning sequence
        for (double angle : ANGLE_SEQ) {
            telemetry.addData("Turning", angle + " at " + FAST_SPEED + " speed");
            telemetry.update();
            turnIMU(angle, FAST_SPEED);
            if (!opModeIsActive()) return;
            sleep(SHORT_PAUSE);
            System.out.println("[TURN_IMU] requested: " + angle + ", final: " + robot.getHeading());
        }

//        Turning calls to measure overshoot
//        turnIMU(90, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 90 + ", final: " + robot.getHeading());
//        turnIMU(90, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 90 + ", final: " + robot.getHeading());
//
//        turnIMU(45, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 45 + ", final: " + robot.getHeading());
//        turnIMU(45, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 45 + ", final: " + robot.getHeading());
//
//        turnIMU(-90, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 90 + ", final: " + robot.getHeading());
//        turnIMU(-90, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 90 + ", final: " + robot.getHeading());
//
//        turnIMU(-45, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 45 + ", final: " + robot.getHeading());
//        turnIMU(-45, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 45 + ", final: " + robot.getHeading());
//
//        turnIMU(180, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 180 + ", final: " + robot.getHeading());
//        turnIMU(180, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: R" + 180 + ", final: " + robot.getHeading());
//
//        turnIMU(-180, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 180 + ", final: " + robot.getHeading());
//        turnIMU(-180, FAST_SPEED);
//        if (!opModeIsActive()) return;
//        sleep(SHORT_PAUSE);
//        System.out.println("[TURN_IMU] requested: L" + 180 + ", final: " + robot.getHeading());


    }

}