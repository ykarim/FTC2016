package org.firstinspires.ftc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConstants;
import org.firstinspires.ftc.teamcode.robot.RobotMovement;
import org.firstinspires.ftc.teamcode.robot.RobotUtilities;

@TeleOp (name = "TeleOp", group = "tele")
public class MainTeleOp extends OpMode{

    private Robot robot = new Robot();
    private RobotMovement robotMovement = new RobotMovement(robot);
    private RobotUtilities robotUtilities = new RobotUtilities(robot);
    private String TAG = RobotConstants.teleOpTag + "Main : ";

    @Override
    public void init() {
        telemetry.addData(TAG, "Status : INITIALIZING");
        robot.initTeleOp(hardwareMap);
        gamepad1.setJoystickDeadzone(.1f);
        gamepad2.setJoystickDeadzone(.1f);

        telemetry.addData(TAG, "Status : READY");
    }

    @Override
    public void loop() {
        updateTelemetryData();

        robotMovement.move(convertGamepadToMovement());
        convertGamepadToIntake(gamepad2.left_stick_y);
        convertGamepadToShoot(gamepad2.right_stick_y);

        if (gamepad1.a) { //Gamepad 1 - X Button

            //Unimplemented

        } else if (gamepad1.b) { //Gamepad 1 - B Button

            //Unimplemented

        } else if (gamepad1.x) { //Gamepad 1 - X Button : Toggles left beacon pusher

            while (gamepad1.x) {}
//            robotUtilities.toggleBeaconPresser(robot.leftBeacon);

        } else if (gamepad1.y) { //Gamepad 1 - Y Button : Toggles right beacon pusher

            while (gamepad1.y) {}
//            robotUtilities.toggleBeaconPresser(robot.rightBeacon);

        } else if (gamepad1.left_bumper) { // Gamepad 1 - Left Bumper : Inverts Robot Direction

            while (gamepad1.left_bumper) {}
            robotMovement.invertDirection();

        } else if (gamepad1.right_bumper) { //Gamepad 1 - Right Bumper : Movement Kill Switch

            while (gamepad1.right_bumper) {
            }
            robotMovement.move(RobotMovement.Direction.NONE);

        } else if (inThresholdRange(gamepad1.left_trigger)) { //Gamepad 1 - Left Trigger

            //Unimplemented

        } else if (inThresholdRange(gamepad1.right_trigger)) { //Gamepad 1 - Right Trigger

            //Unimplemented

        } else if (gamepad2.a) { //Gamepad 2 - A Button

            //Unimplemented

        } else if (gamepad2.b) { //Gamepad 2 - B Button

            //Unimplemented

        } else if (gamepad2.x) { //Gamepad 2 - X Button : Toggles continuous intake

            while (gamepad2.x) { }
            robotUtilities.continuousIntake();

        } else if (gamepad2.y) { //Gamepad 2 - Y Button : Toggles continuous intake

            while (gamepad2.y) { }
            robotUtilities.continuousShoot();

        } else if (gamepad2.left_bumper) { //Gamepad 2 - Left Bumper : Increases intake speed

            while (gamepad2.left_bumper) {}
            RobotConstants.intakeSpeed += 0.05;
            RobotConstants.intakeSpeed = Range.clip(RobotConstants.intakeSpeed,
                    RobotConstants.MIN_MOTOR_PWR, RobotConstants.MAX_MOTOR_PWR);

        } else if (gamepad2.right_bumper) { //Gamepad 2 - Right Bumper : Decreases intake speed

            while (gamepad2.right_bumper) {}
            RobotConstants.intakeSpeed -= 0.05;
            RobotConstants.intakeSpeed = Range.clip(RobotConstants.intakeSpeed,
                    RobotConstants.MIN_MOTOR_PWR, RobotConstants.MAX_MOTOR_PWR);

        } else if (inThresholdRange(gamepad2.left_trigger)) { //Gamepad 2 - Left Trigger : Increases shoot speed

            while (inThresholdRange(gamepad2.left_trigger)) {}
            RobotConstants.shootSpeed += 0.05;
            RobotConstants.shootSpeed = Range.clip(RobotConstants.shootSpeed,
                    RobotConstants.MIN_MOTOR_PWR, RobotConstants.MAX_MOTOR_PWR);

        } else if (inThresholdRange(gamepad2.right_trigger)) { //Gamepad 2 - Right Trigger : Decreases shoot speed

            while (inThresholdRange(gamepad2.right_trigger)) {}
            RobotConstants.shootSpeed -= 0.05;
            RobotConstants.shootSpeed = Range.clip(RobotConstants.shootSpeed,
                    RobotConstants.MIN_MOTOR_PWR, RobotConstants.MAX_MOTOR_PWR);

        } else {
//            robot.cap.setPower(0);
        }
    }

    /**
     * Converts gamepad-1 x and y coords to robot directions
     * TODO: Add cases for diagonal movement
     * @return
     */
    private RobotMovement.Direction convertGamepadToMovement() {
        if (gamepad1.left_stick_y > RobotConstants.gamepadThreshold &&
                !inThresholdRange(gamepad1.left_stick_x)) {
            return RobotMovement.Direction.NORTH;
        } else if (gamepad1.left_stick_y < -RobotConstants.gamepadThreshold &&
                !inThresholdRange(gamepad1.left_stick_x)) {
            return RobotMovement.Direction.SOUTH;
        } else if (gamepad1.left_stick_x > RobotConstants.gamepadThreshold &&
                !inThresholdRange(gamepad1.left_stick_y)) {
            return RobotMovement.Direction.EAST;
        } else if (gamepad1.left_stick_x < -RobotConstants.gamepadThreshold &&
                !inThresholdRange(gamepad1.left_stick_y)) {
            return RobotMovement.Direction.WEST;
        } else if (inThresholdRange(gamepad1.left_trigger)) {
            return RobotMovement.Direction.ROTATE_LEFT;
        } else if (inThresholdRange(gamepad1.right_trigger)) {
            return RobotMovement.Direction.ROTATE_RIGHT;
        } else {
            return RobotMovement.Direction.NONE;
        }
    }

    private void convertGamepadToIntake(double value) {
        if (value > RobotConstants.gamepadThreshold) {
            robot.intake.setPower(RobotConstants.intakeSpeed);
        } else if (value < -RobotConstants.gamepadThreshold) {
            robot.intake.setPower(-RobotConstants.intakeSpeed);
        } else {
            if (!robotUtilities.continuousIntake) {
                robot.intake.setPower(0);
            }
        }
    }

    private void convertGamepadToShoot(double value) {
        if (value > RobotConstants.gamepadThreshold) {
            robot.shoot.setPower(RobotConstants.shootSpeed);
        } else if (value < -RobotConstants.gamepadThreshold) {
            robot.shoot.setPower(-RobotConstants.shootSpeed);
        } else {
            if (!robotUtilities.continuousShoot) {
                robot.shoot.setPower(0);
            }
        }
    }

    private boolean inThresholdRange(double val) {
        if (val > RobotConstants.gamepadThreshold ||
                val < -RobotConstants.gamepadThreshold) {
            return true;
        } else {
            return false;
        }
    }

    private void updateTelemetryData() {
        telemetry.addData(TAG, "Status : RUNNING");

        telemetry.addData(TAG, "Gamepad 1 Left Position : (" + gamepad1.left_stick_x + " , " + gamepad1.left_stick_y + ")");
        telemetry.addData(TAG, "Gamepad 1 Right Position : (" + gamepad1.right_stick_x + " , " + gamepad1.right_stick_y + ")");
        telemetry.addData(TAG, "Gamepad 2 Left Position : (" + gamepad2.left_stick_x + " , " + gamepad2.left_stick_y + ")");
        telemetry.addData(TAG, "Gamepad 2 Right Position : (" + gamepad2.right_stick_x + " , " + gamepad2.right_stick_y + ")");

        telemetry.addData(TAG, "Cont. Intake : " + robotUtilities.continuousIntake);
        telemetry.addData(TAG, "Cont. Shoot : " + robotUtilities.continuousShoot);

        telemetry.addData(TAG, "Intake Speed : " + RobotConstants.intakeSpeed);
        telemetry.addData(TAG, "Shoot Speed : " + RobotConstants.shootSpeed);

        telemetry.addData(TAG, "Inverted : " + robotMovement.getInverted());
        telemetry.update();
    }
}
