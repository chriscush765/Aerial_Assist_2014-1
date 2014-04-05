/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firstrobotics1923.Components;
import java.lang.System;
import edu.wpi.first.wpilibj.PIDController;
import org.firstrobotics1923.util.MotorGroup;


/**
 *
 * @author Makarand
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    // Declare variable for the robot drive system
    public RobotDrive driveTrain = Components.robotDrive;
    private Timer timer;
    // Drive Wheel Encoders
    private Encoder leftEnc = Components.DriveEncoderLeft;
    private Encoder rightEnc = Components.DriveEncoderRight;
    // gyro.
    private Gyro gyro = Components.DriveGyro;
    private MotorGroup left = Components.driveLeftSide;
    private MotorGroup right = Components.driveRightSide;
    
    
    // distance per pulse of a drive-wheel encoder, in inches. [CHANGE THESE VALUES!!!!!!!!!!]
    private static final double NUM_CLICKS = 256, //distance per pulse = 0.0491"/pulse
            GEAR_RATIO = 1.0/1.0, 
            WHEEL_CIRCUMFERENCE = 12.56,   // 4 inches wheels
            Pg = 0.1, Ig = 0.005, Dg = 0.0,     // LEAVE THESE CONSTANTS ALONE!
            Pe = 8.0, Ie = 0.01, De = 0.0,      // LEAVE THESE CONSTANTS ALONE!
            PID_LOOP_TIME = .05, 
            gyroTOLERANCE = 0.3,            // 0.2778% error ~= 0.5 degrees...?
            encoderTOLERANCE = 2.0;         // +/- 2" tolarance
            
    private static final int GYRO_MODE = 0, ENCODER_MODE = 1, MANUAL_MODE = 2;
    private int mode = MANUAL_MODE;
    private boolean timerRunning = false;
    private static double SETTLED_TIME = 2.0;
    
    private AnalogChannel temperature = Components.temperature;         // temperature.
    
    private PIDController gyroLeftPID, gyroRightPID, leftEncPID, rightEncPID;//, encPID, accelPID;
    
    public void initDefaultCommand() {
        //setDefaultCommand(new ManualDrive()); // TBD for Commandbased programming
    }
    
    public void init() {
   
        // Set distance per pulse for each encoder
        leftEnc.setDistancePerPulse(GEAR_RATIO*WHEEL_CIRCUMFERENCE/NUM_CLICKS);
        rightEnc.setDistancePerPulse(GEAR_RATIO*WHEEL_CIRCUMFERENCE/NUM_CLICKS);
        // Set PID source parameter to Distance...
        leftEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        rightEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        // Start the encoders.
        leftEnc.start();
        rightEnc.start();
        gyro.reset();
         
        gyroLeftPID = new PIDController(Pg,Ig,Dg,gyro,left,PID_LOOP_TIME);
        gyroRightPID = new PIDController(Pg,Ig,Dg,gyro,right,PID_LOOP_TIME);
             
        leftEncPID = new PIDController(Pe,Ie,De,leftEnc,left,PID_LOOP_TIME);
        rightEncPID = new PIDController(Pe,Ie,De,rightEnc,right,PID_LOOP_TIME);
       
        // soft limits: 0 to 90 degrees...
        gyroLeftPID.setInputRange(-90.0, 90.0);  
        gyroRightPID.setInputRange(-90.0, 90.0);        

        leftEncPID.setInputRange(-100.0,100.0);       // adjust?
        rightEncPID.setInputRange(-100.0,100.0);       // adjust?
        
        
        gyroLeftPID.setOutputRange(-0.7, 0.7);
        gyroRightPID.setOutputRange(-0.7, 0.7);
        
        leftEncPID.setOutputRange(-0.7,0.7);
        rightEncPID.setOutputRange(-0.7,0.7);
        // Timer        
        timer = new Timer();
        timer.reset();
        timer.stop();
        
        
    }
    // Manual Drive 
    public void manualDrive(double x, double y) {
        disablePID();
        mode = MANUAL_MODE;
        
        Components.robotDrive.drive(x, y);
        
    }
    // Drive to Specific distance in stright line
    public boolean driveToDistanceUsingEnc(double distance, double maxTimeOut){
        
        this.SETTLED_TIME = maxTimeOut;
        this.driveDistance(distance);
        while (!this.thereYet(distance)){
            
        }
        this.disablePID();
        mode = MANUAL_MODE;
        return true;
    }
    
    public boolean correctAngleUsingGyro(double angle,boolean correctUsingleftWheels, double maxTimeOut){
        
        this.SETTLED_TIME = maxTimeOut;
        this.autoAimPan(angle, correctUsingleftWheels);
        while (!this.thereYet(angle)){
            
        }
        this.disablePID();
        mode = MANUAL_MODE;
        return true;
    }
    /**
     * @param targetAngle Angle to drive in degrees/radians?
     * @param correctUsingleftWheels
     */
    public void autoAimPan(double targetAngle,boolean correctUsingleftWheels) {
        mode = GYRO_MODE;
        
        if(leftEncPID.isEnable()) leftEncPID.disable();
        if(rightEncPID.isEnable()) rightEncPID.disable();
        
        
           gyro.reset();
           timer.reset();
            timer.start();
            timerRunning = true;
            
       
        if(correctUsingleftWheels){
            if(!gyroLeftPID.isEnable()) gyroLeftPID.enable();
            if(!gyroRightPID.isEnable()) gyroLeftPID.disable();
            gyroLeftPID.setSetpoint(targetAngle);
        }else{
            if(!gyroLeftPID.isEnable()) gyroLeftPID.disable();
            if(!gyroRightPID.isEnable()) gyroLeftPID.enable();
            gyroRightPID.setSetpoint(targetAngle);
        }
        
        
    }
    
    /**
     * @param distance
     */
    public void driveDistance(double distance) {
        
        mode = ENCODER_MODE;
        gyroLeftPID.disable();
        gyroRightPID.disable();
        
            resetEncoders();
           
      
        if(!leftEncPID.isEnable()) leftEncPID.enable();
        if(!rightEncPID.isEnable()) rightEncPID.enable();
        timer.reset();
        timer.start();
        timerRunning = true;
        
        leftEncPID.setSetpoint(distance);      // Check direction
        rightEncPID.setSetpoint(distance);     // Check direction
        
    }
    
    public void resetGyro() {
        gyro.reset();
    }
    
    public double getGyroAngle() {
        return gyro.getAngle();
    }
    

    
    /**
     * @param wheelSide 0 for left, 1 for right. Incorrect side input will return a position value of 0.0.
     * @return Count from the encoder (since the last reset?).
     */
    public double getEncoderCount(int wheelSide) {
        if (wheelSide == 0) return leftEnc.getRaw();
        else if (wheelSide == 1) return rightEnc.getRaw();
        
        else return 0.0;
    }
    
    /**
     * @param wheelSide 0 for left, 1 for right. Incorrect side input will return a position value of 0.0.
     * @return Distance the encoder has recorded since the last reset, adjusted for the gear ratio.
     */
    public double getEncoderDistance(int wheelSide) {
        if (wheelSide == 0) return leftEnc.getDistance();
        else if (wheelSide == 1) return rightEnc.getDistance();
        else return 0.0;
    }
    
    public double getAvgEncoderDistance() {
        return (leftEnc.getDistance()+rightEnc.getDistance())/2.0;
    }
    
    public void resetEncoders() {
        leftEnc.reset();
        rightEnc.reset();
    }
    
    public double getTemperature() {
        return temperature.getVoltage();        // ADJUST THIS TO RETURN IN DEGREES...
    }
    
    public void disablePID() {
        if(gyroLeftPID.isEnable()) gyroLeftPID.disable();
        if(gyroRightPID.isEnable()) gyroRightPID.disable();
        if(leftEncPID.isEnable()) leftEncPID.disable();
        if(rightEncPID.isEnable()) rightEncPID.disable();
        
    }
    
    public boolean thereYet(double target) {
        boolean targetReached = onTarget(target);
        if (targetReached ||(timer.get() >= SETTLED_TIME) ){
            timer.stop();
            return true;
            
        }else{
            return false;
        }
        
        
    }
    
    private boolean onTarget(double target) {
        boolean toReturn = false, leftOnTarget, rightOnTarget;
        
        switch(mode) {
            case GYRO_MODE:
                System.out.println("gyro onTarget...");
                
                toReturn = gyro.getAngle()>=(target-((gyroTOLERANCE/100.0)*180.0)) &&
                gyro.getAngle()<=(target+((gyroTOLERANCE/100.0)*180.0));
                break;
            
            case ENCODER_MODE:
                System.out.println("encoder onTarget... left error: "+
                        (leftEnc.pidGet()-target)+"\tright error: "+
                        (rightEnc.pidGet()-(-target))+"\ttolerance: "+
                        ((encoderTOLERANCE/100.0)*5.0));
                leftOnTarget = (leftEnc.pidGet()>=(target- encoderTOLERANCE))
                && (leftEnc.pidGet()<=(target+ encoderTOLERANCE));
                
                rightOnTarget = (rightEnc.pidGet()>=(-target - encoderTOLERANCE))
                && (rightEnc.pidGet()<=(-target + encoderTOLERANCE));
                
                System.out.println("left onTarget? "+leftOnTarget);
                System.out.println("right onTarget? "+rightOnTarget);
                
                toReturn = leftOnTarget && rightOnTarget;
                
                break;
            case MANUAL_MODE:
                toReturn = true;
                break;
         
        }
        System.out.println("on target? "+toReturn);
        return toReturn;
    }
    

    
}

