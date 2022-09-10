//Class for enemies
package wizardRush;

import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.ImageIcon;

public class Enemy extends Living{
	//Attributes
	boolean active = false;
	private long behaviorTimer;
	private long[] shotTimer;
	private int currHPThreshold = 0;
	private int currTimeThreshold = 0;
	private Behavior currBehavior;
	private double angle;
	private int value;
	private int[] HPThresholds;
	private ArrayList<ArrayList<Behavior>> behaviors = new ArrayList<ArrayList<Behavior>>();
	public LinkedList<Projectile> projs = new LinkedList<Projectile>();
	private Player player;
	private int circleDir;
	private double distanceMult = Math.random() / 5.0 + 0.9;
	private Point behaviorPoint;
	boolean changedPoint;

	public Enemy(LevelState levelState, double x, double y, int width, int height, int enemyNum, double valueMult, double hpMult, double damageMult) {
		super(levelState, (int) Math.round(x - width / 2.0), (int) Math.round(y - height/ 2.0), 0, 0, width, height, new ImageIcon("src/res/sprites/Enemy" + enemyNum + ".png").getImage(), 1000);
		player = levelState.getPlayer();
		angle = Math.random() * 2 * Math.PI;
		levelState.getEnemies().add(this);
		behaviorPoint = new Point();
		behaviorPoint.setLocation(x - width / 2.0, y - height / 2.0);
		changedPoint = false;
		//Read behaviors from file
		try {
			BufferedReader inFile = new BufferedReader(new FileReader("src/res/behaviors/EnemyBehavior" + enemyNum + ".txt"));
			value = (int) (Integer.parseInt(inFile.readLine()) * valueMult);
			maxHP = (int) (Integer.parseInt(inFile.readLine()) * hpMult);
			HP = maxHP;
			HPThresholds = new int[Integer.parseInt(inFile.readLine())];
			//For each HPThreshold
			for(int i = 0; i < HPThresholds.length; i ++) {
				ArrayList<Behavior> behaviorsInHPThreshold = new ArrayList<Behavior>();
				HPThresholds[i] = Integer.parseInt(inFile.readLine());
				int numBehaviors = Integer.parseInt(inFile.readLine());
				//For each move type/timeThreshold in hpThreshold
				for(int j = 0; j < numBehaviors; j ++) {
					double behaviorDuration = Double.parseDouble(inFile.readLine());
					int behaviorType = Integer.parseInt(inFile.readLine());
					double behaviorSpeed = Double.parseDouble(inFile.readLine());
					double behaviorDistance = Double.parseDouble(inFile.readLine());
					int numShotPatterns = Integer.parseInt(inFile.readLine());
					double[] shotDelays = new double[numShotPatterns];
					ShotDetails[] shotDetails = new ShotDetails[numShotPatterns];
					//For each shot pattern in behavior
					for(int k = 0; k < numShotPatterns; k ++) {
						shotDetails[k] = new ShotDetails(inFile.readLine());
						shotDetails[k].multDamage(damageMult);
					}
					behaviorsInHPThreshold.add(new Behavior(behaviorDuration, behaviorType, behaviorSpeed, behaviorDistance, numShotPatterns, shotDetails));
				}
				behaviors.add(behaviorsInHPThreshold);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//No return
	//No parameters
	//Actives enemy when player is within range. Sets enemy's circle direction to random direction (clockwise/counterclockwise). Sets behavior related timers.
	public void activate() {
		if(Math.random() > 0.5) {
			circleDir = 1;
		}
		else {
			circleDir = -1;
		}
		currBehavior = behaviors.get(0).get(0);
		behaviorTimer = System.currentTimeMillis();
		shotTimer = new long[(int) currBehavior.getNumShotPattern()];
		for(int j = 0; j < shotTimer.length; j ++) {
			shotTimer[j] = System.currentTimeMillis();
		}
		active = true;
	}

	//No return
	//Parameter for speed
	//Enemy wanders in a random direction at set speed. Change in direction is capped at ~ 10 degrees per frame
	public void wander(double speed) {
		angle += (Math.random() - 0.5) / 3.0;
		speed /= 5;
		xVel = speed * Math.cos(angle);
		yVel = speed * Math.sin(angle);
	}

	//No return
	//Parameters for speed, distance and target position
	//Move to maintain at set distance away from a point at a set maximum speed.
	public void followTarget(double speed, double distance, Point p) {
		angle =  Math.atan2(p.getY() - (y + height / 2), p.getX() - (x + width / 2));
		double currDistance = Math.sqrt(Math.pow(x + width / 2.0 - p.getX(), 2) + Math.pow(y + height / 2.0 - p.getY(), 2));

		//Randomize distance a little bit so identical enemies don't stack up as much
		distance *= distanceMult;
		//Move exactly to target position if within range
		if(Math.abs(currDistance - distance) < speed) {
			xVel = p.getX() - Math.cos(angle) * distance - (x + width / 2);
			yVel = p.getY() - Math.sin(angle) * distance - (y + height / 2);
		}
		//Otherwise, move in target direction at set speed
		else {
			if(currDistance <= distance) {
				xVel = - Math.cos(angle) * speed;
				yVel = - Math.sin(angle) * speed;
			}
			else {
				xVel = Math.cos(angle) * speed;
				yVel = Math.sin(angle) * speed;
			}
		}
	}
	
	//No return
	//Parameters for speed and distance
	//Rotates around player at set speed and distance
	public void circlePlayer(double speed, double distance) {
		//First, tries to get to target distance from player
		Point p = new Point();
		p.setLocation(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
		followTarget(speed, distance, p);
		//Adds velocity in direction perpendicular to towards/away from player
		angle += circleDir * Math.PI / 2.0;
		xVel += Math.cos(angle) * speed;
		yVel += Math.sin(angle) * speed;
	}
	
	//No return
	//Parameters for speed, xChange, yChange
	//Sets behavior point when behavior starts. Moves towards it for rest
	public void moveToPoint(double speed, int xChange, int yChange) {
		if(!changedPoint) {
			behaviorPoint.setLocation(behaviorPoint.getX() + xChange, behaviorPoint.getY() + yChange);
			changedPoint = true;
		}
		angle =  Math.atan2((behaviorPoint.getY()) - (y + height / 2), (behaviorPoint.getX()) - (x + width / 2));
		double currDistance = Math.sqrt(Math.pow(x + width / 2.0 - (behaviorPoint.getX()), 2) + Math.pow(y + height / 2.0 - behaviorPoint.getY(), 2));

		//If target position is within range, move directly there
		if(currDistance <= speed) {
			xVel = behaviorPoint.getX() - (x + width / 2);
			yVel = behaviorPoint.getY() - (y + height / 2);
		}
		//Otherwise move in target direction at set speed
		else {
			xVel = Math.cos(angle) * speed;
			yVel = Math.sin(angle) * speed;
		}
	}

	//No return
	//Parameters for speed and distance
	//Circles around behaviorPoint
	public void circlePoint(double speed, double distance) {
		followTarget(speed, distance, behaviorPoint);
		angle += circleDir * Math.PI / 2.0;
		xVel += Math.cos(angle) * speed;
		yVel += Math.sin(angle) * speed;
	}


	//No return
	//No parameters
	//Controls enemy logic each frame
	@Override
	public void update() {
		if(active) {
			//Remove enemy if it has no HP
			if(HP <= 0) {
				levelState.getHandler().increaseMoney(value);
				levelState.getGameObjects().remove(this);
				levelState.getEnemies().remove(this);
				levelState.playEnemyDefeatedSound();
				if(this.getClass() == Boss.class) {
					levelState.defeatBoss();
				}
			}
			else {
				//Check if new HPThreshold triggered
				for(int i = currHPThreshold + 1; i < HPThresholds.length; i ++) {
					if((HP / (double) maxHP) * 100 < HPThresholds[i]) {
						//Set behavior attributes
						currHPThreshold = i;
						currTimeThreshold = 0;
						angle = Math.random() * 2 * Math.PI;
						currBehavior = behaviors.get(currHPThreshold).get(currTimeThreshold);
						behaviorTimer = System.currentTimeMillis();
						shotTimer = new long[(int) currBehavior.getNumShotPattern()];
						for(int j = 0; j < shotTimer.length; j ++) {
							shotTimer[j] = System.currentTimeMillis();
						}
						changedPoint = false;
					}
				}
				//Check if new timeThreshold triggered
				if(System.currentTimeMillis() - behaviorTimer > behaviors.get(currHPThreshold).get(currTimeThreshold).getDuration() * 1000) {
					//Set behavior attributes
					currTimeThreshold ++;
					if(currTimeThreshold == behaviors.get(currHPThreshold).size()) {
						currTimeThreshold = 0;
					}
					currBehavior = behaviors.get(currHPThreshold).get(currTimeThreshold);
					angle = Math.random() * 2 * Math.PI;
					behaviorTimer = System.currentTimeMillis();

					shotTimer = new long[(int) currBehavior.getNumShotPattern()];
					for(int j = 0; j < shotTimer.length; j ++) {
						shotTimer[j] = System.currentTimeMillis();
					}
					changedPoint = false;
				}

				//Shoot based on timers
				for(int j = 0; j < shotTimer.length; j ++) {
					if(System.currentTimeMillis() - shotTimer[j] > currBehavior.getShotDetails()[j].getShotDelay() * 1000) {
						shoot(player.drawX + player.width / 2.0, player.drawY + player.height / 2.0, currBehavior.getShotDetails()[j]);
						shotTimer[j] = System.currentTimeMillis();
					}
				}
				
				//Move based on behavior type
				if(currBehavior.getType() == 1) {
					wander(currBehavior.getSpeed());
				}
				else if(currBehavior.getType() == 2) {
					Point playerPoint = new Point();
					playerPoint.setLocation(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
					followTarget(currBehavior.getSpeed(), currBehavior.getDistance(), playerPoint);
				}
				else if(currBehavior.getType() == 3) {
					circlePlayer(currBehavior.getSpeed(), currBehavior.getDistance());
				}
				else if(currBehavior.getType() == 4) {
					//Movement is encoded in distance. First digit is 1, next digit it 0 for pos 1 for neg. Next 3 digits are x change. Next digit is 0 for pos 1 for neg. Next 3 digits are y change.
					String s = "" + (int) currBehavior.getDistance();
					int sign = 1;
					if(Integer.parseInt("" + s.charAt(1)) == 1){
						sign = -1;
					}
					int xChange = sign * Integer.parseInt(s.substring(2, 5));
					sign = 1;
					if(Integer.parseInt("" + s.charAt(5)) == 1){
						sign = -1;
					}
					int yChange = sign * Integer.parseInt(s.substring(6));
					moveToPoint(currBehavior.getSpeed(), xChange, yChange);
				}
				else if(currBehavior.getType() == 5) {
					circlePoint(currBehavior.getSpeed(), currBehavior.getDistance());
				}
				setTempPos(new LinkedList<GameObject>());
				move();
			}
		}
		//Activate if player is in range
		else if(Math.sqrt(Math.pow(x + width / 2.0 - (player.x + player.width / 2.0), 2) + Math.pow(y + height / 2.0 - (player.y + player.height / 2.0), 2)) < 500) {
			activate();
		}


	}


}
