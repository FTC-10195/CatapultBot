package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystems.Catapult;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain2;

@TeleOp
public class OnlyDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        DriveTrain2 driveTrain = new DriveTrain2();
        driveTrain.initiate(hardwareMap);
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            driveTrain.run(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x);
            telemetry.update();
        }
    }
}
