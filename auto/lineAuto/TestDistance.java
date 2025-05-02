package edu.elon.robotics.auto.lineAuto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.Parking.Parking;

@Autonomous(name = "**Distance**")
public class TestDistance extends Parking {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        sensorTest();
        sleep(3000);

    }
}