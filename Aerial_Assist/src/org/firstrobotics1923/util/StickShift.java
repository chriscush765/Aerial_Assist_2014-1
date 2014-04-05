package org.firstrobotics1923.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Our very own Joystick
 * 
 * @author Pavan Hegde, Chris Cushman, Prasanth Yedalapalli
 * @version 1.0
 * @since Jan 20, 2014
 */
public class StickShift extends Joystick{
    private final Coalescor xCoalascor;    //Coalescors for each Axis
    private final Coalescor yCoalascor;
    private final Coalescor zCoalascor;
    
    /**
     * Creates a StickShift plugged into "port"
     * @param port the port that the Joystick is plugged in to
     */
    public StickShift(int port) {
       super(port);
       this.xCoalascor = new Coalescor(this.getX());
       this.yCoalascor = new Coalescor(this.getY());
       this.zCoalascor = new Coalescor(this.getZ());
    }
    
    /**
     * Creates a StickShift with the desired port and epsilon
     * @param port the port that the Joystick is plugged in to
     * @param epsilon the rate of change
     */
    public StickShift(int port, double epsilon) {
        super(port);
       this.xCoalascor = new Coalescor(this.getX(), epsilon);
       this.yCoalascor = new Coalescor(this.getY(), epsilon);
       this.zCoalascor = new Coalescor(this.getZ(), epsilon);
    }
    
    /**
     * Returns the coalesced X-value (Left and Right)
     * @return the coalesced X-value
     */
    public double getCoalescedX() {
        return this.xCoalascor.coalesce(this.getX());
    }
    
    /**
     * Returns the coalesced Y-value (Forward and Backward)
     * @return the coalesced Y-value
     */
    public double getCoalescedY() {
        return this.yCoalascor.coalesce(this.getY());
    }
    
    /**
     * Returns the coalesced Z-value (Twist)
     * @return the coalesced Z-value
     */
    public double getCoalescedZ() {
        return this.zCoalascor.coalesce(this.getZ());
    }
}