package org.firstinspires.ftc.teamcode.opModes.auto.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConstants;
import org.firstinspires.ftc.teamcode.robot.RobotMovement;
import org.firstinspires.ftc.teamcode.robot.RobotUtilities;
import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Size;

@Autonomous(name = "Double Beacon Test", group = "autotest")
public class AutoBeaconTest extends LinearVisionOpMode{

    private Robot robot = new Robot();
    private RobotMovement robotMovement = new RobotMovement(robot);
    private RobotUtilities robotUtilities = new RobotUtilities(robot);

    @Override
    public void runOpMode() throws InterruptedException{
        waitFor(getDelay());
        waitForVisionStart();
        initVision();

        robot.initAutoOp(this, hardwareMap);

        Robot.TeamColor teamColor = getTeamColor();
        boolean blueLeft, blueRight, redLeft, redRight;

        waitForStart();

        while (opModeIsActive()) {
            robotMovement.move(RobotMovement.Direction.NORTH, 36);
            robotMovement.rotate(RobotMovement.Direction.ROTATE_RIGHT, 90);
            robotMovement.move(RobotMovement.Direction.NORTH, 48);
            robotUtilities.alignWithLine(RobotMovement.Direction.EAST);

            blueLeft = beacon.getAnalysis().isLeftBlue();
            blueRight = beacon.getAnalysis().isRightBlue();
            redLeft = beacon.getAnalysis().isLeftRed();
            redRight = beacon.getAnalysis().isRightRed();

            if (teamColor == Robot.TeamColor.BLUE) {
                if (blueLeft) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Blue Beacon One on Left");
                } else if (blueRight) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Blue Beacon One on Right");
                }
            } else if (teamColor == Robot.TeamColor.RED) {
                if (redLeft) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Red Beacon One on Left");
                } else if (redRight) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Red Beacon One on Right");
                }
            }

            robotUtilities.alignWithLine(RobotMovement.Direction.WEST);

            blueLeft = beacon.getAnalysis().isLeftBlue();
            redLeft = beacon.getAnalysis().isLeftRed();

            if (teamColor == Robot.TeamColor.BLUE) {
                if (blueLeft) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Blue Beacon Two on Left");
                } else if (blueRight) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Blue Beacon Two on Right");
                }
            } else if (teamColor == Robot.TeamColor.RED) {
                if (redLeft) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Red Beacon Two on Left");
                } else if (redRight) {
                    telemetry.addData(RobotConstants.autoOpTag, "Hit Red Beacon Two on Right");
                }
            }

            telemetry.update();
            requestOpModeStop();
        }
    }

    private void initVision() {
        this.setCamera(Cameras.SECONDARY);
        this.setFrameSize(new Size(900, 900));

        enableExtension(Extensions.BEACON);         //Beacon detection
        enableExtension(Extensions.ROTATION);       //Automatic screen rotation correction
        enableExtension(Extensions.CAMERA_CONTROL); //Manual camera control

        beacon.setAnalysisMethod(Beacon.AnalysisMethod.COMPLEX);

        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);

        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);

        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();
    }

    /**
     * Reads Team Color from FtcRobotControllerActivity
     * @return int representing team color (RED_ALLIANCE or BLUE_ALLIANCE)
     */
    private Robot.TeamColor getTeamColor() {
        boolean blueChecked = FtcRobotControllerActivity.blueTeamColor.isChecked();
        boolean redChecked = FtcRobotControllerActivity.redTeamColor.isChecked();

        if(blueChecked) {
            return Robot.TeamColor.BLUE;
        } else if(redChecked) {
            return Robot.TeamColor.RED;
        }
        return Robot.TeamColor.NONE;
    }

    /**
     * Returns int of sec to wait before running program
     * @return
     */
    private int getDelay() {
        try {
            return Integer.parseInt(FtcRobotControllerActivity.autoDelay.getText().toString());
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    /**
     * Wait a period of time. This will be non-blocking, so Thread away!
     * @param sec time to wait in seconds.
     */
    private void waitFor(int sec) {
        long millis = sec * 1000;
        long stopTime = System.currentTimeMillis() + millis;
        while(opModeIsActive() && System.currentTimeMillis() < stopTime) {
            try {
                waitOneFullHardwareCycle();
            } catch(Exception ex) {}
        }
    }
}