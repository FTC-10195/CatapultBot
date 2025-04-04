package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
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
    public void initiate(HardwareMap hardwareMap){
        //Right Side Dominant
        leftMotor = hardwareMap.dcMotor.get("LeftCatapult");
        rightMotor = hardwareMap.dcMotor.get("RightCatapult");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public static double RETURNING_POWER = 0;
    public static double LOADING_POWER = 0.2;
    public static long LOAD_TIME = 400;
    long loadStartTime = 0;
    public void setState(States newState){
        currentState = newState;
        loadStartTime = System.currentTimeMillis();
    }
    public States getState(){
        return currentState;
    }
    public void run(Telemetry telemetry){
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
                leftMotor.setPower(0);
                rightMotor.setPower(0);
        }
        telemetry.addData("currentState",currentState);
    }
}
