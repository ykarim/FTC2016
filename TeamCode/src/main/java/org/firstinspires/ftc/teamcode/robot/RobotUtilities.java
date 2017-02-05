package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Servo;

import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;

public class RobotUtilities {

    private Robot robot = null;

    public boolean continuousIntake = false;
    public boolean continuousShoot = false;

    private boolean leftBeaconPusherExtended = false;
    private boolean rightBeaconPusherExtended = false;

    public RobotUtilities(Robot robot) {
        this.robot = robot;
    }

    public void capBall() {
        //TODO
    }

    public void toggleBeaconPresser(Servo servo) {
        if (servo == robot.leftBeacon) {
            if (!leftBeaconPusherExtended) {
                while (RobotConstants.leftBeaconPusherPosition != RobotConstants.SERVO_MAX) {
                    RobotConstants.leftBeaconPusherPosition += RobotConstants.beaconPusherSpeed;
                    servo.setPosition(RobotConstants.leftBeaconPusherPosition);
                }
                leftBeaconPusherExtended = true;
            } else {
                while (RobotConstants.leftBeaconPusherPosition != RobotConstants.SERVO_MIN) {
                    RobotConstants.leftBeaconPusherPosition -= RobotConstants.beaconPusherSpeed;
                    servo.setPosition(RobotConstants.leftBeaconPusherPosition);
                }
                leftBeaconPusherExtended = false;
            }
        } else if (servo == robot.rightBeacon) {
            if (!rightBeaconPusherExtended) {
                while (RobotConstants.rightBeaconPusherPosition != RobotConstants.SERVO_MAX + RobotConstants.beaconPusherSpeed) {
                    RobotConstants.rightBeaconPusherPosition += RobotConstants.beaconPusherSpeed;
                    servo.setPosition(RobotConstants.rightBeaconPusherPosition);
                }
                rightBeaconPusherExtended = true;
            } else {
                while (RobotConstants.rightBeaconPusherPosition != RobotConstants.SERVO_MIN) {
                    RobotConstants.rightBeaconPusherPosition -= RobotConstants.beaconPusherSpeed;
                    servo.setPosition(RobotConstants.rightBeaconPusherPosition);
                }
                rightBeaconPusherExtended = false;
            }
        }
    }

    public void pushBeaconButton(Beacon.BeaconAnalysis analysis, Robot.TeamColor teamColor) {
        boolean leftBlue, leftRed, rightBlue, rightRed;

        leftBlue = analysis.isLeftBlue();
        leftRed = analysis.isLeftRed();
        rightBlue = analysis.isRightBlue();
        rightRed = analysis.isRightRed();

        if (teamColor == Robot.TeamColor.BLUE) {
            if (leftBlue) {
                toggleBeaconPresser(robot.leftBeacon);
                toggleBeaconPresser(robot.leftBeacon);
            } else if (rightBlue) {
                toggleBeaconPresser(robot.rightBeacon);
                toggleBeaconPresser(robot.rightBeacon);
            }
        } else if (teamColor == Robot.TeamColor.RED) {
            if (leftRed) {
                toggleBeaconPresser(robot.leftBeacon);
                toggleBeaconPresser(robot.leftBeacon);
            } else if (rightRed) {
                toggleBeaconPresser(robot.rightBeacon);
                toggleBeaconPresser(robot.rightBeacon);
            }
        }
    }

    public void shootBall(LinearVisionOpMode opMode) {
        shootBalls(true);
        waitFor(opMode, RobotConstants.shotWaitPeriod);

        intakeBalls(true);
        waitFor(opMode, 3);

        intakeBalls(false);
        shootBalls(false);
    }

    public void shootDoubleBall(LinearVisionOpMode opMode) {
        shootBalls(true);
        waitFor(opMode, RobotConstants.shotWaitPeriod);

        intakeBalls(true);
        waitFor(opMode, 5);

        intakeBalls(false);
        shootBalls(false);
    }

    public void intakeBalls(boolean condition) {
        if (condition) {
            robot.intake.setPower(RobotConstants.intakeSpeed);
        } else {
            robot.intake.setPower(0);
        }
    }

    public void shootBalls(boolean condition) {
        if (condition) {
            robot.shoot.setPower(RobotConstants.shootSpeed);
        } else {
            robot.shoot.setPower(0);
        }
    }

    public void continuousIntake() {
        if (!continuousIntake) {
            continuousIntake = true;
            robot.intake.setPower(RobotConstants.intakeSpeed);
        } else {
            continuousIntake = false;
            robot.intake.setPower(0);
        }
    }

    public void continuousShoot() {
        if (!continuousShoot) {
            continuousShoot = true;
            robot.shoot.setPower(RobotConstants.shootSpeed);
        } else {
            continuousShoot = false;
            robot.shoot.setPower(0);
        }
    }

    /**
     * Aligns ODS by moving left or right given which side line is located on
     * @param direction
     */
    public void alignWithLine(RobotMovement.Direction direction) {
        robot.lightSensor.enableLed(true);

        RobotMovement robotMovement = new RobotMovement(robot);
        robotMovement.move(direction);

        while (robot.lightSensor.getLightDetected() > RobotConstants.whiteLineValue) { }
        robotMovement.move(RobotMovement.Direction.NONE);
    }

    private void waitFor(LinearVisionOpMode opMode, int sec) {
        long millis = sec * 1000;
        long stopTime = System.currentTimeMillis() + millis;
        while(opMode.opModeIsActive() && System.currentTimeMillis() < stopTime) {
            try {
                opMode.waitOneFullHardwareCycle();
            } catch(Exception ex) {}
        }
    }
}
