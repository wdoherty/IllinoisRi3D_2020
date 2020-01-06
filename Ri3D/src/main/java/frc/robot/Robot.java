/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private XboxController controller;
  private static final String kDefaultAuto = "Default";	  
  private static final String kCustomAuto = "My Auto";	  
  private String m_autoSelected;	 
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private SpeedController m_left1 = new PWMVictorSPX(0);
  private SpeedController m_left2 = new PWMVictorSPX(1);
  private SpeedController m_right1 = new PWMVictorSPX(2);
  private SpeedController m_right2 = new PWMVictorSPX(3);
  // private SpeedController m_intake = new PWMVictorSPX(4);
  private VictorSPX m_intake = new VictorSPX(1);
  private DoubleSolenoid tensioner = new DoubleSolenoid(0, 1);
  private SpeedControllerGroup m_left = new SpeedControllerGroup(m_left1, m_left2);
  private SpeedControllerGroup m_right = new SpeedControllerGroup(m_right1, m_right2);


  @Override
  public void robotInit() {
    m_myRobot = new DifferentialDrive(m_left, m_right);
    controller = new XboxController(0);
    tensioner.set(Value.kOff);
  }

        /**	
   * This function is called every robot packet, no matter the mode. Use	
   * this for items like diagnostics that you want ran during disabled,	
   * autonomous, teleoperated and test.	
   *	
   * <p>This runs after the mode specific periodic functions, but before	
   * LiveWindow and SmartDashboard integrated updating.	
   */	
  @Override	
  public void robotPeriodic() {	
  }	

  /**	
   * This autonomous (along with the chooser code above) shows how to select	
   * between different autonomous modes using the dashboard. The sendable	
   * chooser code works with the Java SmartDashboard. If you prefer the	
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the	
   * getString line to get the auto name from the text box below the Gyro	
   *	
   * <p>You can add additional auto modes by adding additional comparisons to	
   * the switch structure below with additional strings. If using the	
   * SendableChooser make sure to add them to the chooser code above as well.	
   */	
  @Override	
  public void autonomousInit() {	
    tensioner.set(Value.kForward);
    m_autoSelected = m_chooser.getSelected();	
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);	
    System.out.println("Auto selected: " + m_autoSelected);	
  }	

  /**	
   * This function is called periodically during autonomous.	
   */	
  @Override	
  public void autonomousPeriodic() {	
    switch (m_autoSelected) {	
      case kCustomAuto:	
        // Put custom auto code here	
        break;	
      case kDefaultAuto:	
      default:	
        // Put default auto code here	
        break;	
    }	
  }

  @Override	
  public void teleopInit() {	
    tensioner.set(Value.kForward);
  }	

  @Override
  public void teleopPeriodic() {
    //default drive train code
    m_myRobot.arcadeDrive(controller.getY(Hand.kLeft), controller.getX(Hand.kRight));

    //when our right bumper is pressed (scoring button) tension the rollers at the scoring end and run rollers
    if(controller.getBumperPressed(Hand.kRight))
    {
      tensioner.set(Value.kReverse);
      m_intake.set(ControlMode.PercentOutput, -0.5);
      // m_intake.set(-0.5);
    }
    else if(controller.getBumperReleased(Hand.kRight))
    {
      tensioner.set(Value.kForward);
      m_intake.set(ControlMode.PercentOutput, 0);
      // m_intake.set(0);
    }

    //runs intake if B button is held down, stops when released
    if(controller.getBButton()) m_intake.set(ControlMode.PercentOutput, -0.5);
    else m_intake.set(ControlMode.PercentOutput, 0);
  }
}
