package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

import java.util.ArrayList;

public class Robot {

    private HardwareMap hwMap = null;

    public DcMotor fl = null; //Front - Left Motor (Config == "FL")
    public DcMotor fr = null; //Front - Right Motor (Config == "FR")
    public DcMotor bl = null; //Back - Left Motor (Config == "BL")
    public DcMotor br = null; //Back - Right Motor (Config == "BR")

    public DcMotor intake = null; //Intake Motor (Config == "INTAKE")
    public DcMotor shoot = null; //Shoot Motor (Config == "SHOOT")

    public DcMotor cap = null; //Cap Motor (Config == "CAP")

    public Servo leftBeacon = null; //Left Beacon Pusher (Config == "LBEACON")
    public Servo rightBeacon = null; //Right Beacon Pusher (Config == "RBEACON")

    public ArrayList<DcMotor> driveMotors = new ArrayList<>(); //stores all drive motors
    public ArrayList<DcMotor> ballMotors = new ArrayList<>(); //stores all ball motors (intake, shoot)

    public OpticalDistanceSensor lightSensor = null; //Distance Sensor (Config == "OPTICAL")

    public enum TeamColor {
        RED("R"), BLUE("B"), NONE("N");

        private final String teamColor;

        TeamColor(String teamColor) {
            this.teamColor = teamColor;
        }

        public String getTeamColor() {
            return teamColor;
        }
    }

    /**
     * Initializes all drive and ball motors in NO ENCODERS mode
     * @param hwMap
     */
    public void initTeleOp(HardwareMap hwMap) {
        this.hwMap = hwMap;
        initDrive();
        initBall();
//        initCap();
//        initServos();

        for(DcMotor driveMotor : driveMotors) {
            driveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            driveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            driveMotor.setPower(0);
        }
        for(DcMotor ballMotor : ballMotors) {
            ballMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ballMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            ballMotor.setPower(0);
        }
//        cap.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        cap.setDirection(DcMotorSimple.Direction.FORWARD);
//        cap.setPower(0);
    }

    /**
     * Initializes all drive and ball motors in USING ENCODERS mode
     * @param hwMap
     */
    public void initAutoOp(LinearOpMode opMode, HardwareMap hwMap) {
        this.hwMap = hwMap;

        initDrive();
        initBall();
//        initCap();
//        initServos();
//        initSensors();

        for(DcMotor driveMotor : driveMotors) {
            driveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            opMode.idle();
            driveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            driveMotor.setPower(0);
        }
        for(DcMotor ballMotor : ballMotors) {
            ballMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            opMode.idle();
            ballMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ballMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            ballMotor.setPower(0);
        }
//        cap.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        cap.setDirection(DcMotorSimple.Direction.FORWARD);
//        cap.setPower(0);
    }

    /**
     * Initializes all drive and ball motors in USING ENCODERS mode
     * @param hwMap
     */
    public void initAutoOp(LinearVisionOpMode opMode, HardwareMap hwMap) {
        this.hwMap = hwMap;

        initDrive();
        initBall();
//        initCap();
//        initSensors();

        for(DcMotor driveMotor : driveMotors) {
            driveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            driveMotor.setPower(0);
        }
        for(DcMotor ballMotor : ballMotors) {
            ballMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ballMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ballMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            ballMotor.setPower(0);
        }
//        cap.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        cap.setDirection(DcMotorSimple.Direction.FORWARD);
//        cap.setPower(0);
    }

    private void initDrive() {
        fl = hwMap.dcMotor.get(RobotConstants.frontLeftMotor);
        fr = hwMap.dcMotor.get(RobotConstants.frontRightMotor);
        bl = hwMap.dcMotor.get(RobotConstants.backLeftMotor);
        br = hwMap.dcMotor.get(RobotConstants.backRightMotor);

        driveMotors.add(fl);
        driveMotors.add(fr);
        driveMotors.add(bl);
        driveMotors.add(br);
    }

    private void initBall() {
        intake = hwMap.dcMotor.get(RobotConstants.intakeMotor);
        shoot = hwMap.dcMotor.get(RobotConstants.shootMotor);

        ballMotors.add(intake);
        ballMotors.add(shoot);
    }

    private void initCap() {
        cap = hwMap.dcMotor.get(RobotConstants.capMotor);
    }

    private void initServos() {
        leftBeacon = hwMap.servo.get(RobotConstants.leftBeaconServo);
        rightBeacon = hwMap.servo.get(RobotConstants.rightBeaconServo);

        leftBeacon.setPosition(RobotConstants.leftBeaconPusherPosition);
        rightBeacon.setPosition(RobotConstants.rightBeaconPusherPosition);
    }

    private void initSensors() {
        lightSensor = hwMap.opticalDistanceSensor.get(RobotConstants.opticalSensor);
    }

    public void setDriveMotorMode(DcMotor.RunMode mode) {
        for (DcMotor motor : driveMotors) {
            motor.setMode(mode);
        }
    }

    public void setDriveMotorPower(double power) {
        for (DcMotor motor : driveMotors) {
            motor.setPower(power);
        }
    }
}
