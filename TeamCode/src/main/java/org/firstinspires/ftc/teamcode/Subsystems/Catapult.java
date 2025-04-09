package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpMotorSpin;
@Config
public class Catapult {
    public enum States{
        RESTING,
        LOADING
    }
    public States currentState = States.LOADING;
    DcMotor leftMotor;
    DcMotor rightMotor;
    public static double kP = 0.006;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 0;
    public static double POSITION_TOLERANCE = 10;
    PIDFController pidfController = new PIDFController(kP,kI,kD,kF);
    public void initiate(HardwareMap hardwareMap){
        //Right Side Dominant
        leftMotor = hardwareMap.dcMotor.get("LeftCatapult");
        rightMotor = hardwareMap.dcMotor.get("RightCatapult");
        pidfController.setTolerance(POSITION_TOLERANCE);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public static double LOADING_POWER = 0.3;
    public static double RESTING_POS = 0;
    public static double LOAD_POS = 225;
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
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void run(Telemetry telemetry){
        pidfController.setP(kP);
        pidfController.setD(kD);
        pidfController.setI(kI);
        pidfController.setF(kF);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        switch (currentState){
            case RESTING:
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                if (System.currentTimeMillis() - timeOfFiring > 500){
                    setState(States.LOADING);
                }
                break;
            case LOADING:
                double power = pidfController.calculate(rightMotor.getCurrentPosition(),LOAD_POS);
                if (Math.abs(power) > LOADING_POWER){
                    power = LOADING_POWER * Math.signum(power);
                }
                leftMotor.setPower(-power);
                rightMotor.setPower(power);
        }
        telemetry.addLine("POSITION RESETS EVERY TIME YOU TURN OFF AND ON AGAIN!!!");
        telemetry.addLine("NORMAL POSITION STARTS WITH BLUE TAPE END POINTING TOWARDS THE SKY");
        telemetry.addLine("DO NOT MODIFY THE POSITION TOO MUCH");
        telemetry.addLine("PULLING MOTOR BACK TOO FAR COULD MAKE THINGS BREAK");
        telemetry.addLine("LEFT BUMPER TO FIRE!!!");
        telemetry.addData("currentPos",rightMotor.getCurrentPosition());
        telemetry.addData("currentState",currentState);
    }
}
