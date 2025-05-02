package edu.elon.robotics.teleop;

/**
 * Manually drive the arm using the game controller.
 *
 * @author J. Hollingsworth
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.elon.robotics.RobotHardware;
import edu.elon.robotics.ControlledServo;

@TeleOp(name = "Servo Cycle")
public class ServoCycle extends LinearOpMode {

    private RobotHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {

        // instantiate the robot (pass the hardware configuration)
        robot = new RobotHardware(hardwareMap, false);

        /*
         * You should find min/max values for all of your servos.
         * These value are used to keep the servos from breaking
         * the arm or burning out the servo.
         *
         * The assumption is that 0.5 is a good starting position.
         * You could find a different initial position if you want.
         */
        // define the arm servos
        ControlledServo[] servos = {
                new ControlledServo(robot.servoShoulder, "shoulder", 0.5, 0.15, 0.85),
                new ControlledServo(robot.servoElbow, "elbow", 0.5, 0.0, 1.0),
                new ControlledServo(robot.servoGripper, "gripper", 0.5, 0.15, 0.55)
        };
        int which_servo = 0;

        // set all servos to their initial position
        for (ControlledServo servo : servos) {
            servo.update();
        }

        // how much do we change by for every button press?
        double[] deltas = {0.1, 0.05, 0.01};
        int which_delta = 0;


        // block and wait until start is pressed
        waitForStart();


        // toggle variables
        boolean wasAPressed = false;
        boolean wasYPressed = false;
        boolean wasDpadUpPressed = false;
        boolean wasDpadDownPressed = false;

        while (opModeIsActive()){
            // switch servos
            if (gamepad1.a && !wasAPressed) {
                which_servo = (which_servo + 1) % servos.length;
            }
            wasAPressed = gamepad1.a;

            // switch deltas
            if (gamepad1.y && !wasYPressed) {
                which_delta = (which_delta + 1) % deltas.length;
            }
            wasYPressed = gamepad1.y;

            // increase position of current servo
            if (gamepad1.dpad_up && !wasDpadUpPressed) {
                servos[which_servo].changePositionBy(deltas[which_delta]);
            }
            wasDpadUpPressed = gamepad1.dpad_up;

            // decrease position of current servo
            if (gamepad1.dpad_down && !wasDpadDownPressed) {
                servos[which_servo].changePositionBy(-deltas[which_delta]);
            }
            wasDpadDownPressed = gamepad1.dpad_down;


            // set all servos to their current position
            for (ControlledServo servo : servos) {
                servo.update();
            }

            // display instructions
            telemetry.addData("A", "cycle thru servos");
            telemetry.addData("Y", "cycle thru change values (delta)");
            telemetry.addData("DPad Up", "increase position value");
            telemetry.addData("DPad Down", "decrease position value");
            telemetry.addData("delta", deltas[which_delta]);
            telemetry.addData("", "------------------------------------------");

            // display servo information
            for (int i = 0; i < servos.length; i++) {
                if (which_servo == i) {
                    telemetry.addData("", "[[current]] " + servos[i]);
                } else {
                    telemetry.addData("", servos[i]);
                }
            }
            telemetry.update();
        }
    }
}