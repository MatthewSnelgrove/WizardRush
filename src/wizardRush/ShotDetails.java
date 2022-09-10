//Class for details of shot projectils
package wizardRush;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ShotDetails {
	//Attibutes
	private double shotDelay;
	private double speed;
	private double seekingAmount;
	private double lifespan;
	private int damage;
	private int shotWidth;
	private int shotHeight;
	private Image sprite;
	private int numShots;
	private double spreadAngle;
	private int pierce;
	private boolean playerProj;
	
	//Constructor
	public ShotDetails(String inS) {
		String[] s = inS.split(" ");
		shotDelay = Double.parseDouble(s[0]);
		speed = Double.parseDouble(s[1]);
		seekingAmount = Double.parseDouble(s[2]);
		lifespan = Double.parseDouble(s[3]);
		damage = Integer.parseInt(s[4]);
		shotWidth = Integer.parseInt(s[5]);
		shotHeight = Integer.parseInt(s[6]);
		sprite = new ImageIcon("src/res/sprites/" + s[7]).getImage();
		numShots = Integer.parseInt(s[8]);
		spreadAngle = Double.parseDouble(s[9]);
		pierce = Integer.parseInt(s[10]);
		playerProj = Boolean.parseBoolean(s[11]);
	}
	//Constructor
	public ShotDetails(double shotDelay, double speed, double seekingAmount, double lifespan, int damage, int shotWidth, int shotHeight, String sprite, int numShots, double spreadAngle, int pierce, boolean playerProj) {
		this.shotDelay = shotDelay;
		this.speed = speed;
		this.seekingAmount = seekingAmount;
		this.lifespan = lifespan;
		this.damage = damage;
		this.shotWidth = shotWidth;
		this.shotHeight = shotHeight;
		this.sprite = new ImageIcon("src/res/sprites/" + sprite).getImage();
		this.numShots = numShots;
		this.spreadAngle = spreadAngle;
		this.pierce = pierce;
		this.playerProj = playerProj;
	}
	
	//Getters
	public double getShotDelay() {
		return shotDelay;
	}
	public double getSpeed() {
		return speed;
	}
	public double getSeekingAmount() {
		return seekingAmount;
	}
	public double getLifespan() {
		return lifespan;
	}
	public int getDamage() {
		return damage;
	}
	public int getShotWidth() {
		return shotWidth;
	}
	public int getShotHeight() {
		return shotHeight;
	}
	public Image getSprite() {
		return sprite;
	}
	public int getNumShots() {
		return numShots;
	}
	public double getSpreadAngle() {
		return spreadAngle;
	}
	public int getPierce() {
		return pierce;
	}
	public boolean getPlayerProj() {
		return playerProj;
	}
	public void multDamage(double mult) {
		damage *= mult;
	}
}
