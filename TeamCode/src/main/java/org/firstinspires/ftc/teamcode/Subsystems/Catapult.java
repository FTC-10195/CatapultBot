package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Catapult {
    public enum States{
        RESTING,
        SHOOTING
    }
    public States currentState = States.RESTING;
    DcMotor catapultMotor;
    public static double kP = 0.006;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 0;
    public static double POSITION_TOLERANCE = 10;
    PIDFController pidfController = new PIDFController(kP,kI,kD,kF);
    public void initiate(HardwareMap hardwareMap){
        //Right Side Dominant
        catapultMotor = hardwareMap.dcMotor.get("RightCatapult");
        pidfController.setTolerance(POSITION_TOLERANCE);
        catapultMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public static double MAX_LAUNCHING_POWER = 0.5;
    public static double SHOOT_POS = 120;
    long timeOfFiring = 0;
    public void setState(States newState){
        currentState = newState;
        if (newState == States.RESTING){
            timeOfFiring = System.currentTimeMillis();
        }
    }
    public States getState(){
        return currentState;
    }
    public void reset(){
        catapultMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void run(Telemetry telemetry){
        pidfController.setP(kP);
        pidfController.setD(kD);
        pidfController.setI(kI);
        pidfController.setF(kF);
        catapultMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        switch (currentState){
            case RESTING:
                catapultMotor.setPower(0);
                break;
            case SHOOTING:
                double power = pidfController.calculate(catapultMotor.getCurrentPosition(),SHOOT_POS);
                if (Math.abs(power) > MAX_LAUNCHING_POWER){
                    power = MAX_LAUNCHING_POWER * Math.signum(power);
                }else if (Math.abs(power) < .05){
                    power = 0;
                }
                catapultMotor.setPower(power);
                if (System.currentTimeMillis() - timeOfFiring > 500){
                    setState(States.RESTING);
                }
        }
        telemetry.addLine("Press Left Bumper to fire!:");
        telemetry.addData("currentPos", catapultMotor.getCurrentPosition());
        telemetry.addData("currentState",currentState);
    }
}
