package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystems.Catapult;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;

@TeleOp
public class NoDriveOutreach extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Catapult catapult = new Catapult();
        catapult.initiate(hardwareMap);
        Gamepad previousGamepad = new Gamepad();
        if (isStopRequested()) return;
        while (opModeIsActive()) {

            if (gamepad1.left_bumper){
                catapult.setState(Catapult.States.SHOOTING);
            }

            if (gamepad1.options){
                catapult.reset();
            }

            catapult.run(telemetry);
            previousGamepad.copy(gamepad1);
            telemetry.update();
        }
    }
}
