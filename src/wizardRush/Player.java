//Class for user-controlled player
package wizardRush;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;


public class Player extends Living{
	//Attributes
	private int jumpsRemaining = 0;
	private int maxJumps;
	private boolean left, right, jumping, shooting, secondaryShooting;
	private boolean canJump = true;
	private boolean onGround;
	private long shotDelayTimer = 0;
	private long secondaryDelayTimer = 0;
	private double gravity;
	private ShotDetails mainShotDetails;
	private ShotDetails secondaryDetails;
	private boolean invulnerable = false;
	private double regen;
	private double decimalHP;
	private double maxGravity;
	private Handler handler;
	//Constructor
	public Player(LevelState levelState, double maxGravity) {
		super(levelState, levelState.getIniPCol() * levelState.getTileWidth(), levelState.getIniPRow() * levelState.getTileHeight(), 0, 0, 47, 65, new ImageIcon("src/res/sprites/Wizard.png").getImage(), levelState.getHandler().getMaxHP());
		levelState.getGameObjects().add(this);
		Handler handler = levelState.getHandler();
		maxJumps = handler.getMaxJumps();
		mainShotDetails = new ShotDetails(handler.getShotDelay(), 17.5, 0, 0.5, handler.getDamage(), 25, 25, "Fireball.png", 1, 0, handler.getPierce(), true);
		secondaryDetails = new ShotDetails(handler.getSecondaryCooldown(), 5, 3, 5, handler.getDamage() * 2, 40, 40, "Fireball.png", 10, 240, handler.getPierce(), true);
		regen = handler.getRegen();
		decimalHP = 0;
		this.maxGravity = maxGravity;
		this.handler = handler;
		levelState.setPlayer(this);
	}


	//No return
	//No parameters
	//Updates player info
	@Override
	public void update() {
		//End level if no HP
		if(HP <= 0 && !levelState.getLevelEnd()) {
			levelState.setLevelEnd(true);
			levelState.playPlayerDefeatedSound();
		}
		//Regenerate HP
		if(!levelState.getLevelEnd()) {
			decimalHP += regen / 60.0;
		}
		//Track HP between ints
		if(decimalHP > 1) {
			HP += decimalHP / 1;
			decimalHP %= 1;
		}
		//Don't go above maxHP
		if(HP > maxHP) {
			HP = maxHP;
		}
		//Gravity
		yVel += levelState.getGravity();
		if(yVel > maxGravity) {
			yVel = maxGravity;
		}
		setYVel(yVel);
		//Movement
		if(left && right) {
			xVel = 0;
		}
		else if(left) {
			xVel = -handler.getSpeed();
		}
		else if(right) {
			xVel = handler.getSpeed();
		}
		else {
			xVel = 0;
		}
		//Jumping
		if(jumping && canJump && jumpsRemaining > 0) {
			yVel = -20;
			onGround = false;
			canJump = false;
			jumpsRemaining --;
			levelState.playJumpSound();
		}
		//Shooting
		if(shooting && shotDelayTimer - System.currentTimeMillis() < 0) {
			shotDelayTimer = System.currentTimeMillis() + (long) (mainShotDetails.getShotDelay() * 1000);
			Point p = levelState.getMoustPos();
			shoot(p.getX(), p.getY(), mainShotDetails);
			levelState.playPlayerShootSound();
		}

		//Secondary shooting
		if(secondaryShooting && secondaryDelayTimer - System.currentTimeMillis() < 0) {
			secondaryDelayTimer = System.currentTimeMillis() + (long) (secondaryDetails.getShotDelay() * 1000);
			Point p = levelState.getMoustPos();
			shoot(p.getX(), p.getY(), secondaryDetails);
			levelState.playPlayerShootSound();
		}

		//Check goal collision
		LinkedList<GameObject> colliders = new LinkedList<GameObject>();
		colliders.add(levelState.getGoal());
		setTempPos(colliders);
		move();

	}
	
	//No return
	//Parameters for colliding object, gameObjects to check after, and colliding directions
	//If colliding with goal, end level. Otherwise, call collision form Living
	public void collision(GameObject col, LinkedList<GameObject> gameObjects, int colDir) {
		if(col.getClass() == Goal.class){
			if(levelState.getBoss() == null && !levelState.getLevelEnd()) {
				levelState.setLevelEnd(true);
				levelState.recordTime();
				levelState.playLevelClearSound();
			}
			super.collision(null, gameObjects, colDir);
		}
		else{
			super.collision(col, gameObjects, colDir);
		}
	}


	//No return
	//No parameters
	//Reset jumps
	public void land() {
		onGround = true;
		jumpsRemaining = maxJumps;
	}

	//Getters & setters
	public double getXVel() {
		return xVel;
	}
	public double getYVel() {
		return yVel;
	}
	public boolean getInvulnerable() {
		return invulnerable;
	}
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}
	public void setSecondaryShooting(boolean secondaryShooting) {
		this.secondaryShooting = secondaryShooting;
	}
	public void setInvulnerable(boolean invulnerable){
		this.invulnerable = invulnerable;
	}
	public long getSecondaryDelayTimer() {
		return secondaryDelayTimer;
	}



}
