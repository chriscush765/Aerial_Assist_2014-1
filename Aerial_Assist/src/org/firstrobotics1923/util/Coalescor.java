package org.firstrobotics1923.util;

/**
 * A util to gradually bring one number towards another value
 * 
 * @author Pavan Hegde, Chris Cushman, Prasanth Yedalapalli
 * @version 1.0
 * @since Jan 20, 2014
 */
public class Coalescor {
    private double startingAmount;
    private double epsilon;
    
    /**
     * Creates a Coalescor with the starting amount of 0
     */
    public Coalescor() {
        this(0);
    }
    
    /**
     * Creates a Coalescor with the desired starting amoutn
     * @param startingAmount 
     */
    public Coalescor(double startingAmount) {
        this(startingAmount, DefaultConfig.EPSILON);
    }
    
    /**
     * Creates a Coalescor with the desired epsilon and starting amount
     * @param startingAmount The amount you start with at each interval
     * @param epsilon the rate of change
     */
    public Coalescor(double startingAmount, double epsilon) {
        this.startingAmount = startingAmount;
        this.epsilon = epsilon;
    }
    
    /**
     * Coalesces the value
     * @param endValue The desired final value
     * @return the next increment
     */
    public double coalesce(double endValue) {
        if (endValue > this.startingAmount) {
            if (this.startingAmount + this.epsilon > endValue) {
                this.startingAmount = endValue;
            }else if (this.startingAmount + this.epsilon < endValue) {
                this.startingAmount += this.epsilon;
            }
        }else if (endValue < startingAmount) {
            if (this.startingAmount - this.epsilon < endValue) {
                this.startingAmount = endValue;
            }else if (this.startingAmount - this.epsilon > endValue) {
                this.startingAmount -= this.epsilon;
            }
        }

        return startingAmount;
    }
    
    /**
     * @return the epsilon
     */
    public double getEpsilon() {
        return epsilon;
    }
    
    /**
     * Sets the epsilon
     * @param epsilon the rate of change
     */
    public void setEpsilon(double epsilon) {
       this.epsilon = epsilon; 
    }
}