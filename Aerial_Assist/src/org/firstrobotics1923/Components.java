package org.firstrobotics1923;

import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.firstrobotics1923.system.DriveSystem;
import org.firstrobotics1923.system.DriveTrain;
import org.firstrobotics1923.util.MotorGroup;
import org.firstrobotics1923.util.SmartDashboardInterface;
import org.firstrobotics1923.util.StickShift;
import org.firstrobotics1923.util.XboxController;

/**
 * All Robotic components used in the code compiled in one place
 * 
 * @author Pavan Hegde, Prasanth Yedlapalli, Aryak Pande
 * @version 1.5
 * @since Jan. 13, 2014
 */
public class Components {
    /* Joysticks and Xbox controller */
    public static final StickShift leftStick = new StickShift(1);         // Left Joystick in port 1
    public static final StickShift rightStick = new StickShift(2);        //Right Joystick in port 2
    public static final XboxController operatorControl = new XboxController(3); //Xbox Controller in 3
    
    /* Vision */
    public static NetworkTable table = NetworkTable.getTable("SmartDashboard");
   
    // SmartDashboard Interface
    public static SmartDashboardInterface sfxDashboard = new SmartDashboardInterface(table,true);

    
    /* Speed controllers */
    public static final Victor frontLeftDrive = new Victor(9);  
    public static final Victor rearLeftDrive = new Victor(1);    
    
    public static final Victor frontRightDrive = new Victor(4); 
    public static final Victor rearRightDrive = new Victor(3);
   
    public static final Victor shooterFrontRight = new Victor(5);
    public static final Victor shooterBackRight = new Victor(6);
    
    public static final Victor shooterFrontLeft = new Victor(8);
    public static final Victor shooterBackLeft = new Victor(7);
   
    //Pneumatics
    public static final Solenoid shooterAngleControllerOne = new Solenoid(1);    
    public static final Solenoid shooterAngleControllerTwo = new Solenoid(2);
    
    public static final Solenoid intakeAngleControllerOne = new Solenoid(3);
    public static final Solenoid intakeAngleControllerTwo = new Solenoid(4);
    
    public static final Solenoid indexerOne = new Solenoid(5);
    public static final Solenoid indexerTwo = new Solenoid(6);
    
    /* Motor Group Init */
    public static final MotorGroup driveLeftSide = new MotorGroup(frontLeftDrive, rearLeftDrive);
    public static final MotorGroup driveRightSide = new MotorGroup(frontRightDrive, rearRightDrive);
    
    public static final MotorGroup shooterRightWheels = new MotorGroup(shooterBackRight, shooterFrontRight);
    public static final MotorGroup shooterLeftWheels = new MotorGroup(shooterBackLeft, shooterFrontLeft);
    
    /* System Init */
    public static final DriveSystem robotDrive = new DriveSystem(driveLeftSide, driveRightSide);
    
    /* Sensors */
    public static Encoder DriveEncoderLeft = new Encoder(1,2, false, CounterBase.EncodingType.k4X);
    public static Gyro DriveGyro = new Gyro(1);
    public static Encoder DriveEncoderRight = new Encoder(3,4, false, CounterBase.EncodingType.k4X);
    public static Accelerometer DriveAccelerometer = new Accelerometer(3);
    // Temperature on analog channel 1.
    public static AnalogChannel temperature = new AnalogChannel(2);
    public static DriveTrain driveTrain = new DriveTrain();
    
    public static void init(){
        // Update LiveWindow - This is for Test mode only !!!!!!!
   
    }
}