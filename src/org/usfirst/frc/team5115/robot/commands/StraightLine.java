package org.usfirst.frc.team5115.robot.commands;

import org.usfirst.frc.team5115.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StraightLine extends Command {
	
	double kp = 0.006;
	double ki = 0.000001;
	double kd = 0.0001;
	
	double errorAccum = 0;
	double lastError = 0;
	
	double setSpeed = 0.3;
	
	double dist;

    public StraightLine(double d) {
        dist = d;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.chassis.inuse = true;
    	Robot.chassis.resetImu();
    	Robot.chassis.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double error = Robot.chassis.getYaw();
    	
    	if (lastError == 0)
    		lastError = error;
    	errorAccum += error;
    	
    	double correction = error * kp + errorAccum * ki + (error - lastError) * kd;
    	
    	lastError = error;
    	
    	Robot.chassis.tankDrive(setSpeed + correction, setSpeed - correction, 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.chassis.distanceTraveled() >= dist;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.chassis.tankDrive(0, 0, 1);
    	Robot.chassis.inuse = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
