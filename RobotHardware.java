package edu.elon.robotics;

/**
 * Defines the robot hardware and implements a few
 * fundamental methods.
 */

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


public class RobotHardware {

    // drive motors
    public DcMotor motorLeft;
    public DcMotor motorRight;
    public DcMotor motorAux;

    //sensors
    public RevTouchSensor touchSensor;
    public ColorSensor colorSensor;

    //brightnesses
    public int maxBrightness;
    public int minBrightness;

    private KiwiDriveRatio ratio;

    // control hub imu
    public IMU imu;

    //distance sensor
    public Rev2mDistanceSensor distanceSensor;

    //arm sensors?
    public Servo servoShoulder;
    public Servo servoElbow;
    public Servo servoGripper;


    public final double TICKS_PER_ROTATION = 537.7;
    public final double WHEEL_CIRCUMFERENCE = 25;
    public final double TICKS_PER_CM = TICKS_PER_ROTATION / WHEEL_CIRCUMFERENCE;

    public final double TURNING_DIAMETER = 28.5;
    public final double TURNING_CIRCLE_CIRCUMFERENCE = 91.11;//centimeters

    public RobotHardware(HardwareMap hardwareMap, boolean isAuto) {
        /*
         * This code provides an object to control the physical
         * motor referenced by the configuration string.  The
         * positive direction of rotation is established. Finally,
         * the motor is directed to forcefully stop when no power
         * is applied.
         */

        // define the drive motors
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorAux = hardwareMap.dcMotor.get("motorAux");
        motorAux.setDirection(DcMotorSimple.Direction.FORWARD);
        motorAux.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //define external sensors
        touchSensor = hardwareMap.get(RevTouchSensor.class, "touchSensor");
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        distanceSensor = hardwareMap.get(Rev2mDistanceSensor.class, "distanceSensor");

        //define arm sensor stuff
        servoShoulder = hardwareMap.get(Servo.class, "servoShoulder");
        servoElbow = hardwareMap.get(Servo.class, "servoElbow");
        servoGripper = hardwareMap.get(Servo.class, "servoGrip");

        // reset the drive encoders to zero
        resetDriveEncoders();

        // setup the motor ratio
        ratio = new KiwiDriveRatio(isAuto);

        /*
         * Define the orientation of the Control (and the IMU inside).
         */

        // the logo on the control hub is pointed up toward the sky
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;

        // the usb on the control hub is pointed up toward the forward
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        // set this orientation
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        // now initialize the IMU with this mounting orientation
        // this assumes the IMU to be in a REV Control Hub is named "imu"
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        // define the current direction as 0
        imu.resetYaw();
    }

    public void resetDriveEncoders() {
        /*
         * This code resets the encoder values back to 0 for
         * each of the three drive motors.
         */
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorAux.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorAux.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void startMove(double drive, double strafe, double turn, double modifier) {
        /*
         * How much power should we apply to the left,
         * right, and aux motor?
         *
         * If all 3 motors apply the same power in the
         * same direction, the robot will turn in place.
         */
        ratio.computeRatio(drive, strafe, turn);

        /*
         * Apply the power to the motors.
         */
        motorLeft.setPower(ratio.powerLeft);
        motorRight.setPower(ratio.powerRight);
        motorAux.setPower(ratio.powerAux);


    }

    public int convertDistanceToTicks(double cm){
        int ticks = (int)Math.round(cm * TICKS_PER_CM);
        return ticks;
    }

    public double convertTicksToDistance(double ticks){
        double cm = 1.15*(ticks / 17.828663);
        return cm;
    }

    public int convertDegreesToTicks(double degrees){
        double arc_length = degrees / 360.0 * Math.PI * TURNING_DIAMETER;
        int numTicks = convertDistanceToTicks(arc_length);
        return numTicks;
    }

    public double getHeading() {
        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        return angles.getYaw(AngleUnit.DEGREES);
    }

    public void setAuxPower(double auxPower){
        motorAux.setPower(auxPower);
    }

}
