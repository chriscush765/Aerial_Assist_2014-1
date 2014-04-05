package org.firstrobotics1923.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firstrobotics1923.Components;

/**
 * SmartDashboard Display
 * 
 * @author Bhavish Y.
 * @version 1.0
 * @since Feb. 27, 2014
 */
public class Dashboard {
    
    public static void update(){
        try {
            SmartDashboard.putString("Hot or Not", Components.table.getString("Hot_Target"));
            SmartDashboard.putString("Target Distance", "" + Components.table.getNumber("TARGET_DISTANCE"));
        
            if (Components.table.getNumber("TARGET_DISTANCE") > 96 && Components.table.getNumber("TARGET_DISTANCE") < 216) {
                SmartDashboard.putString("In Range?", "Ready to Fire");
            } else {
                SmartDashboard.putString("In Range?", "DO NOT shoot");
            }
        } catch(Exception e) {
            System.out.println("Excption in Dash: " + e.toString());
        }
    }
}