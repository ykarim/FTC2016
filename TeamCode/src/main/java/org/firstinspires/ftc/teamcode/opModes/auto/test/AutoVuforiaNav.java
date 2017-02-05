package org.firstinspires.ftc.teamcode.opModes.auto.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConstants;
import org.firstinspires.ftc.teamcode.robot.RobotMovement;
import org.firstinspires.ftc.teamcode.robot.RobotUtilities;
import org.firstinspires.ftc.teamcode.utils.BeaconUtils;

@Autonomous (name = "Vuforia Test", group = "autotest")
public class AutoVuforiaNav extends LinearOpMode{

    private final String licenseKey = " Ac2N2pD/////AAAAGUtP1PV5KUFbn2hG/K+RFO1V1MLAkEORx2m1ZGTlPO" +
            "/MyjNlkL/iv3eFSj9um8RlJ0HrOsHSJ3ajrpZgyAnyikkmh2rPyWbmw5UfLw9YLLGsC99ATkXLKk+sjjiUTSV" +
            "0hubnL+9AfgrvhNY4kTG3XX7feZvFByyF1La4Inw/UFPx9LiunShdUK9QRgX3HfSACm1JHZGVj8mL0TByq6OL" +
            "4lOA77UOX8Kn++Ed/OMyv6QyaRVxGob8uQv+xBhvKKm1QUFg2tTRHdnV9Lc0tbMUQLx2bS6XgAIkq4qF8ZIMO" +
            "cd8uUYztkKoI5V9kiOHFpNZJS8yE7Qi8pgdwuetwHO3X4ZKLMUwBnUYVQbqfPzOa2ay";

    private Robot tom = new Robot();
    private RobotMovement robotMovement = new RobotMovement(tom);
    private RobotUtilities robotUtilities = new RobotUtilities(tom);

    private final String TAG = RobotConstants.autoOpTag + "Vuforia Test : ";

    @Override
    public void runOpMode() throws InterruptedException{
        VuforiaLocalizer vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(initVuforia());
        vuforiaLocalizer.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();
        VuforiaTrackableDefaultListener lego = (VuforiaTrackableDefaultListener) beacons.get(2).getListener();
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        beacons.activate();

        tom.initAutoOp(this, hardwareMap);

        robotMovement.rotate(RobotMovement.Direction.ROTATE_RIGHT, 45);

        robotMovement.move(RobotMovement.Direction.NORTH);
        while (opModeIsActive() && gears.getPose() == null) {
            idle();
        }
        robotMovement.move(RobotMovement.Direction.NONE);

        //Analyze beacon
        int beaconConfig = BeaconUtils.waitForBeaconDetection(this,
                BeaconUtils.getImageFromFrame(vuforiaLocalizer.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, vuforiaLocalizer.getCameraCalibration(), 5000);

        VectorF angles = anglesFromTarget(gears);
        VectorF trans = navOffWall(gears.getPose().getTranslation(),
                Math.toDegrees(angles.get(0)) - 90,
                new VectorF(500, 0, 0)); //50 cm away from wall

        if (trans.get(0) > 0) {
            robotMovement.move(RobotMovement.Direction.ROTATE_RIGHT);
        } else {
            robotMovement.move(RobotMovement.Direction.ROTATE_LEFT);
        }

        do {
            if (gears.getPose() != null) {
                trans = navOffWall(gears.getPose().getTranslation(),
                        Math.toDegrees(angles.get(0)) - 90,
                        new VectorF(500, 0, 0));
                idle();
            }
        } while (opModeIsActive() && Math.abs(trans.get(0)) > 30);

        robotMovement.move(RobotMovement.Direction.NONE);

        //Need to move Math.hypot(trans.get(0), trans.get(2) + 150) / 409.575 * 1120 NORTH
        robotMovement.move(RobotMovement.Direction.NORTH, Math.hypot(trans.get(0), trans.get(2) + 150)); //Change 150 = dist from center robot to center of phone

        while (opModeIsActive() && (gears.getPose() != null || Math.abs(gears.getPose().getTranslation().get(0)) > 10)) {
            if (gears != null) {
                if (gears.getPose().getTranslation().get(0) > 0) {
                    robotMovement.move(RobotMovement.Direction.ROTATE_RIGHT);
                } else {
                    robotMovement.move(RobotMovement.Direction.ROTATE_LEFT);
                }
            } else {
                robotMovement.move(RobotMovement.Direction.ROTATE_RIGHT);
            }
        }
        robotMovement.move(RobotMovement.Direction.NONE);
        addToTelemetry("Aligned with beacon");

        if (beaconConfig == BeaconUtils.BEACON_BLUE_RED) {
            if (getTeamColor() == Robot.TeamColor.BLUE) {
                robotUtilities.toggleBeaconPresser(tom.leftBeacon);
            } else if (getTeamColor() == Robot.TeamColor.RED) {
                robotUtilities.toggleBeaconPresser(tom.rightBeacon);
            }
        } else if (beaconConfig == BeaconUtils.BEACON_RED_BLUE) {
            if (getTeamColor() == Robot.TeamColor.BLUE) {
                robotUtilities.toggleBeaconPresser(tom.rightBeacon);
            } else if (getTeamColor() == Robot.TeamColor.RED) {
                robotUtilities.toggleBeaconPresser(tom.leftBeacon);
            }
        }
        addToTelemetry("Hit beacon button");

    }

    private VuforiaLocalizer.Parameters initVuforia () {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        parameters.vuforiaLicenseKey = licenseKey;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        return parameters;
    }

    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF((float) (trans.get(0) - offWall.get(0) *
                Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) *
                Math.cos(Math.toRadians(robotAngle))), trans.get(1),
                (float) (trans.get(2) + offWall.get(0) *
                        Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) *
                        Math.sin(Math.toRadians(robotAngle))));
    }

    public VectorF anglesFromTarget(VuforiaTrackableDefaultListener image){
        float [] data = image.getRawPose().getData();
        float [] [] rotation = {{data[0], data[1]}, {data[4], data[5], data[6]}, {data[8], data[9], data[10]}};
        double thetaX = Math.atan2(rotation[2][1], rotation[2][2]);
        double thetaY = Math.atan2(-rotation[2][0], Math.sqrt(rotation[2][1] * rotation[2][1] + rotation[2][2] * rotation[2][2]));
        double thetaZ = Math.atan2(rotation[1][0], rotation[0][0]);
        return new VectorF((float)thetaX, (float)thetaY, (float)thetaZ);
    }

    /**
     * Reads Team Color from FtcRobotControllerActivity
     * @return Robot.TeamColor
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

    private void addToTelemetry (String... msg) {
        for (String text : msg) {
            telemetry.addData(TAG, text);
        }
        telemetry.update();
    }
}
