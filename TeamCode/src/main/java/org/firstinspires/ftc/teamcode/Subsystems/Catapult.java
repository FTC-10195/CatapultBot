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
    public static double LOCK_POS = 0;
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
                double power = pidfController.calculate(rightMotor.getCurrentPosition(),LOCK_POS);
                if (Math.abs(power) > LOADING_POWER){
                    power = LOADING_POWER * Math.signum(power);
                }
                leftMotor.setPower(-power);
                rightMotor.setPower(power);
        }
        telemetry.addLine("POSITION RESETS EVERY TIME YOU TURN OFF AND ON AGAIN!!!");
        telemetry.addLine("Set lock position:");
        telemetry.addLine("1. Stop robot from running");
        telemetry.addLine("2. Move Trigger to lock position manually:");
        telemetry.addLine("3. Run the robot");
        telemetry.addLine("4. Press Left Bumper to fire!:");
        telemetry.addData("currentPos",rightMotor.getCurrentPosition());
        telemetry.addData("currentState",currentState);
    }
}
