package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.Timer;
import org.firstrobotics1923.util.DefaultConfig;
import org.firstrobotics1923.util.MotorGroup;
import org.firstrobotics1923.Components;

/**
 * The Shooter System
 * 
 * @author Kartik Vaidya, Pavan Hegde 
 * @version 1.4
 * @since Jan 26, 2014 
 *
 */
public class ShooterSystem implements System {

    private final MotorGroup rightWheels, leftWheels;
    private double speed = DefaultConfig.SHOOTER_SPEED;
    private Timer shootTimer = new Timer();
    
    /**
     * Creates a ShooterSystem with A set of front and back wheels
     * @param backWheels
     *              The motor group made of the Victors controlling the back wheels
     * @param frontWheels 
     *              The motor group made of the Victors controlling the front wheels
     */
    public ShooterSystem(MotorGroup leftWheels, MotorGroup rightWheels) {
       this.leftWheels = leftWheels;
       this.rightWheels = rightWheels;
    }
    
    /**
    * Starts Motors at set speed 
    */
    public void activate() {
        rightWheels.set(speed);
        leftWheels.set(-speed);
        
        Components.sfxDashboard.ShooterWheel_Command = true;
        Components.sfxDashboard.Victor_5 = speed;
        Components.sfxDashboard.Victor_6 = speed;
        Components.sfxDashboard.Victor_7 = -speed;
        Components.sfxDashboard.Victor_8 = -speed;
                
        this.shootTimer.stop();
        this.shootTimer.reset();
        this.shootTimer.start();
    }
  
    /**
    * Stops the Motors on the shooter
    */
    public void stop() {
        rightWheels.set(0.0);
        rightWheels.disable();
        leftWheels.set(0.0);
        leftWheels.disable();
        
        Components.sfxDashboard.ShooterWheel_Command = false;
        Components.sfxDashboard.Victor_5 = 0.0;
        Components.sfxDashboard.Victor_6 = 0.0;
        Components.sfxDashboard.Victor_7 = 0.0;
        Components.sfxDashboard.Victor_8 = 0.0;
        
        this.shootTimer.stop();
        this.shootTimer.reset();
        this.shootTimer.start(); //Keep This here
        this.shootTimer.stop();
    }
    
    public double getShootTime() {
        return shootTimer.get();
    }
    
    public void resetTimer() {
        this.shootTimer.stop();
        this.shootTimer.reset();
        this.shootTimer.start();
    }
}