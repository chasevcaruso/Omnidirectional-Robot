package edu.elon.robotics.auto;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * General autonomous methods.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import edu.elon.robotics.KiwiDriveRatio;
import edu.elon.robotics.RobotHardware;

public class AutoCommon extends LinearOpMode {

    protected RobotHardware robot;
    protected KiwiDriveRatio ratio;

    public final double RAMP_TICKS = 1000;
    public final double BASE_POWER = 0.15;

    // how many degrees to adjust the requested degree angle by
    private final double ANGLE_OVERSHOOT = 9.03;
    //this was calculated in sheets. Noticed larger angles mean robot had more momentum and would overshoot more because if this. this rate combats that.
    //private final double OVERSHOOT_RATE = 0.0304;
    // slow power of the motor for the final part of the turn
    private final double TURN_ENDING_POWER = 0.2;
    // number of degrees that will be done using the slow power
    private final double SLOW_DOWN_DEGREES = 15.0;


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RobotHardware(hardwareMap, true);
        ratio = new KiwiDriveRatio(true);
    }

//    protected void driveDistance(double cmForward, double cmSide, double maxPower) {
//
//    }

    protected void turnAngle(double degrees, double maxPower) {
        int ticksToTurn = robot.convertDegreesToTicks(degrees);
        int ttt = 1 * (ticksToTurn/Math.abs(ticksToTurn));
        robot.resetDriveEncoders();
        robot.startMove(0.0, 0.0, ttt*maxPower, 1.0);
        while (Math.abs(robot.motorLeft.getCurrentPosition()) < Math.abs(ticksToTurn) && opModeIsActive()) {
            // nothing here for now
        }
        robot.startMove(0.0, 0.0, 0.0, 1.0);
    }

//    protected void rampUp(int maxTicks, double maxPower, DcMotor motor){
//
//    }
//
//    protected void rampDown(int maxTicks, double maxPower, DcMotor motor){
//
//    }


    protected void driveDistance(double cmForward, double cmSide, double maxPower){
        double drive_cm = robot.convertDistanceToTicks(cmForward);
        double strafe_cm = robot.convertDistanceToTicks(cmSide);

        double distance = Math.sqrt(Math.pow(cmSide,2) + Math.pow(cmForward,2));
        double distInTicks = robot.convertDistanceToTicks(distance);

        double drive_power = cmForward * (maxPower / distance);
        double strafe_power = cmSide * (maxPower / distance);

        ratio.computeRatio(drive_power, strafe_power, 0.0);

        if (ratio.powerLeft > ratio.powerRight && ratio.powerLeft > ratio.powerAux){
            robot.resetDriveEncoders();
            robot.startMove(drive_power, strafe_power, 0.0, 1.0);
            while (Math.abs(robot.motorLeft.getCurrentPosition()) < Math.abs(distInTicks) && opModeIsActive()) {
                // nothing here for now
            }
            robot.startMove(0.0, 0.0, 0.0, 1.0);
        } else if (ratio.powerRight > ratio.powerAux) {
            robot.resetDriveEncoders();
            robot.startMove(drive_power, strafe_power, 0.0, 1.0);
            while (Math.abs(robot.motorRight.getCurrentPosition()) < Math.abs(distInTicks) && opModeIsActive()) {
                // nothing here for now
            }
            robot.startMove(0.0, 0.0, 0.0, 1.0);
        } else {
            robot.resetDriveEncoders();
            robot.startMove(drive_power, strafe_power, 0.0, 1.0);
            while (Math.abs(robot.motorAux.getCurrentPosition()) < Math.abs(distInTicks) && opModeIsActive()) {
                // nothing here for now
            }
            robot.startMove(0.0, 0.0, 0.0, 1.0);
        }
    }

    protected void turnIMU(double degrees, double power){
        // reset the yaw angle
        robot.imu.resetYaw();
        //double overshoot = ANGLE_OVERSHOOT + OVERSHOOT_RATE*Math.abs(degrees);
        // start the motors turning the robot (based on power/degrees)
        robot.startMove(0.0, 0.0, (Math.abs(power)*(degrees/Math.abs(degrees))), 1.0);
        // wait until the heading of the robot matches the desired heading
        //***when degrees = 180, doesnt stop because angle goes from 180 to -180 (no time to detect robot heading > degrees)
        while(Math.abs(robot.getHeading()) < Math.abs(degrees)-ANGLE_OVERSHOOT-SLOW_DOWN_DEGREES && opModeIsActive() ){
        }
        robot.startMove(0.0, 0.0, (TURN_ENDING_POWER*(degrees/Math.abs(degrees))), 1.0);
        while(Math.abs(robot.getHeading()) < Math.abs(degrees)-2.75 && opModeIsActive() ){
        }
        // stop the motors
        robot.startMove(0.0, 0.0, 0.0, 0.0);
    }

    protected void driveIMU(double cm, double power){
        //wheel power magnitude
        double POWER = Math.abs(power);
        double distInTicks = robot.convertDistanceToTicks(cm);
        //reset encoders, direction, and get heading
        robot.resetDriveEncoders();
        robot.imu.resetYaw();

        robot.startMove(POWER, 0.0, 0.0, 1.0);
        while(Math.abs(robot.motorLeft.getCurrentPosition()) < distInTicks && opModeIsActive() ){
            if (Math.abs(robot.getHeading()) > 0.5){
                robot.setAuxPower(3*robot.getHeading()/180);
            } else {
                robot.setAuxPower(0);
            }

        }

    }

    protected void pathDriveIMU(int maxLight, int minLight){
        //reset encoders, direction, and get heading
        robot.resetDriveEncoders();
        robot.imu.resetYaw();

        //figure out line threshold ***MIGHT DELETE
        int difference = maxLight - minLight;
        int threshold = difference / 4; //how much change to redirect movement

        //the rest
    }

    protected void driveUntilTouch(double power){
        robot.resetDriveEncoders();
        robot.startMove(power,0.0,0.0,1.0);
        while (!robot.touchSensor.isPressed()){
        }
        robot.startMove(0.0,0.0,0.0,1.0);
    }

    protected void driveToCalibrateLightSensor(double power){
        robot.colorSensor.enableLed(true);
        robot.maxBrightness = robot.colorSensor.alpha();
        robot.minBrightness = robot.colorSensor.alpha();

        robot.startMove(power,0.0,0.0,1.0);

        while(Math.abs(robot.motorLeft.getCurrentPosition()) < Math.abs(robot.convertDistanceToTicks(20)) && opModeIsActive()){
            int lightValue = robot.colorSensor.alpha();
            if (lightValue > robot.maxBrightness) { robot.maxBrightness = lightValue; }
            if (lightValue < robot.minBrightness) { robot.minBrightness = lightValue; }
        }
        robot.startMove(0.0,0.0,0.0,1.0);
        robot.colorSensor.enableLed(false);
    }


    //for regular lines
    protected int driveUntilTouchAndCount(double power){
        robot.resetDriveEncoders();
        robot.colorSensor.enableLed(true);
        robot.startMove(power,0.0,0.0,1.0);
        //variables to measure light
        int sensitivity = 2000;
        int numLines = 0;
        int avgLight = 0;
        boolean justCounted = false;
        while (!robot.touchSensor.isPressed()){
            int one = robot.colorSensor.alpha();
            int two = robot.colorSensor.alpha();
            int three = robot.colorSensor.alpha();
            avgLight = (one + two + three) / 3;
            if (!justCounted){
                if (avgLight > robot.minBrightness+sensitivity){
                    numLines += 1;
                    justCounted = true;
                    telemetry.addData("Lines: ", numLines);
                    telemetry.update();
                }
            } else if (avgLight < robot.minBrightness + sensitivity){
                justCounted = false;
            }
        }
        robot.startMove(0.0,0.0,0.0,1.0);
        robot.colorSensor.enableLed(false);
        return numLines;
    }

//    //for short dense lines
//    protected int driveUntilTouchAndCount(double power){
//        robot.resetDriveEncoders();
//        robot.colorSensor.enableLed(false);
//        robot.startMove(0.075,0.0,0.0,1.0);
//        //variables to measure light
//        int numLines = 0;
//        boolean justCounted = false;
//        while (!robot.touchSensor.isPressed()){
//            int lightVal = robot.colorSensor.alpha();
//            if (!justCounted){
//                if (lightVal >  robot.maxBrightness/2){
//                    numLines += 1;
//                    justCounted = true;
//                    telemetry.addData("Lines: ", numLines);
//                    telemetry.update();
//                }
//            } else if (lightVal < robot.maxBrightness/2){
//                justCounted = false;
//            }
//            System.out.println("LV: " + lightVal);
//        }
//        robot.startMove(0.0,0.0,0.0,1.0);
//        return numLines;
//    }

    protected void countLines(){
        //calibrate light sensor get range of light (black matt --> white tape)
        driveToCalibrateLightSensor(0.075);

        // <2000 = Black matt, >6000 = white tape
        boolean isHome = false;
        //call drive until touch
        int lineCount = driveUntilTouchAndCount(0.3);
        System.out.println("Line Count: " + lineCount);
        double distanceBackCM = 15.0 + robot.convertTicksToDistance(Math.abs(robot.motorLeft.getCurrentPosition()));
        driveDistance(-distanceBackCM, 0.0, 0.3);
    }


}
