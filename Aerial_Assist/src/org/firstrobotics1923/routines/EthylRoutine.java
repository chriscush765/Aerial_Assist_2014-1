package org.firstrobotics1923.routines;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.Components;

/**
 * A Routine that Drives forward for 1.5s, checks the hot target at 1s and fires based on the hot target or if a set time is reached
 * 
 * @author Pavan Hegde
 * @version 1.0
 * @since Mar. 9, 2014
 */
public class EthylRoutine {
    private static final Timer timer = new Timer();
    private static final String seekTarget = "HOT";
    
    private static boolean useVision = true;
    private static boolean targetIsHot = false;
    private static boolean []actions = new boolean[10];
    
    private static double currentTime = 0;
    private static double timeWhenFired = 100000;    
    // Variables from dashboard for use with distance time tunning.
    private static double var1 = 0.5;
    private static double var2 = 0.5;
    private static double var3 = 0.5;
    private static double var4 = 0.5;
    private static double var5 = 0.5;
    private static double var6 = 0.5;
    private static boolean useDashboardVariables = false;
    
        
    public static void start() {
        for (int i = 0; i < actions.length; i++) {
            actions[i] = false;
        }
    
        targetIsHot = false;
       
        timeWhenFired = 100000; //Do not change number
        
        timer.stop();
        timer.reset();
        timer.start();
        
        // initialize variables from dashboard
        if (Components.sfxDashboard.useDashboardVar){
            var1 = Components.sfxDashboard.var_1Value;
            var2 = Components.sfxDashboard.var_2Value;
            var3 = Components.sfxDashboard.var_3Value;
            var4 = Components.sfxDashboard.var_4Value;
            var5 = Components.sfxDashboard.var_5Value;
            var6 = Components.sfxDashboard.var_6Value;
            
        }
    }
    
    public static void stop() {
        timer.stop();
        timer.reset();
    }    
    
    public static void routine() {
        currentTime = timer.get(); //Sets the Current Time
                
       try { //If the target is hot, define it as hot
            if(useVision){
                if(Components.table.getString("Hot_Target").equals(seekTarget) && currentTime > 1.5){ //If hot and left
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
        
        //Auton Drive                  Drives For 1.5s forward at 0.7mag 
        if (timer.get() < 1.5){ 
            Components.robotDrive.drive(-0.7, -0.7);
        } else {
            Components.robotDrive.drive(0d, 0d);
            actions[9] = true;
        } //End Auton Driving
      
        if(!actions[0] && currentTime >= 0) { //Action 1: Intake Angle Out
            Components.intakeSystem.activate();
            actions[0] = true;
        } else if(!actions[1] && actions[0] && currentTime >= 0) { //Action 2: Start Up Shooter
            Components.shooterSystem.activate();
            actions[1] = true;
        } else if(!actions[2] && actions[0] && currentTime >= 2.2) { //Action 3: Shooter Angle up if Intake Out
            Components.shooterAngleSystem.activate();
            actions[2] = true;
        } else if(!actions[3] && currentTime >= 2.5) { //Action 4: Intake Wheels Rotate In
            Components.intakeSystem.reverseMotor();
            actions[3] = true;
        } else if(!actions[4] && actions[2] && currentTime >= 3.0 && (targetIsHot || currentTime >= 7.0)) { //Action 5: Fire (Intake Angle in) if target is hot or time past 6.5s
            Components.intakeSystem.deactivate();
            actions[4] = true;
            timeWhenFired = currentTime; //Shooter fired at "timeWhenFired"
        } else if(!actions[5] && ((currentTime >= timeWhenFired + 1) || currentTime >= 9.9)) { //Action 6: Blank
            actions[5] = true;
        } else if(!actions[6] && ((currentTime >= timeWhenFired + 1.1) || currentTime >= 9.91)) { //Action 7: Intake System In again
            Components.intakeSystem.deactivate();
            actions[6] = true;
        } else if(!actions[7] && ((currentTime >= timeWhenFired + 1.11) || currentTime >= 9.92)) { //Action 8: Shooter Off
            Components.shooterSystem.stop();
            actions[7] = true;
        } else if (!actions[8] && ((currentTime >= timeWhenFired + 1.12) || currentTime >= 9.93)) { //Intake Motor Off
            Components.intakeSystem.deactivateMotor();
            actions[8] = true;
        }
    }
}