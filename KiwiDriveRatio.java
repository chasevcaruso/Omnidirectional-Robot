package edu.elon.robotics;

public class KiwiDriveRatio {

    public double powerLeft = 0.0;
    public double powerRight = 0.0;
    public double powerAux = 0.0;

    private boolean isAuto = false;

    public KiwiDriveRatio(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public void computeRatio(double drive, double strafe, double turn) {
        if (isAuto) computeRatioAuto(drive, strafe, turn);
        else computeRatioTeleop(drive, strafe, turn);
    }

    public void computeRatioAuto(double drive, double strafe, double turn) {
        powerLeft  = -0.58 * drive + 1.0 * strafe / 3.0 + turn / 3.0;
        powerRight =  0.58 * drive + 1.0 * strafe / 3.0 + turn / 3.0;
        powerAux   =                  2.0 * -strafe / 3.0 + turn / 3.0;
    }

    private void computeRatioTeleop(double drive, double strafe, double turn) {
        powerLeft  = -Math.sqrt(3.0/2.0)*drive + turn + -strafe*0.5;
        powerRight = Math.sqrt(3.0/2.0)*drive + turn + -strafe*0.5;
        powerAux   = turn + strafe;
    }

}
