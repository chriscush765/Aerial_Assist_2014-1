package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import org.firstrobotics1923.Components;

/**
 * The intake system on the robot
 * 
 * @author Aryak Pande , Pavan Hedge, Kartik Vaidya, Prasanth Yedlapalli, Saikiran Nakka, DJ Wadhwa, Nithin Suresh
 * @version 1.5
 * @since Jan 26, 2014
 */
public class IntakeSystem extends PneumaticSystem {
 
    private final Solenoid pistonControllerOne,pistonControllerTwo; 
    private final Victor intakeMotorController;
    private double intakePower = 1.0;
    
    /**
     * Creates an IntakeSystem with parameters pistonControllerOne, pistonControllerTwo, and motorController
     * 
     * @param pistonControllerOne
     *                          One of Two Solenoid objects that controls the shooter intake angle  
     * @param pistonControllerTwo
     *                          One of Two Solenoid objects that controls the shooter intake angle 
     * @param intakeSpike 
     *                          The Spike which controls the motors on the intake system
     */
    public IntakeSystem(Solenoid pistonControllerOne, Solenoid pistonControllerTwo, Victor intakeSpike){        
        this.pistonControllerOne = pistonControllerOne;
        this.pistonControllerTwo = pistonControllerTwo;
        this.intakeMotorController = intakeSpike;
    }
    
    /**
     * Extends the pistons angling the intake system
     */
    public void activate(){       
        //System.out.println("");
        pistonControllerOne.set(true);
        pistonControllerTwo.set(false);
        
       Components.sfxDashboard.IntakeAngle_Command = true;
       Components.sfxDashboard.IntakePiston_1 = true;
       Components.sfxDashboard.IntakePiston_2 = false;
    }
    
    /**
     * Retracts the piston and thus the Intake system
     */
    public void deactivate(){
        pistonControllerOne.set(false);
        pistonControllerTwo.set(true); 
        
       Components.sfxDashboard.IntakeAngle_Command = false;
       Components.sfxDashboard.IntakePiston_1 = false;
       Components.sfxDashboard.IntakePiston_2 = true;
    }
    
    /**
     * Starts the motors on the Intake system forward
     */
    public void forwardMotor() {
        this.intakeMotorController.set(-intakePower);
        Components.sfxDashboard.IntakeWheel_Command = -intakePower;
         Components.sfxDashboard.Victor_9 = -intakePower;
        
        
    }
    
    /**
     * Starts the motors on the Intake system in reverse
     */
    public void reverseMotor() {
        this.intakeMotorController.set(intakePower);   
        
        Components.sfxDashboard.IntakeWheel_Command = intakePower;
         Components.sfxDashboard.Victor_9 = intakePower;
    }
    
    /**
     * Turns off the motor
     */
    public void deactivateMotor() { 
        this.intakeMotorController.set(0.0);
        this.intakeMotorController.stopMotor();
        
        Components.sfxDashboard.IntakeWheel_Command = 0.0;
         Components.sfxDashboard.Victor_9 = 0.0;
    }
    
    /**
     * Retracts the piston and turns off the motor
     */
    public void stop(){
        this.deactivate();
        intakeMotorController.set(0.0);
        
        Components.sfxDashboard.IntakeWheel_Command = 0.0;
         Components.sfxDashboard.Victor_9 = 0.0;
    }
}