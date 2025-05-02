package edu.elon.robotics.auto.Parking;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.elon.robotics.auto.AutoCommon;
import edu.elon.robotics.auto.Parking.Parking;

@Autonomous(name = "**Park**")
public class ParkTest extends Parking {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();

        park();
        sleep(3000);
    }
}