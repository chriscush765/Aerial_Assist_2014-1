package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * System to control a Piston (sample, most likely to be changed)
 * 
 * @author Pavan Hegde
 * @version 1.0
 * @since Jan. 13, 2014
 */
public abstract class PneumaticSystem implements System{ //Overhaul Committed by Pavan
    private boolean state = false;
    
    public abstract void activate();
    
    public abstract void deactivate();
    
    public void set(boolean on) {          //Sets system to on or not (activated vs. deactivated)
        if (on) {
            this.activate();
        }else {
            this.deactivate();
        }
    }
    
    public void toggle() {
        this.set(!state);
        state = !state;
    }
    
    public void stop() {
        this.set(false);
    }
}