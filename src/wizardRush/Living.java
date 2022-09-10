//Superclass of players and enemies
package wizardRush;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import javax.swing.ImageIcon;

public abstract class Living extends Dynamic{
	//Attributes
	protected int HP, maxHP;

	public Living(LevelState levelState, double x, double y, double xVel, double yVel, int width, int height, Image sprite, int HP) {
		super(levelState, x, y, xVel, yVel, width, height, sprite);
		this.HP = HP;
		this.maxHP = HP;
	}

	//No return
	//Parameter for graphics
	//Draws objects relative to player
	public void render(Graphics g) {
		drawX = (int) Math.round(x - levelState.getPlayer().x + 390);
		drawY = (int) Math.round(y - levelState.getPlayer().y + 440);
		if(isOnScreen()) {
			g.drawImage(sprite, drawX, drawY, width, height, null);
		}
	}

	//No return
	//Parameters for targetX, targetY, and shotDetails
	//First a projectile with shotDetails towards target position
	public void shoot(double targetX, double targetY, ShotDetails shotDetails) {
		//Angle
		double targetAngle = Math.atan2(targetY - (drawY + height / 2), targetX - (drawX + width / 2));
		//Stats
		double speed = shotDetails.getSpeed();
		double seekingAmount = shotDetails.getSeekingAmount();
		double lifespan = shotDetails.getLifespan();
		int damage = shotDetails.getDamage();
		int shotWidth = shotDetails.getShotWidth();
		int shotHeight = shotDetails.getShotHeight();
		Image sprite = shotDetails.getSprite();
		int numShots = shotDetails.getNumShots();
		double spreadAngle = shotDetails.getSpreadAngle();
		int pierce = shotDetails.getPierce();
		boolean playerProj = shotDetails.getPlayerProj();
		double shotAngle;
		//For multiple shots
		if (numShots > 1) {
			//If spreadAngle == 360, angle between all shots should be same
			if(spreadAngle == 360) {
				shotAngle = spreadAngle / (double) numShots  * Math.PI / 180.0;
			}
			//Otherwise, angle between all shots EXCEPT 2 outermost shots should be same
			else {
				shotAngle = (spreadAngle / (double) (numShots - 1)) * Math.PI / 180.0;
			}
			//Even number of shots
			if (numShots % 2 == 0) {
				for (int i = numShots / -2; i < numShots / 2 + 1; i++) {
					if (i < 0) {
						//Shift half the angle between shots towards target angle
						double shotRotation = targetAngle + (i * shotAngle + shotAngle / 2);
						levelState.getGameObjects().add(new Projectile(levelState, x + width / 2.0, y + height / 2.0, speed, seekingAmount, lifespan, pierce, damage, shotWidth, shotHeight, shotRotation, sprite, playerProj));
					}
					if (i > 0) {
						//Shift half the angle between shots towards target angle
						double shotRotation = targetAngle + (i * shotAngle - shotAngle / 2);
						levelState.getGameObjects().add(new Projectile(levelState, x + width / 2.0, y + height / 2.0, speed, seekingAmount, lifespan, pierce, damage, shotWidth, shotHeight, shotRotation, sprite, playerProj));
					}   
				}
			}
			//Odd number of shots
			else {
				for (int i = (numShots - 1) / -2; i < (numShots - 1) / 2 + 1; i++) {
					double shotRotation = targetAngle + (i * shotAngle);
					levelState.getGameObjects().add(new Projectile(levelState, x + width / 2.0, y + height / 2.0, speed, seekingAmount, lifespan, pierce, damage, shotWidth, shotHeight, shotRotation, sprite, playerProj));
				}
			}
		}
		else {
			levelState.getGameObjects().add(new Projectile(levelState, x + width / 2.0, y + height / 2.0, speed, seekingAmount, lifespan, pierce, damage, shotWidth, shotHeight, targetAngle, sprite, playerProj));
		}
	}


	//No return
	//No parameters
	//Sets future position by checking tile collisions
	public void setTempPos(LinkedList<GameObject> colliders) {
		int tileHeight = 50;
		int tileWidth = 50;

		tempX = x + xVel;
		tempY = y + yVel;

		//If tile intersects rect from further top left to further bottom right between frames, check it for collision
		for(int i = (int) Math.min((y / tileHeight), (tempY / tileHeight)) ; i <= (int)  Math.max(((y + height) / tileHeight), ((tempY + height) / tileHeight)); i ++) {
			for(int j = (int) Math.min((x / tileWidth), (tempX / tileWidth)) ; j <= (int)  Math.max(((x + width) / tileWidth), ((tempX + width) / tileWidth)); j ++) {
				Tile tile;
				if(i >= 0 && i < levelState.getRows() && j >= 0 && j < levelState.getCols() && (tile = levelState.getTiles()[i][j]) != null) {
					colliders.add(tile);
				}
			}
		}
		checkCollision(colliders);
	}

	//No return
	//Parameters for colliding object, gameObjects, and coliding direction
	public void collision(GameObject col, LinkedList<GameObject> gameObjects, int colDir) {
		if(col != null) {
			//Push left
			if(colDir == 0) {
				xVel = 0;
				tempX = col.x - width;
			}
			//Push right
			else if(colDir == 1) {
				xVel = 0;
				tempX = col.x + col.width;
			}
			//Push up
			else if(colDir == 2) {
				yVel = 0;
				tempY = col.y - height;
				if(this.getClass() == Player.class) {
					((Player) this).land();
				}
			}
			//Push down
			else if(colDir == 3) {
				yVel = 0;
				tempY = col.y + col.width;
			}
		}
		checkCollision(gameObjects);
	}

	//Getters & setters
	public int getHP() {
		return HP;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}

}
