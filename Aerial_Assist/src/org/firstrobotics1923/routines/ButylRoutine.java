package org.firstrobotics1923.routines;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.Components;

/** 
 * A hopefully clean routine that drives forward for 1.5s, checks for hot target for 1s, all the while performing actions based on relative time
 * @author Pavan Hegde
 * @version 1.0
 * @since 3/24/2014
 */
public class ButylRoutine {
   private static Timer timer = new Timer(); 
   private static final String seekTarget = new String("HOT");
   private static final boolean useVision = true;
   
   private static double currentTime = 0;
   private static boolean targetIsHot = false;
   
   private static double []actionTime = new double[10];
   private static boolean []actions = new boolean[8];
   
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
       currentTime = timer.get();
       
       //Auton Drive                  Drives For 1.5s forward at 0.7mag 
        if (currentTime < 1.5){ 
            Components.robotDrive.drive(-0.7, -0.7);
        } else {
            Components.robotDrive.drive(0d, 0d);
            actionTime[0] = currentTime;                     //Action Time 0 set to when drive stops (~1.5)
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
       if (!actions[0]) { //Action 1: Intake Angle Out 
           Components.intakeSystem.activate();
           actions[0] = true;
           actionTime[1] = currentTime;
       } else if (!actions[1] && actions[0] && currentTime >= (actionTime[1] + 0.5)) { //Action Two: Start Shooter at ~0.5s
           Components.shooterSystem.activate();
           actions[1] = true;
           actionTime[2] = currentTime;
       } else if (!actions[2] && actions[0] && currentTime >= (actionTime[0] + 1.0)) { //Action Three: Shooter Angle up if intake out and 1s past drive
           Components.shooterAngleSystem.activate();
           actions[2] = true;
           actionTime[3] = currentTime;
       } else if (!actions[3] && actions[0] && currentTime >= (actionTime[0] + 0.5)) { //Action Four: Intake Wheels in if intake angled back and drive is over
           Components.intakeSystem.reverseMotor();
           actions [3] = true;
           actionTime[4] = currentTime;
       } else if (!actions[4] && actions[0] && actions[2] && actions[3] && (currentTime >= actionTime[2] + 2.4) && (currentTime >= actionTime[0] + 0.9) && (targetIsHot || (currentTime >= actionTime[0] + 4.0))) { //Action Five: Fire (Intake Angle In) if Shooter up, intake out, wheels on for 2.5, intake wheels in, drive done and hot or past time
           Components.intakeSystem.deactivate();
           actions[4] = true;
           actionTime[5] = currentTime;
       } else if (!actions[5] && actions[4] && currentTime >= (actionTime[5] + 2.0)) { //Action Six: Intake System Angle In, Shooter Turns off, Intake Motor Turns Off, Shooter Angle Down 2.0s after shot
           Components.intakeSystem.deactivate();
           Components.shooterSystem.stop();
           Components.intakeSystem.deactivateMotor();
           actions[5] = true;
           actionTime[6] = currentTime;
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
