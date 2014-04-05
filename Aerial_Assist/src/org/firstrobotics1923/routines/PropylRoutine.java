package org.firstrobotics1923.routines;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.Components;

/** 
 * A routine that performs actions based on relative time and has the ability to use encoders and the gyro for angle and distance
 * @author Makarand Pande
 * @version 1.0
 * @since 4/5/2014
 */
public class PropylRoutine {
   private static Timer timer = new Timer(); 
   private static final String seekTarget = new String("HOT"); 
   private static final boolean useVision = true;
   private static final boolean useGyro = false;
   private static final boolean useEncoder = false;   
   
   private static double currentTime = 0;
   private static boolean targetIsHot = false;
   
   private static final double distance = 36; //For encoder use, 36in   
   private static double []actionTime = new double[10];
   private static boolean []actions = new boolean[8];
   private static double Angle_Tolerance = 2.0;
   // Contstants for readability
   private static final int DROVE = 0;
   private static final int INTAKE_OUT = 1;
   private static final int SHOOTER_WHEEL_START = 2;
   private static final int SHOOTER_ANGLE_UP = 3;
   private static final int INTAKE_WHEEL_ROTATE_IN = 4;
   private static final int FIRE_INTAKE_IN = 5;
   private static final int RESET_POSITION = 6;
   private static final int ANGLE_CORRECTED = 7;
   
   
   public static void initialize() {
       for (int i = 0; i < actions.length; i++) {  //All actions set to false
            actions[i] = false;
       }
       
       timer.stop(); //Timer initialized (stopped, reset and started)
       timer.reset();
       timer.start();
       
       actionTime[0] = 10000; //Drive Time when done set to 10000 to prevent premature actions. Reset to actual time when done driving
       
       targetIsHot = false;   //Target is initialized to cold
   }
   
   public static void routine() {
       currentTime = timer.get();     //Sets the current time every iteration       
       
        //Auton Drive                  Drives For 1.5s forward at 0.7mag OR uses encoders to drive "distance" for a max of 2s 
       if (useEncoder) {
           if (!actions[DROVE]) {               
               Components.driveTrain.driveToDistanceUsingEnc(distance, 2.0);
               actions[DROVE] = true;
               actionTime[DROVE] = currentTime;
           }
       } else {
           if (currentTime < 1.5){ 
                Components.robotDrive.drive(-0.7, -0.7);
            } else {
                Components.robotDrive.drive(0d, 0d);
                actions[DROVE] = true;
                actionTime[DROVE] = currentTime;                     //Action Time 0 set to when drive stops (~1.5)
           }
       } //End Auton Driving
       
       try { //If the target is hot, define it as hot
            if(useVision){
                if (Components.table.getString("Hot_Target").equals(seekTarget) && (actionTime[0] >= 1.5)){ //If hot and past drive time
                    System.out.println(Components.table.getString("Hot_Target")); 
                    targetIsHot = true;                    
                }
            } else {
                    targetIsHot = false;                    
            }
            Components.sfxDashboard.sfxTargetHotorNot = targetIsHot;            
        } catch(Exception e){
            System.out.println("Exception Thrown: " + e.toString());
        }
       
       //v Actions v
       if (!actions[INTAKE_OUT] && actions[DROVE]) { //Action 1: Intake Angle Out after reached shooting dist
           Components.intakeSystem.activate();
           actions[INTAKE_OUT] = true;
           actionTime[INTAKE_OUT] = currentTime;
       }
       if(useGyro && actions[INTAKE_OUT] && !actions[ANGLE_CORRECTED]){
       // Check if Robot heading changed
       double angleMoved = Components.DriveGyro.getAngle();
       System.out.println("Angle Changed by : " + angleMoved);
            if(Math.abs(angleMoved) >= Angle_Tolerance) {
                Components.driveTrain.correctAngleUsingGyro(-angleMoved,true, 1.0);   //0? CHECK THIS          
            }
            actions[ANGLE_CORRECTED] = true;
            actionTime[ANGLE_CORRECTED] = currentTime; 
       }
       if (!actions[SHOOTER_WHEEL_START] && currentTime >= 0.5) { //Action Two: Start Shooter at ~0.5s
           Components.shooterSystem.activate();
           actions[SHOOTER_WHEEL_START] = true;
           actionTime[SHOOTER_WHEEL_START] = currentTime;
       } 
       if (!actions[SHOOTER_ANGLE_UP] && actions[DROVE] ) { //Action Three: Shooter Angle up if intake out and 1s past drive
           Components.shooterAngleSystem.activate();
           actions[SHOOTER_ANGLE_UP] = true;
           actionTime[SHOOTER_ANGLE_UP] = currentTime;
       }
       if (!actions[INTAKE_WHEEL_ROTATE_IN] && actions[INTAKE_OUT]) { //Action Four: Intake Wheels in if intake angled back and drive is over
           Components.intakeSystem.reverseMotor();
           actions [INTAKE_WHEEL_ROTATE_IN] = true;
           actionTime[INTAKE_WHEEL_ROTATE_IN] = currentTime;
       }
       if (!actions[FIRE_INTAKE_IN] && actions[INTAKE_WHEEL_ROTATE_IN] && (currentTime >= actionTime[SHOOTER_WHEEL_START]+1.5) &&  (targetIsHot || (currentTime >= 7.5))) { //Action Five: Fire (Intake Angle In) if Shooter up, intake out, wheels on for 2.5, intake wheels in, drive done and hot or past time
           Components.intakeSystem.deactivate();
           actions[FIRE_INTAKE_IN] = true;
           actionTime[FIRE_INTAKE_IN] = currentTime;
       }
       if (!actions[RESET_POSITION] && actions[FIRE_INTAKE_IN] && currentTime >= (actionTime[FIRE_INTAKE_IN] + 2.0)) { //Action Six: Intake System Angle In, Shooter Turns off, Intake Motor Turns Off, Shooter Angle Down 2.0s after shot
           Components.intakeSystem.deactivate();
           Components.shooterSystem.stop();
           Components.intakeSystem.deactivateMotor();
           Components.shooterAngleSystem.deactivate();
           actions[RESET_POSITION] = true;
           actionTime[RESET_POSITION] = currentTime;
       }
   }
   
   /**
    * Stops and Resets Auton Timer
    */
   public static void stop() {             
       timer.stop();
       timer.reset();
   }
}
