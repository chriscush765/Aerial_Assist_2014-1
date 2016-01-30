package org.firstrobotics1923;

import edu.wpi.first.wpilibj.IterativeRobot;

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
        Components.driveTrain.init();
       
      //  Components.sfxDashboard.smartDashboardUpdate();
    }
    
    
    /**
     * Ensures that all systems are disabled
     */
    public void disabledInit() {
        Components.robotDrive.stop();
    }
    
    public void disabledPeriodic() {
        //... ... ... ... ... Yes ... ... ...
    }

    /**
     * Called once at the start of Auton
     */
    public void autonomousInit() {

    }
    
    /**
     * Called periodically in auton
     */
    public void autonomousPeriodic(){                

    }
    
    /**
     * Initializes required things before teleop
     */
    public void teleopInit() {
      
        
        //EventBus.instance.clear();        
    }
    
    /**
     * All of the periodically called teleop-functions (eg. input)
     */
    public void teleopPeriodic() {
            Components.robotDrive.drive(Components.leftStick.getCoalescedY(), Components.rightStick.getCoalescedY());
     
    }
}
