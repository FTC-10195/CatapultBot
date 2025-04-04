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
        LOADING,
        RETURNING
    }
    States currentState = States.RESTING;
    DcMotor leftMotor;
    DcMotor rightMotor;
    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 0;
    public static double POSITION_TOLERANCE = 50;
    PIDFController pidfController = new PIDFController(kP,kI,kD,kF);
    public void initiate(HardwareMap hardwareMap){
        //Right Side Dominant
        leftMotor = hardwareMap.dcMotor.get("LeftCatapult");
        rightMotor = hardwareMap.dcMotor.get("RightCatapult");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pidfController.setTolerance(POSITION_TOLERANCE);
    }
    public static double RETURNING_POWER = 0;
    public static double LOADING_POWER = 0.2;
    public static long LOAD_TIME = 400;
    public static double RETURNING_POS = 0;
    long loadStartTime = 0;
    public void setState(States newState){
        currentState = newState;
        loadStartTime = System.currentTimeMillis();
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
        switch (currentState){
            case RESTING:
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                break;
            case LOADING:
                long timePassed = (System.currentTimeMillis() - loadStartTime);
                if (timePassed > LOAD_TIME){
                    setState(States.RESTING);
                }
                leftMotor.setPower(-LOADING_POWER);
                rightMotor.setPower(LOADING_POWER);
                break;
            case RETURNING:
                double power = pidfController.calculate(rightMotor.getCurrentPosition(),RETURNING_POS);
                if (Math.abs(power) > RETURNING_POWER){
                    power = RETURNING_POWER * Math.signum(power);
                }
                leftMotor.setPower(-power);
                rightMotor.setPower(power);
                double difference = Math.abs(rightMotor.getCurrentPosition() - RETURNING_POS);
                if (difference < POSITION_TOLERANCE){
                    //Ready to be loaded and eventually fire
                    setState(States.RESTING);
                }
        }
        telemetry.addData("currentPos",rightMotor.getCurrentPosition());
        telemetry.addData("currentState",currentState);
    }
}
