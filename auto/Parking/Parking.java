package edu.elon.robotics.auto.Parking;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import edu.elon.robotics.auto.AutoCommon;

public class Parking extends AutoCommon {


    public void sensorTest(){
        while (opModeIsActive()) {
            telemetry.addData("distance", robot.distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }

    public void moveUntilLine(double drive, double turn){
        robot.startMove(drive,0.0,turn,0.0);
        boolean keepRunning = true;
        while(keepRunning){
            if (robot.colorSensor.alpha() > robot.minBrightness + 3000){
                keepRunning = false;
            }
        }
        sleep(100);
        robot.startMove(0.0,0.0,0.0,0.0);
    }

    public void park(){
        //Calibrate light sensor
        driveToCalibrateLightSensor(0.2);

        //Drive to next white line
        boolean timeToTurn = false;
        robot.startMove(0.2,0.0,0.0,0.0);
        while (!timeToTurn){
            if (robot.colorSensor.alpha() > robot.minBrightness + 3000){
                timeToTurn = true;
            }
        }
        robot.startMove(0.0,0.0,0.0,0.0);

        //wait two seconds
        sleep(2000);

        //turn 90 degrees
        robot.resetDriveEncoders();
        robot.startMove(0.0,0.0,-0.2,0.0);
        while(robot.getHeading() < 84.4){
        }
        robot.startMove(0.0,0.0,0.0,0.0);
        sleep(500);//stop all momentum

        //move closer to the wall
        robot.startMove(0.0,0.2,0.0,0.0);
        while(robot.distanceSensor.getDistance(DistanceUnit.CM) > 30){
            //just get closer to the wall until it is good distance
        }
        robot.startMove(0.0,0.0,0.0,0.0);
        sleep(500);//stop all momentum

        //parking spot tracking variables
        double spaceBeg = -1.0;
        double spaceEnd = -1.0;
        double finSpaceBeg = -1.0;
        double finSpaceEnd = -1.0;
        boolean trackingSpot = false;
        boolean validSpot = false;
        double wallDepth = robot.distanceSensor.getDistance(DistanceUnit.CM);
        double backWallDepth = -1.0;
        double currentDepth;
        int spotCount = 0;
        int potentialSpots = 0;
        double lastSpaceLength = 0;
        boolean firstSpot = true;


        //drive until next white line and look for parking spot
        robot.startMove(0.25,0.0,0.0,0.0);
        robot.resetDriveEncoders();
        while(robot.colorSensor.alpha() < robot.minBrightness + 3000){
            //measure current distance from robot sensor
            currentDepth = robot.distanceSensor.getDistance(DistanceUnit.CM);
            System.out.println("Current Depth: " + currentDepth);

            if (currentDepth < wallDepth){
                wallDepth = currentDepth;
            }

            //update initial depth if measuring same wall
            //if (currentDepth < initialDepth + 10) { initialDepth = currentDepth; }

            //check to see if parking spot has begun
            if (!trackingSpot && currentDepth > wallDepth+3) {
                spaceBeg = robot.convertTicksToDistance(robot.motorRight.getCurrentPosition());
                trackingSpot = true;
                potentialSpots += 1;
            }

            //update backwall depth
            if (currentDepth > backWallDepth){
                backWallDepth = currentDepth;
            }

            //check to see if parking spot has ended and it is valid
            if (trackingSpot){
                //see if parking spot ended/obstructed, save variables and set booleans appropriately
                if (currentDepth < (wallDepth * 1.5)) {
                    spaceEnd = robot.convertTicksToDistance(robot.motorRight.getCurrentPosition());
                    System.out.println("Space Beginning: " + spaceBeg);
                    System.out.println("Space End: " + spaceEnd);
                    if (((spaceEnd - spaceBeg) > 35) && ((backWallDepth - wallDepth) > 35)){
                        validSpot = true;
                        spotCount += 1;
                        telemetry.addData("VALID SPOT", spotCount);
                        telemetry.update();
                        //save spot locations
                        if (firstSpot){
                            lastSpaceLength = spaceEnd-spaceBeg;
                            finSpaceBeg = spaceBeg;
                            finSpaceEnd = spaceEnd;
                            firstSpot = false;
                        } else {
                            if (lastSpaceLength > (spaceEnd-spaceBeg)){
                                finSpaceBeg = spaceBeg;
                                finSpaceEnd = spaceEnd;
                                System.out.println("LSL: " + lastSpaceLength);
                                System.out.println("CSL: " + (spaceEnd-spaceBeg));
                            }
                        }

                    }
                    trackingSpot = false;
                }
            }
        }
        System.out.println("Wall Depth: " + wallDepth);
        System.out.println("Potential Spots: " + potentialSpots);
        System.out.println("Spot Count: " + spotCount);
        robot.startMove(0.0,0.0,0.0,0.0);
        sleep(300);

        //code to make the car go back
        System.out.println("Space Beginning: " + spaceBeg);
        System.out.println("Space End: " + spaceEnd);
        System.out.println("Space: " + finSpaceBeg + ", " + finSpaceEnd);
        System.out.println("END: " + robot.convertTicksToDistance(robot.motorRight.getCurrentPosition()));
        if (validSpot){
            //backup to spot
            robot.startMove(-0.25,0.0,0.0,0.0);
            while(robot.convertTicksToDistance(robot.motorRight.getCurrentPosition()) > finSpaceBeg + 25){
                //move backwards
            }

            //strafe into spot
            robot.startMove(0.0,0.0,0.0,0.0);
            sleep(200);
            robot.startMove(0.0,0.25,0.0,0.0);
            while(robot.distanceSensor.getDistance(DistanceUnit.CM) > 20){
            }
        }
        robot.startMove(0.0,0.0,0.0,0.0);
    }
}
