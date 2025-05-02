package edu.elon.robotics.auto.lineAuto;

import com.qualcomm.robotcore.util.ElapsedTime;

import edu.elon.robotics.auto.AutoCommon;


public class LineFollower extends AutoCommon {

    protected void turnToCalibrateLightSensor(){
        robot.colorSensor.enableLed(true);
        int turnAngle = robot.convertDegreesToTicks(45);
        robot.maxBrightness = robot.colorSensor.alpha();
        robot.minBrightness = robot.colorSensor.alpha();
        double origin = robot.getHeading();

        //measure to the left
        robot.startMove(0.0,0.0, -0.2,1.0);
        while(robot.motorLeft.getCurrentPosition() > -turnAngle && opModeIsActive()){
            int lightValue = robot.colorSensor.alpha();
            if (lightValue > robot.maxBrightness) { robot.maxBrightness = lightValue; }
            if (lightValue < robot.minBrightness) { robot.minBrightness = lightValue; }
            telemetry.addData("LV: ", lightValue);
            telemetry.update();
            System.out.println("LEFT: " + lightValue);
        }
        robot.startMove(0.0,0.0, 0.0,1.0);
        //measure to the right
        robot.startMove(0.0,0.0, 0.2,1.0);
        while(robot.motorLeft.getCurrentPosition() < turnAngle && opModeIsActive()){
            int lightValue = robot.colorSensor.alpha();
            if (lightValue > robot.maxBrightness) { robot.maxBrightness = lightValue; }
            if (lightValue < robot.minBrightness) { robot.minBrightness = lightValue; }
            telemetry.addData("LV: ", lightValue);
            telemetry.update();
            System.out.println("RIGHT: " + lightValue);
        }
        robot.startMove(0.0,0.0, 0.0,1.0);
        robot.startMove(0.0,0.0,-0.3,1.0);
        while (robot.getHeading() < origin - 5){
            int lightValue = robot.colorSensor.alpha();
            System.out.println("ORIGIN: " + lightValue);
        }
        robot.startMove(0.0,0.0,0.0,1.0);
        robot.colorSensor.enableLed(false);
    }

//    //P-Controller Version
//    protected void lineDrive() {
//        resetRuntime();
//        int maxLight = robot.maxBrightness;
//        int minLight = robot.minBrightness;
//        System.out.println(maxLight + ", " + minLight);
//        //set point (average of darkest and brightest)
//        double sp = (maxLight + minLight) / 2.0;
//
//        //calculate kp
//        //double kp = 0.00021421; // <-- good initially. caluclated w/ real light values "(1.0 / ((4135 - 1334) / 2.0)) * 0.3" --> 0.3 is value of max turn speed
//        double kp = 0.000065; //oscillates noticably but at 0.5 not noticable (looks like straight line)
//
//        //loop --> measure light, calculate error, calculate turn speed, startMove
//        while (opModeIsActive()) {
//            //process variable (current light value)
//            double pv = robot.colorSensor.alpha();
//
//            //calculate error (roughly a value in the thousands)
//            double error = sp - pv;
//
//            //determine turn speed
//            double turn = kp * error;
//
//            //startMove
//            robot.startMove(0.2, 0.0, turn, 0.0);
//
//            //oscillation plot stuff
//            //System.out.println(pv + " " + time);
//        }

    //PID-Controller Version
    protected void lineDrive() {
        resetRuntime();
        int maxLight = robot.maxBrightness;
        int minLight = robot.minBrightness;
        System.out.println(maxLight + ", " + minLight);
        //set point (average of darkest and brightest)
        double sp = (maxLight + minLight) / 2.0;

        //calculate kc + kp (4.24/13 osc)
        //double kc = 0.0000575; //oscillates noticably but at 0.5 not noticable (looks like straight line)
        double kp = 0.000132;  //^ for agressive response
        double ki = 0.00175235;   //^ for ?? ()0.00175
        double kd = 0.000005208;//^ for more twitchyness
        double dT = 0.03;

        //sumError and previousError
        double sumError = 0;
        double prevError = 0;

        // create only one loop timer
        ElapsedTime loopTimer = new ElapsedTime();

        // main PID loop
        while (opModeIsActive()) {
            loopTimer.reset();   // restart the timer at 0

            //***PID CALCULATIONS***
            //process variable (current light value)
            double pv = robot.colorSensor.alpha();

            //P
            double error = sp - pv;
            double p = kp * error;
            //I
            sumError = 0.8 * sumError + error;
            double i = ki * sumError * dT;
            //D
            double d = kd * ((error - prevError) / dT);
            prevError = error;

            //PID
            double turn = p + i + d;


            //startMove
            robot.startMove(0.113, 0.0, turn, 0.0);
            //***PID CALCULATIONS***


            //oscillation plot stuff
            System.out.println(pv + " " + time);
            // force the loop to take 30ms total
            if (Math.round(loopTimer.milliseconds()) <= 30){
                sleep(30 - Math.round(loopTimer.milliseconds()));
            }
        }
    }
}
