package org.firstrobotics1923;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
//import static org.firstrobotics1923.Components.sfxDashboard;

import org.firstrobotics1923.routines.*;

//import org.firstrobotics1923.util.Dashboard;
import org.firstrobotics1923.util.XboxController;
//import org.firstrobotics1923.Components;

/**
 * The Core Code for FRC Team 1923's "Aerial Assist" Robot 
 * 
 * @author Pavan Hegde & whoever else touched this without commenting
 * @version 1.3
 * @since Mar. 11, 2014
 */
public class AssistRobot extends IterativeRobot{ 
    
    private XboxController xbc = Components.operatorControl;
    
    private boolean[] justPressed = new boolean[14];       //Array to store Xbox button input
    private boolean[] triggers = new boolean[2]; //Array to store Xbox trigger input

    private String currentAutonRoutine = "propyl";

    private boolean compressorOn = false;
        
    private boolean indexerUp = false;
    private boolean waitingForRelease = false;
    private boolean intakeMotorAllowed = true;
    
    public void robotInit(){
        
        Components.init();
        this.compressorOn = false;
        Components.shooterAngleSystem.stop();
        Components.intakeSystem.stop();
        Components.driveTrain.init();
       
      //  Components.sfxDashboard.smartDashboardUpdate();
    }
    
    
    /**
     * Ensures that all systems are disabled
     */
    public void disabledInit() {
        Components.shooterSystem.stop();
        Components.shooterAngleSystem.stop();
        Components.robotDrive.stop();
        Components.intakeSystem.stop();
        //EventBus.instance.clear();
    }
    
    public void disabledPeriodic() {
        //... ... ... ... ... Yes ... ... ...
    }

    /**
     * Called once at the start of Auton
     */
    public void autonomousInit() {
      //  Components.sfxDashboard.smartDashboardUpdate();
        if (currentAutonRoutine.equalsIgnoreCase("methyl")){ //UNUSABLE!!!!! ROBOREALM NO LONGER PASSES TARGET DISTANCE
            MethylRoutine.start();
        } else if (currentAutonRoutine.equalsIgnoreCase("ethyl")){
            EthylRoutine.start();
        } else if (currentAutonRoutine.equalsIgnoreCase("butyl")) {
            ButylRoutine.initialize();
        }else if (currentAutonRoutine.equalsIgnoreCase("propyl")) {
            PropylRoutine.initialize();
        }
    }
    
    /**
     * Called periodically in auton
     */
    public void autonomousPeriodic(){                
        if (currentAutonRoutine.equalsIgnoreCase("methyl")){ //UNUSABLE!!!! ROBOREALM NO LONGER PASSED TARGET DISTANCE
            MethylRoutine.routine();
        } else if (currentAutonRoutine.equalsIgnoreCase("ethyl")){
            EthylRoutine.routine();
        } else if (currentAutonRoutine.equalsIgnoreCase("butyl")) {
            ButylRoutine.routine();
        }else if (currentAutonRoutine.equalsIgnoreCase("propyl")) {
            PropylRoutine.routine();
        }
        Components.sfxDashboard.smartDashboardUpdate();
    }
    
    /**
     * Initializes required things before teleop
     */
    public void teleopInit() {
        if (currentAutonRoutine.equals("methyl")){     //DO NOT USE METHYL!!!! NO MORE TARGET DISTANCE... No point in it
            MethylRoutine.stop();
        } else if (currentAutonRoutine.equalsIgnoreCase("ethyl")){
            EthylRoutine.stop();
        } else if (currentAutonRoutine.equalsIgnoreCase("butyl")) {
            ButylRoutine.stop();
        } else if (currentAutonRoutine.equalsIgnoreCase("propyl")) {
            PropylRoutine.stop();
        }
      
        
        //EventBus.instance.clear();        
    }
    
    /**
     * All of the periodically called teleop-functions (eg. input)
     */
    public void teleopPeriodic() {
        { //Driving Scope : Currently Full Joystick Control, no correction
            Components.robotDrive.drive(Components.leftStick.getCoalescedY(), Components.rightStick.getCoalescedY());
        } //End Driving Scope
     
        {  //Shooter Scope
           if (Components.operatorControl.getButton(XboxController.Button.Start) & !justPressed[XboxController.Button.Start.value]) {    //Start button turns on shooter
               //EventBus.instance.push(new ShooterStartEvent());
               Components.shooterSystem.activate();
               justPressed[XboxController.Button.Start.value] = true;
           } else {
               justPressed[XboxController.Button.Start.value] = false;
           }
           
           if (Components.operatorControl.getButton(XboxController.Button.Back) & !justPressed[XboxController.Button.Back.value]) {     //Back Button stops the shooter
               //EventBus.instance.push(new ShooterStopEvent());
               Components.shooterSystem.stop();
               justPressed[XboxController.Button.Back.value] = false;
           } else {
               justPressed[XboxController.Button.Back.value] = false;
           }
           

        } //End Shooter Scope
        
        { // Shooter Angle Scope
           if ((Components.operatorControl.getRawAxis(3) < 0) && !triggers[0]) {         //Left Trigger lowers the shooter angle
               //EventBus.instance.push(new ShooterLowerAngleEvent());
               Components.shooterAngleSystem.deactivate();
               triggers[0] = true;
           } else {
               triggers[0] = false;
           } 
           
           if ((Components.operatorControl.getRawAxis(3) > 0) && !triggers[1]) {         //Right Trigger raises the shooter angle
               //EventBus.instance.push(new ShooterRaiseAngleEvent());
               Components.shooterAngleSystem.activate();
               //System.out.println("RT pressed");
               triggers[1] = true;
           } else {
               triggers[1] = false;
            }  
        } // End Shooter Angle Scope
         
        { //Intake Scope            
            //Indexer Control: While holding A keep indexer up
            if (Components.operatorControl.getButton(XboxController.Button.A)) {
                indexerUp = true;
            } else {
                indexerUp = false;
                if (waitingForRelease) {
                    Components.indexer.deactivate();
                    waitingForRelease = false;
                    intakeMotorAllowed = true;
                }
            }//Indexer end
           
            if (Components.operatorControl.getButton(XboxController.Button.B) & !justPressed[XboxController.Button.B.value]) {         //B angles intake in
               //EventBus.instance.push(new IntakeAngleInEvent());
               Components.intakeSystem.deactivate();
               justPressed[XboxController.Button.B.value] = true;
               //Components.shooterSystem.resetTimer();
               if (indexerUp) { //If indexer stays up
                   Components.indexer.activate(); //Keep Indexer Up
                   waitingForRelease = true;
                   intakeMotorAllowed = false; //Intake Motor Off
               } else {
                   Components.indexer.deactivate(); //Indexer 
               }
           } else {
               justPressed[XboxController.Button.B.value] = false;
           }
           
           if (Components.operatorControl.getButton(XboxController.Button.X) & !justPressed[XboxController.Button.X.value]) {         //X angles intake out
               //EventBus.instance.push(new IntakeAngleOutEvent());
               Components.intakeSystem.activate();
               justPressed[XboxController.Button.X.value] = true;
               Components.indexer.activate(); //Indexer Up when extending intake system
           } else {
               justPressed[XboxController.Button.X.value] = false;
           }
           
           if ((Components.operatorControl.getButton(XboxController.Button.Y) & !justPressed[XboxController.Button.Y.value]) || !intakeMotorAllowed) {         //Y turns off the intake motor
               //EventBus.instance.push(new IntakeMotorOffEvent());
               Components.intakeSystem.deactivateMotor();
               justPressed[XboxController.Button.Y.value] = true;
           } else {
               justPressed[XboxController.Button.Y.value] = false;
           }
           
           if (Components.operatorControl.getButton(XboxController.Button.LB) & !justPressed[XboxController.Button.LB.value] & intakeMotorAllowed) {         //Left Bumper starts the intake in reverse
               //EventBus.instance.push(new IntakeMotorForwardEvent());
               Components.intakeSystem.forwardMotor();
               justPressed[XboxController.Button.LB.value] = true;
           } else {
               justPressed[XboxController.Button.LB.value] = false;
           }
           
           if (Components.operatorControl.getButton(XboxController.Button.RB) & !justPressed[XboxController.Button.RB.value] & intakeMotorAllowed) {         //Right Bumper starts the intake forward
               //EventBus.instance.push(new IntakeMotorReverseEvent());
               Components.intakeSystem.reverseMotor();
               justPressed[XboxController.Button.RB.value] = true;
           } else {
               justPressed[XboxController.Button.RB.value] = false;
           }           
           
        } //End Intake Scope
        
        { //Compressor Scope
            boolean safety = Components.compressorSafety.get();
            if (!safety && !compressorOn) {
                //EventBus.instance.push(new CompressorOnEvent());
                Components.compressorSpike.set(Relay.Value.kForward);
                compressorOn = true;
            }else if (safety && compressorOn) {
                //EventBus.instance.push(new CompressorOffEvent());
                 Components.compressorSpike.set(Relay.Value.kOff);
                compressorOn = false;
            }
        } //End Compressor Scope
        
        {  //For Testing Gyro and Encoder ... TBD comment for match
            
            double angleMoved = Components.DriveGyro.getAngle();
            Components.sfxDashboard.Gyro_ANGLE = angleMoved;
            
           if (Components.operatorControl.getButton(XboxController.Button.RightClick) & !justPressed[XboxController.Button.RightClick.value]) {    //Start button turns on shooter
               // Call Gyro methods
                        
            Components.driveTrain.correctAngleUsingGyro(-angleMoved,true, 3.0);
             
           
               
               justPressed[XboxController.Button.A.value] = true;
           } else {
               justPressed[XboxController.Button.A.value] = false;
           }
           // Encode
           double leftEncDist = Components.DriveEncoderLeft.getDistance();
           double rightEncDist = Components.DriveEncoderRight.getDistance();
           boolean leftEncDirection = Components.DriveEncoderLeft.getDirection();
           boolean rightEncDirection = Components.DriveEncoderRight.getDirection();
           
            Components.sfxDashboard.Left_Encoder_Value = leftEncDist;
            Components.sfxDashboard.Right_Encoder_Value = rightEncDist;
            Components.sfxDashboard.Left_Encoder_Direction = leftEncDirection;
            Components.sfxDashboard.Right_Encoder_Direction = rightEncDirection;
            
            
           
           if (Components.operatorControl.getButton(XboxController.Button.LeftClick) & !justPressed[XboxController.Button.LeftClick.value]) {     //Back Button stops the shooter
               // Call Encoder methods
               Components.driveTrain.driveToDistanceUsingEnc(24.0, 3);
               justPressed[XboxController.Button.LeftClick.value] = false;
           } else {
               justPressed[XboxController.Button.LeftClick.value] = false;
           }
           

        } //End Testing Gyro and Encode Scope
        
       { //SFX Dashboard Scope
           
           Components.sfxDashboard.smartDashboardUpdate();
           
        } // SFX End Dashboard Scope
        
    }
}
