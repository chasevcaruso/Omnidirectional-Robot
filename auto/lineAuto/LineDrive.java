package edu.elon.robotics.auto.lineAuto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "**LineDrive**")
public class LineDrive extends LineFollower {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        turnToCalibrateLightSensor();
        sleep(1000);

        lineDrive();
        sleep(3000);

    }
}