package edu.elon.robotics;

/**
 * A helper class for controlling servos.
 * Mainly provides min/max limiting of the servo.
 *
 * @author J. Hollingsworth
 */

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

public class ControlledServo {
    private Servo servo;
    private String name;
    private double pos, min, max;

    public ControlledServo(Servo servo, String name, double pos, double min, double max) {
        this.servo = servo;
        this.name = name;
        this.pos = pos;
        this.min = min;
        this.max = max;
    }

    public void setPosition(double position) {
        this.pos = Range.clip(position, min, max);
    }

    public void changePositionBy(double delta) {
        this.pos = Range.clip(this.pos + delta, min, max);
    }

    public void update() {
        servo.setPosition(this.pos);
    }

    public String toString() {
        String posStr = String.format(Locale.US, "%.2f", pos);
        return name + "==> pos: " + posStr + "[" + min + "," + max + "]";
    }

}