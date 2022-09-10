//Class for enemy behaviors
package wizardRush;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Behavior {
	//Attributes
	private int type;
	private double duration, speed, distance, numShotPattern;
	private ShotDetails[] shotDetails;

	public Behavior(double duration, int type, double speed, double distance, int numShotPattern, ShotDetails[] shotDetails) {
		this.duration = duration;
		this.type = type;
		this.speed = speed;
		this.distance = distance;
		this.numShotPattern = numShotPattern;
		this.shotDetails = shotDetails;
	}
	
	//Getters
	public double getDuration() {
		return duration;
	}
	public int getType() {
		return type;
	}
	public double getSpeed() {
		return speed;
	}
	public double getDistance() {
		return distance;
	}
	public double getNumShotPattern() {
		return numShotPattern;
	}
	public ShotDetails[] getShotDetails(){
		return shotDetails;
	}
}