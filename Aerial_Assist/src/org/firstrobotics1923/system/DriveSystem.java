package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import org.firstrobotics1923.util.MotorGroup;
import org.firstrobotics1923.Components;

/**
 * The Drive System
 * 
 * @author Pavan Hegde
 * @version 1.0
 * @since Jan 20, 2014
 */
public class DriveSystem extends RobotDrive{

    public DriveSystem(SpeedController frontLeft, SpeedController rearLeft, SpeedController frontRight, SpeedController rearRight) {
        super(frontLeft, rearLeft, frontRight, rearRight);
    }
    
    public DriveSystem(MotorGroup leftSide, MotorGroup rightSide) {
        super(leftSide, rightSide);
    }
    
    /**
     * Drive using Tank Drive
     * @param leftMag Magnitude of the left side
     * @param rightMag Magnitude of the right side
     */
    public void drive(double leftMag, double rightMag) {
        this.tankDrive(leftMag, rightMag);   
        //Components.sfxDashboard.Left_Joy = leftMag;
        //Components.sfxDashboard.Victor_1 = leftMag;
        //Components.sfxDashboard.Victor_2 = leftMag;
        //Components.sfxDashboard.Right_Joy = rightMag;
        //Components.sfxDashboard.Victor_3 = rightMag;
        //Components.sfxDashboard.Victor_4 = rightMag;
        
    }
    
    public void setSafety(boolean enabled) {
       this.setSafetyEnabled(enabled);
    }
    
    public void stop() {
        super.stopMotor();
        
        //Components.sfxDashboard.Victor_1 = 0.0;
        //Components.sfxDashboard.Victor_2 = 0.0;
        
        //Components.sfxDashboard.Victor_3 = 0.0;
        //Components.sfxDashboard.Victor_4 = 0.0;
        
    }   
    public void pidWrite(double output) {
            super.m_rearLeftMotor.pidWrite(output);
            super.m_rearRightMotor.pidWrite(output);
    }
}