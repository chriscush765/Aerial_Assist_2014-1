package org.firstrobotics1923.routines;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.Components;
import org.firstrobotics1923.util.Coalescor;

/**
 *  A Routine that does some action that is unknown 
 * 
 * @author Someone else...  
 * @version 1.0
 * @since Sometime in March of 2013... 
 */
public class MoreTacticalRoutine {

//Passed as parameters
    private static int ballCount;// = 1;
    private static String seekTarget;// = "HOT";
    private static boolean useVisionForHotGoal;// = true;
    private static boolean useVisionForMovement;// = true;
    private static int minDistance;// = 96;    //Max and Min distance from target in inches to shoot
    private static int maxDistance;// = 170;
    private static int initialDistance;// = 228;
    private static double moveForwardSpeed;// = -0.7;
    private static double moveBackwardsSpeed;// = 0.6;
    private static double moveForwardNoCameraDuration;// = 1.5;
    private static double moveForwardMaxDuration;// = 1.5;
//End passed as parameters

    private static final Timer timer = new Timer();
    
    private static boolean targetIsHot = false;
    private static boolean []actions = new boolean[20];
    
    private static double currentTime = 0;
    private static double timeWhenFired = 100000;    
    private static double distanceToTarget = -1;
     
    public static void startValentine() { //Zero Ball, No Vision
        start( 0, "", false, false);     //ballCountIn, seekTargetIn, useVisionForHotGoalIn, useVisionForMovementIn    
    }
    
    public static void startMethyl() { //One Ball, Vision For Hot, Vision For Movement
         start( 1, "HOT", true, true);     //ballCountIn, seekTargetIn, useVisionForHotGoalIn, useVisionForMovementIn    
    }
    
    public static void startEthyl() { //One Ball, Vision For Hot, No Vision For Movement
        start( 1, "HOT", true, false);     //ballCountIn, seekTargetIn, useVisionForHotGoalIn, useVisionForMovementIn    
    }
    
    public static void startPropyl() { //One Ball, Vision For Hot, No Vision For Movement
        start( 2, "", false, false);     //ballCountIn, seekTargetIn, useVisionForHotGoalIn, useVisionForMovementIn    
    }
       
    public static void startFromDashboard() { //Mr. Pande - add your method here
        start(1, //ballCountIn, 
            "HOT", //seekTargetIn, 
            true, //useVisionForHotGoalIn,
            false, //useVisionForMovementIn,
            96, //minDistanceIn
            170, //maxDistanceIn
            228, //initialDistanceIn
            -0.7, //moveForwardSpeedIn
            0.6, //moveBackwardsSpeedIn
            1.5, //moveForwardNoCameraDurationIn
            1.5 //moveForwardMaxDurationIn
            );
        
    }
       
    public static void start(int ballCountIn, String seekTargetIn, boolean useVisionForHotGoalIn, boolean useVisionForMovementIn) {
        start(ballCountIn, 
            seekTargetIn, 
            useVisionForHotGoalIn,
            useVisionForMovementIn,
            96, //minDistanceIn
            170, //maxDistanceIn
            228, //initialDistanceIn
            -0.7, //moveForwardSpeedIn
            0.6, //moveBackwardsSpeedIn
            1.5, //moveForwardNoCameraDurationIn
            1.5 //moveForwardMaxDurationIn
            );
        
    }
    
    public static void start(
        int ballCountIn,
        String seekTargetIn,
        boolean useVisionForHotGoalIn,
        boolean useVisionForMovementIn,
        int minDistanceIn,
        int maxDistanceIn,
        int initialDistanceIn,
        double moveForwardSpeedIn,
        double moveBackwardsSpeedIn,
        double moveForwardNoCameraDurationIn,
        double moveForwardMaxDurationIn
        ) 
    {
        ballCount = ballCountIn;
        seekTarget = seekTargetIn;
        useVisionForHotGoal = useVisionForHotGoalIn;
        useVisionForMovement = useVisionForMovementIn;
        minDistance = minDistanceIn;
        maxDistance = maxDistanceIn;
        initialDistance = initialDistanceIn;
        moveForwardSpeed = moveForwardSpeedIn;
        moveBackwardsSpeed = moveBackwardsSpeedIn;
        moveForwardNoCameraDuration = moveForwardNoCameraDurationIn;
        moveForwardMaxDuration = moveForwardMaxDurationIn;

        int actionsLength = actions.length; //Defined to avoid recalculating
        for (int i = 0; i < actionsLength; i++) {
            actions[i] = false;
        }
        
        targetIsHot = false;
       
        timeWhenFired = 100000; //Do not change number - this is arbitrarily large to not be hit
        
        timer.stop();
        timer.reset();
        timer.start();
    }
    
    public static void stop() {
        timer.stop();
        timer.reset();
    }    
    
    public static void routine() {
        currentTime = timer.get(); //Sets the Current Time
        
        try { //Try getting the distance to target via network tables, otherwise set it to -1
            if (useVisionForMovement) {
                distanceToTarget = Components.table.getNumber("TARGET_DISTANCE");           
            }
        } catch(Exception e){
            System.out.println("Open RoboRealm, Exception thrown: " + e.toString());
            distanceToTarget = -1;            
        }
        
        try { //If the target is hot, define it as hot
            if(useVisionForHotGoal){ //If using vision for Hot goal
                if(Components.table.getString("Hot_Target").equals(seekTarget) && currentTime > 0.5){ //If hot 
                    System.out.println(Components.table.getString("Hot_Target")); 
                    targetIsHot = true;
                }
            } else {
                targetIsHot = true;
            }
        } catch(Exception e){
            System.out.println("Exception Thrown: " + e.toString());
        }
         
        //Auton Drive
        if(ballCount == 2){
            if(currentTime >= 0 && currentTime < 1.5){
                Components.robotDrive.drive(moveForwardSpeed, moveForwardSpeed);                
            }
            else if(currentTime >= 3.0 && currentTime < 5.0){
                Components.robotDrive.drive(moveBackwardsSpeed, moveBackwardsSpeed);                
            }
            else if(currentTime >= 7.0 && currentTime < 9.0){
                Components.robotDrive.drive(moveBackwardsSpeed, moveBackwardsSpeed);                
            }
            else{
                Components.robotDrive.drive(0.0, 0.0);                
            }
        }
        if(useVisionForMovement && currentTime < moveForwardMaxDuration) {  //If using vision, drive into range, else drive for 1.5s then stop driving
            if (distanceToTarget <= 0) {
                distanceToTarget = maxDistance + 1;
            } 
            
            if (distanceToTarget >= maxDistance) {      //If distanceToTarget is greater than max distance to target to score, drive forward at 0.7mag
                Components.robotDrive.drive(moveForwardSpeed, moveForwardSpeed);
            } else if (distanceToTarget <= minDistance) { // If dTT is less than min, drive back at 0.6 mag
                Components.robotDrive.drive(moveBackwardsSpeed, moveBackwardsSpeed);
            } else {
                Components.robotDrive.drive(0.0,0.0);
            }
        } else if (!useVisionForMovement && currentTime < moveForwardNoCameraDuration){
            Components.robotDrive.drive(moveForwardSpeed, moveForwardSpeed);
        } else {
            Components.robotDrive.drive(0d, 0d);
        } //End Auton Driving
      
        if(ballCount == 0) //Zero Ball Autonomous
        {
            //Smile for the camera - don't do anything
        }
        else if(ballCount == 1) //One Ball Autonomous
        {
            if(!actions[0] && currentTime >= 0) { //Action 1: Intake Angle Out
                Components.intakeSystem.activate();
                actions[0] = true;
            } else if(!actions[1] && actions[0] && currentTime >= 0.5) { //Action 2: Start Up Shooter
                Components.shooterSystem.activate();
                actions[1] = true;
            } else if(!actions[2] && actions[0] && currentTime >= 1.3) { //Action 3: Shooter Angle up if Intake Out
                Components.shooterAngleSystem.activate();
                actions[2] = true;
            } else if(!actions[3] && currentTime >= 1.1) { //Action 4: Intake Wheels Rotate In
                Components.intakeSystem.reverseMotor();
                actions[3] = true;
            } else if(!actions[4] && currentTime >= 2.5 && (targetIsHot || currentTime >= 7.0)) { //Action 5: Fire (Intake Angle in) if target is hot or time past 6.5s
                Components.intakeSystem.deactivate();
                actions[4] = true;
                timeWhenFired = currentTime; //Shooter fired at "timeWhenFired"
            } else if(!actions[5] && ((currentTime >= timeWhenFired + 1) || currentTime >= 9.9)) { //Action 6: Shooter Lowered after shot
                Components.shooterAngleSystem.stop();
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
        else if(ballCount == 2) //Two Ball Autonomous
        {
            if(!actions[0] && currentTime >= 0) { //Action 1: Intake Angle Out
                Components.intakeSystem.activate();
                actions[0] = true;
            } else if(!actions[1] && actions[0] && currentTime >= 0.5) { //Action 2: Start Up Shooter
                Components.shooterSystem.activate();
                actions[1] = true;
            } else if(!actions[2] && currentTime >= 1.1) { //Action 4: Intake Wheels Rotate In
                Components.intakeSystem.reverseMotor();
                actions[2] = true;
            } else if(!actions[3] && actions[0] && currentTime >= 1.3) { //Action 3: Shooter Angle up if Intake Out
                Components.shooterAngleSystem.activate();
                actions[3] = true;
            } else if(!actions[4] && currentTime >= 2.5) { //Action 5: Fire (Intake Angle in) if target is hot or time past 6.5s
                Components.intakeSystem.deactivate();
                actions[4] = true;
                timeWhenFired = currentTime; //Shooter fired at "timeWhenFired"
            } else if(!actions[5] && currentTime >= 3.0) { //Action 6: Shooter Lowered after shot
                Components.shooterAngleSystem.stop();
                actions[5] = true;
            } else if(!actions[6] && currentTime >= 3.01) { //Action 8: Shooter Off
                Components.shooterSystem.stop();
                actions[6] = true;
            } else if(!actions[7] && currentTime >= 7.0) { //Action 2: Start Up Shooter
                Components.shooterSystem.activate();
                actions[7] = true;
            } else if(!actions[8] && currentTime >= 7.6) { //Action 4: Intake Wheels Rotate In
                Components.intakeSystem.reverseMotor();
                actions[8] = true;
            } else if(!actions[9] && currentTime >= 7.8) { //Action 3: Shooter Angle up if Intake Out
                Components.shooterAngleSystem.activate();
                actions[9] = true;
            } else if(!actions[10] && currentTime >= 9.0) { //Action 5: Fire (Intake Angle in) if target is hot or time past 6.5s
                Components.intakeSystem.deactivate();
                actions[10] = true;
                timeWhenFired = currentTime; //Shooter fired at "timeWhenFired"
            } else if(!actions[11] && actions[10] && currentTime >= 9.7) { //Action 6: Shooter Lowered after shot
                Components.shooterAngleSystem.stop();
                actions[11] = true;
            } else if(!actions[12] && actions[10] && currentTime >= 9.71) { //Action 7: Intake System In again
                Components.intakeSystem.deactivate();
                actions[12] = true;
            } else if(!actions[13] && actions[10] && currentTime >= 9.72) { //Action 8: Shooter Off
                Components.shooterSystem.stop();
                actions[13] = true;
            } else if (!actions[14] && actions[10] && currentTime >= 9.73) { //Intake Motor Off
                Components.intakeSystem.deactivateMotor();
                actions[14] = true;
            }
        }
    }
            
}
