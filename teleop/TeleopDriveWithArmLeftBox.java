package edu.elon.robotics.teleop;

/**
 * Manually drive the robot using the game controller.
 *
 * @author J. Hollingsworth
 */

/*
 * Notice there is one main loop (in runOpMode) that loops until the user
 * presses stop. More than likely that should be the only loop in this
 * program. You should also avoid sleeps anywhere in the main
 * loop. Sleeps and other loops will slow down how fast the main loop is
 * able to iterate making the robot less responsive. In other words, while
 * your program is waiting in a sleep, the user may want to drive the robot
 * and will not be able to until the sleep is finished.
 *
 * Notice how this code uses an ElapsedTime timer to figure out that a
 * certain amount of time has passed (instead of using a sleep).
 *
 * Notice the use of a Finite State Machine (FSM) to automate the arm
 * movements (the enum lists the possible states, you can add more or
 * change what they do).
 *
 * Notice the use of the wasPressed boolean variables to treat each
 * button press as a single button press. Without that variable the
 * main loop may be fast enough to register a single button press from
 * the user as multiple presses. This logic says that the user must
 * release the button before the next press of that button is read.
 * The other option is to hold the button until something should stop.
 * Both options for reading user button presses are useful.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.elon.robotics.ControlledServo;
import edu.elon.robotics.RobotHardware;

@TeleOp(name = "DRWA Left", group = "TeleOp")
public class DriveWithArmLeftBox extends LinearOpMode {

    // declare a variable that will represent the robot hardware (i.e., the robot)
    private RobotHardware robot;

    // helper classes for controlling servos
    ControlledServo shoulder, elbow, gripper;

    @Override
    public void runOpMode() throws InterruptedException {

        // instantiate the robot (pass the hardware configuration)
        robot = new RobotHardware(hardwareMap, false);

        // wrap our servos within a ControlledServo
        shoulder = new ControlledServo(robot.servoShoulder, "shoulder", 0.5, 0.15, 0.77);
        elbow    = new ControlledServo(robot.servoElbow, "elbow", 0.5, 0.0, 1.0);
        gripper  = new ControlledServo(robot.servoGripper, "gripper", GRIPPER_OPEN, 0.15, 0.55);

        // move servos to their initial positions
        shoulder.update();
        elbow.update();
        gripper.update();

        // pause to allow the loading of a cube (this sleep is ok, not in main loop)
        sleep(2000);
        gripper.setPosition(GRIPPER_CLOSED);
        gripper.update();

        telemetry.addData("status", "robot is ready!");
        telemetry.update();

        // block and wait until start is pressed
        waitForStart();

        //autonomous teleop
        autonomousScore();

        // controlled teleop
        while (opModeIsActive()){
            stickDriving();
            controlArm();
        }
    }

    private boolean wasAPressed = false;
    private boolean wasBPressed = false;
    private boolean wasXPressed = false;
    private boolean wasYPressed = false;

    private boolean wasRBPressed = false;
    private double power = 0.5;

    private enum ArmMovement {PAUSED, MOVE_UP, MOVE_DOWN_FOR_PICKUP, GRAB, OPEN, DUCK_DROP}
    private ArmMovement armMovement = ArmMovement.PAUSED;
    private ElapsedTime armTimer = new ElapsedTime();

    private final double SHOULDER_UP = 0.42;
    private final double SHOULDER_DOWN = 0.15;
    private final double ELBOW_UP = 0.65;
    private final double ELBOW_DOWN = 0.92;
    private final double GRIPPER_OPEN = 0.26;
    private final double GRIPPER_CLOSED = 0.52;
    private final double DUCK_ELBOW = 0.44;
    private final double DUCK_SHOULDER = 0.17;

    private final long UP_MOVE_TIME = 2000;  // 2 seconds

    public void autonomousScore(){
        controlArm();
        armMovement = armMovement.MOVE_UP;
        armMovement = armMovement.OPEN;
        sleep(500);
        armMovement = armMovement.GRAB;
        sleep(100);

        //turn, strafe before duck
        robot.startMove(-0.095,0.0,0.1,0);
        while (robot.getHeading() > -78 && opModeIsActive()) {
        }
        robot.startMove(0,0.12,0,0);
        while (robot.motorLeft.getCurrentPosition() > -195 && opModeIsActive()){
        }

        //reset driver encoders and drive forward to the thingy
        sleep(50);
        robot.resetDriveEncoders();
        robot.startMove(0.1,-0.0285,0,0);
        while (robot.motorLeft.getCurrentPosition() > -490 && opModeIsActive()){
        }
        robot.startMove(0,0,0,0);

        controlArm();
        armMovement = armMovement.DUCK_DROP;
        controlArm();
        sleep(1000);
        armMovement = armMovement.OPEN;
        controlArm();
        sleep(1000);
        robot.servoShoulder.setPosition(SHOULDER_UP);
        controlArm();

        //backup
        robot.startMove(-0.1,0,0,0);
        while (robot.motorLeft.getCurrentPosition() < -460 && opModeIsActive()){
        }
        robot.startMove(0,0,0,0);


    }

    public void controlArm() {
        // what is the player asking us to do?
        if (gamepad1.right_bumper && !wasRBPressed) {
            power = 1.0;
            wasRBPressed = true;
        } else if (gamepad1.right_bumper && wasRBPressed){
            power = 0.4;
            wasRBPressed = false;
        }

        if (gamepad1.a && !wasAPressed) {
            armMovement = ArmMovement.MOVE_DOWN_FOR_PICKUP;
        }
        wasAPressed = gamepad1.a;

        if (gamepad1.b && !wasBPressed) {
            armMovement = ArmMovement.MOVE_UP;
            armTimer.reset();
        }
        wasBPressed = gamepad1.b;

        if (gamepad1.x && !wasXPressed) {
            armMovement = ArmMovement.GRAB;
        }
        wasXPressed = gamepad1.x;

        if (gamepad1.y && !wasYPressed) {
            armMovement = ArmMovement.OPEN;
        }
        wasYPressed = gamepad1.y;


        // perform the requested action (FSM)
        switch (armMovement) {
            case MOVE_DOWN_FOR_PICKUP:
                shoulder.setPosition(SHOULDER_DOWN);
                elbow.setPosition(ELBOW_DOWN);
                gripper.setPosition(GRIPPER_OPEN);
                armMovement = ArmMovement.PAUSED;
                break;
            case MOVE_UP:
                shoulder.setPosition(SHOULDER_UP);
                elbow.setPosition(ELBOW_UP);
                break;
            case GRAB:
                gripper.setPosition(GRIPPER_CLOSED);
                break;
            case OPEN:
                gripper.setPosition(GRIPPER_OPEN);
                break;

            case DUCK_DROP:
                elbow.setPosition(DUCK_ELBOW);
                shoulder.setPosition(DUCK_SHOULDER);
                elbow.update();
                shoulder.update();
                break;
        }

        // update all of the servo positions
        shoulder.update();
        elbow.update();
        gripper.update();
    }

    public void stickDriving() {
        /*
         * Read the gamepad joysticks and use that information
         * to drive the robot.
         */
        double forward  = gamepad1.right_trigger*0.9;
        double reverse  = -gamepad1.left_trigger*0.6;
        double drive = forward+reverse;
        //double strafe = -gamepad1.left_stick_x;
        double turn   = gamepad1.left_stick_x*0.4;

        /*
         * Telemetry shows up at the bottom of the
         * drive station. It's a good way to help
         * you debug your code.
         */
        telemetry.addData("drive", drive);
        telemetry.addData("strafe", 0);
        telemetry.addData("turn", turn);

        // call startMove to move the robot
        robot.startMove(drive*power, 0, turn, 0.5);
    }
}

//duck drop
//elbow: .44
//shoulder: .17
//regular drop
//
//