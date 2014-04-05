package org.firstrobotics1923.system;

import edu.wpi.first.wpilibj.Relay;
import org.firstrobotics1923.Components;

/**
 * The on-board compressor
 * 
 * @author Aryak Pande, Pavan Hegde, Olu Olorode
 * @version 1.0
 * @since Jan 25, 2014
 */
public class Compressor implements System{
    
    private final Relay compressor; //Relay Init
    private boolean on = false; //compressor status
    
    /**
     *  Main Constructor for class.     
     * @param compressorSpike 
     *                  Spike relay that controls the actual compressor
     */
    public Compressor(Relay compressorSpike){         
        this.compressor = compressorSpike;
    }
    
    /**
     * Turns on the Compressor
     */
    public void start(){       
        compressor.set(Relay.Value.kOn);
        //Components.sfxDashboard.CompressorRelay = true;
        
    }
    
    /**
     * Toggles compressor state
     */
    public void toggle(){        
        on = !on;       
    }
    
    /**
     *  Stops the Compressor
     */
    public void stop(){      
        compressor.set(Relay.Value.kOff);  
        //Components.sfxDashboard.CompressorRelay = false;
    }
}