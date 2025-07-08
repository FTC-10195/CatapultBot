package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class DriveTrain2 {
    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    public static double fl = 1;
    public static double bl = 1;
    public static double fr = 1;
    public static double br = 1;
    public void initiate(HardwareMap hardwareMap){
        frontLeftMotor = hardwareMap.dcMotor.get("fl");
        frontRightMotor = hardwareMap.dcMotor.get("fr");
        backLeftMotor = hardwareMap.dcMotor.get("bl");
        backRightMotor = hardwareMap.dcMotor.get("br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Are all of the motors going to spin the right direction?
    }
    public void run(double x, double y, double rx){
        x = -x;
        rx = -rx;
        //run or update are generally methods that run in a loop
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator * fl;
        double backLeftPower = (y - x + rx) / denominator * bl;
        double frontRightPower = (y - x - rx) / denominator * fr;
        double backRightPower = (y + x - rx) / denominator * br;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }
    public void status(Telemetry telemetry){
        telemetry.addLine("USE FTC DASHBOARD, http://192.168.43.1:8080/dash, look under drive train, then change the values by multiplying by adding negative sign in front to flip motor spin direction. Press Enter to save values, OR save button on top right. GOOD LUCK TEAM!!! ");
    }
}
