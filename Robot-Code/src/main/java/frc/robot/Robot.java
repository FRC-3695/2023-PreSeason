// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.util.Xbox;
import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
 private static WPI_TalonSRX m_leftmaster = new WPI_TalonSRX(1); 
 private static WPI_TalonSRX m_leftslave = new WPI_TalonSRX(2);
 private static WPI_TalonSRX m_rightmaster = new WPI_TalonSRX(3);
 private static WPI_TalonSRX m_rightslave = new WPI_TalonSRX(4);
 //private static MotorControllerGroup m_left = new MotorControllerGroup(m_leftmaster, m_leftslave);
 //private static MotorControllerGroup m_right = new MotorControllerGroup(m_rightmaster, m_rightslave);
  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive( m_leftmaster, m_rightmaster);
  private final Joystick m_stick = new Joystick(0);
  private final Timer m_timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    if (m_timer.get() < 2.0) {
      m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
    } else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    m_leftslave.follow(m_leftmaster);
    m_rightslave.follow(m_rightmaster);


  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
   // m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
   double throttle = Xbox.RT(m_stick) - Xbox.LT(m_stick);
   double steering = Xbox.LEFT_X(m_stick);
   double driveRight = throttle - steering;
   double driveLeft = throttle + steering;

   driveRight = (driveRight < -1 ? -1 : (driveRight > 1 ? 1 : driveRight));
   driveLeft = (driveLeft < -1 ? -1 : (driveLeft > 1 ? 1 : driveLeft));

   //m_robotDrive.tankDrive(Xbox.LT(m_stick), Xbox.RT(m_stick));
   m_robotDrive.tankDrive(driveLeft, driveRight);

  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
