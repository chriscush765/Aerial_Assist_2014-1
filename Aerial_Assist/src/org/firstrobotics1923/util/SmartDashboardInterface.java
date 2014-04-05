package org.firstrobotics1923.util;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import java.util.logging.Level;
//import java.util.logging.Logger;
/**
 *
 * @author Makarand
 */

public class SmartDashboardInterface {
    
public double Victor_1 = 0.0;
public double Victor_2 = 0.0;
public double Victor_3 = 0.0;
public double Victor_4 = 0.0;
public double Victor_5 = 0.0;
public double Victor_6 = 0.0;
public double Victor_7 = 0.0;
public double Victor_8 = 0.0;
public double Victor_9 = 0.0;

public boolean IntakePiston_1 = false;
public boolean IntakePiston_2 = false;
public boolean ShooterAnglePiston_1 = false;
public boolean ShooterAnglePiston_2 = false;
public boolean CompressorRelay = false;


public double Left_Joy = 0.0; 
public double Right_Joy =0.0;
public boolean IntakeAngle_Command = false;
public boolean ShooterWheel_Command = false;
public boolean ShooterAngle_Command = false;  
public double IntakeWheel_Command = 0.0;
public boolean useDashboard = false;
public double var_1Value = 0.5;
public double var_2Value = 0.5;
public double var_3Value = 0.5;
public double var_4Value = 0.5;
public double var_5Value = 0.5;
public double var_6Value = 0.5;

public boolean useDashboardVar = false;
public boolean sfxTargetHotorNot = false;
public double sfxDistanceFromTarget = -1;
public double Gyro_ANGLE = 0.0;
public double Left_Encoder_Value = 0.0;
public double Right_Encoder_Value = 0.0;
public boolean Left_Encoder_Direction = false;
public boolean Right_Encoder_Direction = true;





private NetworkTable table ; 

// Smartdashboard Variable Names
private final String v1 = "Victor_1";
private final String v2 = "Victor_2";
private final String v3 = "Victor_3";
private final String v4 = "Victor_4";
private final String v5 = "Victor_5";
private final String v6 = "Victor_6";
private final String v7 = "Victor_7";
private final String v8 = "Victor_8";
private final String v9 = "Victor_9";

private final String ip1 = "IntakePiston_1";
private final String ip2 = "IntakePiston_2";
private final String sap1 = "ShooterAnglePiston_1";
private final String sap2 = "ShooterAnglePiston_2";
private final String cr = "CompressorRelay";
private final String lj = "Left_Joy";
private final String rj = "Right_Joy";
private final String iac = "IntakeAngle_Command";
private final String swc = "ShooterWheel_Command";
private final String sac = "ShooterAngle_Command";
private final String iwc = "IntakeWheel_Command";
private final String var_1 = "Var_1";
private final String var_2 = "Var_2";
private final String var_3 = "Var_3";
private final String var_4 = "Var_4";
private final String var_5 = "Var_5";
private final String var_6 = "Var_6";
private final String Use_Dashboard_Var = "Use_Dashboard_Var";
private final String gad = "Gyro_ANGLE_DEG";
private final String leftenc = "Left_Encoder_Value_IN";
private final String rightenc = "Right_Encoder_Value_IN";
private final String leftencdir = "Left_Encoder_Direction";
private final String rightencdir = "Right_Encoder_Direction";



private final String hotornot = "Vision_Target";
private final String distFTarget = "Target_Distance";

    public SmartDashboardInterface(NetworkTable table,boolean useDB) {
        // Constructor
              //this.table = NetworkTable.getTable("SmartDashboard"); 
   
    this.table = table;
    this.useDashboard = useDB;
    this.initSDVariables();
    }

     public SmartDashboardInterface() {
        // Constructor
              //this.table = NetworkTable.getTable("SmartDashboard"); 
   
    
    //this.initSDVariables();
    }
   


public void smartDashboardUpdate(){
    // update all values
   if(this.useDashboard){
    try{
    table.putNumber(v1, Victor_1);
    table.putNumber(v2, Victor_2);
    table.putNumber(v3, Victor_3);
    table.putNumber(v4, Victor_4);
    table.putNumber(v5, Victor_5);
    table.putNumber(v6, Victor_6);
    table.putNumber(v7, Victor_7);
    table.putNumber(v8, Victor_8);
    table.putNumber(v9, Victor_9);
    
    table.putBoolean(ip1, this.IntakePiston_1);
    table.putBoolean(ip2, this.IntakePiston_2);
    table.putBoolean(sap1, this.ShooterAnglePiston_1);
    table.putBoolean(sap2, this.ShooterAnglePiston_2);
    table.putBoolean(this.cr, this.CompressorRelay);
    table.putNumber(this.lj, this.Left_Joy);
    table.putNumber(this.rj, this.Right_Joy);
    table.putBoolean(this.iac, this.IntakeAngle_Command);
    table.putBoolean(this.swc, this.ShooterWheel_Command);
    table.putBoolean(this.sac, this.ShooterAngle_Command);
    table.putNumber(this.iwc, this.IntakeWheel_Command);
    table.putBoolean(this.hotornot, this.sfxTargetHotorNot);
    table.putNumber(this.distFTarget, this.sfxDistanceFromTarget);
    table.putNumber(this.gad, this.Gyro_ANGLE);
    table.putNumber(this.leftenc, this.Left_Encoder_Value);
    table.putNumber(this.rightenc, this.Right_Encoder_Value);
    table.putBoolean(this.leftencdir, this.Left_Encoder_Direction);
    table.putBoolean(this.rightencdir, this.Right_Encoder_Direction);
        
    
    this.var_1Value = table.getNumber(this.var_1);
    this.var_2Value = table.getNumber(this.var_2);
    this.var_3Value = table.getNumber(this.var_3);
    this.var_4Value = table.getNumber(this.var_4);
    this.var_5Value = table.getNumber(this.var_5);
    this.var_6Value = table.getNumber(this.var_6);
    this.useDashboardVar = table.getBoolean(this.Use_Dashboard_Var);
   } 
    catch(TableKeyNotDefinedException e){
            System.out.println("Smartdashboard Interface Exception thrown: " + e.toString());
            }
   }
 
}

public void cRioDummy(){
   // This is dummy method to simulate CRio output. Can be used for testing program without joystick and Xbox
    this.Left_Joy = table.getNumber(this.lj);
    this.Right_Joy = table.getNumber(this.rj);
    this.IntakeAngle_Command = table.getBoolean(this.iac);
    this.ShooterWheel_Command = table.getBoolean(this.swc);
    this.ShooterAngle_Command = table.getBoolean(this.sac);  
    this.IntakeWheel_Command = table.getNumber(this.iwc);
    
    this.Victor_1 = this.Left_Joy;
    this.Victor_2 = this.Left_Joy;
    this.Victor_3 = this.Right_Joy;
    this.Victor_4 = this.Right_Joy;
    
     // Shooter Wheel
        if (this.ShooterWheel_Command){
            this.Victor_5 = 1.0;
            this.Victor_6 = 1.0;
            this.Victor_7 = -1.0;
            this.Victor_8 = -1.0;
        }
            else{ 
            this.Victor_5 = 0.0;
            this.Victor_6 = 0.0;
            this.Victor_7 = 0.0;
            this.Victor_8 = 0.0;
        }
            // Intake Motor
        
        
            this.Victor_9 = this.IntakeWheel_Command;
        
        // Intake Solinoid
        
        this.IntakePiston_1 = this.IntakeAngle_Command;
        this.IntakePiston_2 = this.IntakeAngle_Command;
        // Shooter Angle
      
        this.ShooterAnglePiston_1= this.ShooterAngle_Command;
        this.ShooterAnglePiston_2 = this.ShooterAngle_Command;
    
        this.smartDashboardUpdate();
   
    
    
}

public void setDashboardTable(NetworkTable table){
    this.table = table;
}

public  void initSDVariables(){
    
    table.putNumber(lj, this.Left_Joy);
    table.putNumber(rj, this.Right_Joy);
    table.putBoolean(iac, this.IntakeAngle_Command);
    table.putBoolean(this.swc, this.ShooterWheel_Command);
    table.putBoolean(this.sac, this.ShooterAngle_Command);
    table.putNumber(this.iwc, this.IntakeWheel_Command);
    table.putNumber(v1, Victor_1);
    table.putNumber(v2, Victor_2);
    table.putNumber(v3, Victor_3);
    table.putNumber(v4, Victor_4);
    table.putNumber(v5, Victor_5);
    table.putNumber(v6, Victor_6);
    table.putNumber(v7, Victor_7);
    table.putNumber(v8, Victor_8);
    table.putNumber(v9, Victor_9);
    
    table.putBoolean(ip1, this.IntakePiston_1);
    table.putBoolean(ip2, this.IntakePiston_2);
    table.putBoolean(sap1, this.ShooterAnglePiston_1);
    table.putBoolean(sap2, this.ShooterAnglePiston_2);
    table.putBoolean(this.cr, this.CompressorRelay);
    table.putNumber(this.var_1, var_1Value);
    table.putNumber(this.var_2, var_2Value);
    table.putNumber(this.var_3, var_3Value);
    table.putNumber(this.var_4, var_4Value);
    table.putNumber(this.var_5, var_5Value);
    table.putNumber(this.var_6, var_6Value);
    table.putBoolean(this.Use_Dashboard_Var, this.useDashboardVar);
    table.putNumber(this.gad, this.Gyro_ANGLE);
    table.putNumber(this.leftenc, this.Left_Encoder_Value);
    table.putNumber(this.rightenc, this.Right_Encoder_Value);
    table.putBoolean(this.leftencdir, this.Left_Encoder_Direction);
    table.putBoolean(this.rightencdir, this.Right_Encoder_Direction);
   
}

}
