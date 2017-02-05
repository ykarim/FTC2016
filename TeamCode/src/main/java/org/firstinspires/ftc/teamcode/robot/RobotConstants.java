package org.firstinspires.ftc.teamcode.robot;

public class RobotConstants {

    static String TAG = "FTC APP";
    public static final String teleOpTag = "Tele-Op : ";
    public static final String autoOpTag = "Autonomous : ";

    static String frontLeftMotor = "FL";
    static String frontRightMotor = "FR";
    static String backLeftMotor = "BL";
    static String backRightMotor = "BR";

    static String intakeMotor = "INTAKE";
    static String shootMotor = "SHOOT";
    static String capMotor = "CAP";

    static String leftBeaconServo = "LBEACON";
    static String rightBeaconServo = "RBEACON";

    static String opticalSensor = "OPTICAL";

    static final int ENCODER_TICKS_PER_REV = 1120;
    static final double WHEEL_DIAMETER = 3.0;
    static final double     DRIVE_GEAR_REDUCTION = 1.0 ;
    static final double TICK_PER_INCHES = (ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER * Math.PI);
    public static final double distFromCenterToWheel = Math.sqrt(162);

    public static double moveSpeed = 1.0;
    public static double rotateSpeed = 0.5;
    public static double intakeSpeed = 1.0;
    public static double shootSpeed = 1.0;
    public static double capSpeed = 1.0;

    public static double MAX_MOTOR_PWR = 1.0;
    public static double MIN_MOTOR_PWR = 0.0;

    static double SERVO_HOME = 0.3;
    static double SERVO_MIN = 0.2;
    static double SERVO_MAX = 0.9;
    static double leftBeaconPusherPosition = SERVO_HOME;
    static double rightBeaconPusherPosition = SERVO_HOME;
    static double beaconPusherSpeed = 0.05;

    public static int shotWaitPeriod = 3; //seconds to wait before shooting ball

    public static double gamepadThreshold = 0.15;

    public static final double whiteLineValue = 0.4;
}
