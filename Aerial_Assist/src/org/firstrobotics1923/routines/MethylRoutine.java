package org.firstrobotics1923.routines;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.Components;

/**
 *  A Routine that drives into range, checks if hot, if not hot, waits, then fires, if hot, fires right away
 *       UNUSABLE!!!!! ROBOREALM NO LONGER PASSES TARGET DISTANCE
 * @author Pavan Hegde
 * @version 1.0
 * @since Mar. 11, 2014
 */
public class MethylRoutine {
    private static final Timer timer = new Timer();
    private static final String seekTarget = "HOT";
    private static final int minDistance = 96;    //Max and Min distance from target in inches to shoot
    private static final int maxDistance = 170;
    
    private static boolean useVision = true;
    private static boolean targetIsHot = false;
    private static boolean []actions = new boolean[10]; //  action 9 dist reached
    
    private static double currentTime = 0;
    private static double timeWhenFired = 100000;    
    private static double distanceToTarget = -1;
    private static double correction = 0.0175;
    
    // Variables from dashboard for use with distance time tunning.
    private static double var1 = 0.5;
    private static double var2 = 0.5;
    private static double var3 = 0.5;
    private static double var4 = 0.5;
    private static double var5 = 0.5;
    private static double var6 = 0.5;
    private static boolean useDashboardVariables = false;
    
    
    public static void start() {                     //Initializes Various Variables
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
        
        try { //Try getting the distance to target via network tables, otherwise set it to -1
            if (useVision) {
                distanceToTarget = Components.table.getNumber("TARGET_DISTANCE");  
                Components.sfxDashboard.sfxDistanceFromTarget = distanceToTarget;
            }
        } catch(Exception e){
            System.out.println("Open RoboRealm, Exception thrown: " + e.toString());
            distanceToTarget = -1;            
        }
        
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
       
        //Auton Drive
        if(useVision && currentTime < 1.5) {  //If using vision, drive into range, else drive for 1.5s then stop driving
            if (distanceToTarget <= 0) {
                distanceToTarget = maxDistance + 1;
            }
            
            if (distanceToTarget >= maxDistance) {      //If distanceToTarget is greater than max distance to target to score, drive forward at 0.7mag
                Components.robotDrive.drive(-0.7 + correction, -0.7 - correction);
            } else if (distanceToTarget <= minDistance) { // If dTT is less than min, drive back at 0.6 mag
                Components.robotDrive.drive(0.6, 0.6);
            } else {
                Components.robotDrive.drive(0.0,0.0);
                actions[9] = true;
            }
        } else if (timer.get() < 1.5){
            Components.robotDrive.drive(-0.7, -0.7);
        } else {
            Components.robotDrive.drive(0d, 0d);
            actions[9] = true;
        } //End Auton Driving
      
        if(!actions[0] && currentTime >= 0) { //Action 1: Intake Angle Out
            Components.intakeSystem.activate();
            actions[0] = true;
        } else if(!actions[1] && currentTime >= 0.5) { //Action 2: Start Up Shooter
            Components.shooterSystem.activate();
            actions[1] = true;
        } else if(!actions[2] && actions[0] && currentTime >= 2.5) { //Action 3: Shooter Angle up if Intake Out
            Components.shooterAngleSystem.activate();
            actions[2] = true;
        } else if(!actions[3] && currentTime >= 2.5) { //Action 4: Intake Wheels Rotate In
            Components.intakeSystem.reverseMotor();
            actions[3] = true;
        } else if(!actions[4] && actions[2] && currentTime >= 3.5 && (targetIsHot || currentTime >= 5.5)) { //Action 5: Fire (Intake Angle in) if target is hot or time past 6.5s
            Components.intakeSystem.deactivate();
            actions[4] = true;
            timeWhenFired = currentTime; //Shooter fired at "timeWhenFired"
        } else if(!actions[5] && ((currentTime >= timeWhenFired + 1) || currentTime >= 9.9)) { //Action 6:Blank
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
