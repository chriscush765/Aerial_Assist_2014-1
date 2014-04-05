package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * An extra system for special index control
 * 
 * @author Pavan Hegde
 * @version 1.0
 * @since 4/5/2014
 */
public class Indexer extends PneumaticSystem {
    private Solenoid indexerOne, indexerTwo;
    
    public Indexer(Solenoid indexerOne, Solenoid indexerTwo) {
        this.indexerOne = indexerOne;
        this.indexerTwo = indexerTwo;
    }
    
    public void activate() { //Test if true false is correct
        indexerOne.set(true);
        indexerTwo.set(false);
    }

    public void deactivate() {
        indexerOne.set(false);
        indexerTwo.set(true);
    }
    
}
