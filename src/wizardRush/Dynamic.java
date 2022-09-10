//Class for all gameObejcts that move
package wizardRush;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public abstract class Dynamic extends GameObject{

	//Attributes
	protected double xVel, yVel, tempX, tempY;
	public Dynamic(LevelState levelState, double x, double y, double xVel, double yVel, int width, int height, Image sprite) {
		super(levelState, x, y, width, height, sprite);
		this.xVel = xVel;
		this.yVel = yVel;
		this.tempX = x + xVel;
		this.tempY = y + yVel;
	}

	//No return
	//No parameters
	//Updates position
	public void move() {
		setX(tempX);
		setY(tempY);
	}
	
	//No return
	//Parameters for targetX and targetY coordinates
	//Checks for collisions. Interpolates first collision using velocity.
	public void checkCollision(LinkedList<GameObject> gameObjects) {
		//Coordinates of edges of target position
		double minX = tempX;
		double maxX = tempX + width;
		double minY = tempY;
		double maxY = tempY + height;

		//Tracks time when first collision happened - collision happened earlyColTime frames ago.
		double earlyColTime = 0;
		//Tracks first tile to collide
		GameObject firstCol = null;
		//Tracks direction of first collision
		int colDir = -1;

		for(GameObject gameObject : gameObjects) {
			//Colliding object edges
			double minX2 = gameObject.x;
			double maxX2 = gameObject.x + gameObject.width;
			double minY2 = gameObject.y;
			double maxY2 = gameObject.y + gameObject.width;

			//Play and enemies get lenience when checking collisions so they do not catch when sliding across tiles
			double lenience = 0.1;
			if(this.getClass() == Projectile.class) {
				lenience = -1;
			}
			
			//If projectile spawns inside object, trigger collision
			if(this.getClass() == Projectile.class && axisOverlap(minX - xVel, minX2, maxX - xVel, maxX2, 0) && axisOverlap(minY - yVel, minY2, maxY - yVel, maxY2, 0)) {
				firstCol = gameObject;
				colDir = 0;
				break;
			}
			//Right of this passes through left of checked GO
			if(maxX > minX2 && xVel != 0) {
				double tempTime = (maxX - minX2) / xVel;
				//If there was overlap in y coordinate when left/right edges intersected --> collision
				//If tempTime > 1 the intersection of the edges would have happened before previous frame. This cannot happen unless projectile.
				if((tempTime <= 1.001) && axisOverlap(minY - yVel * tempTime, minY2, maxY - yVel * tempTime, maxY2, lenience)) {
					if(tempTime > earlyColTime) {
						earlyColTime = tempTime;
						firstCol = gameObject;
						colDir = 0;
					}
				}
			}
			//Left of this passes through right of GO
			if(minX < maxX2 && xVel != 0) {
				double tempTime = (minX - maxX2) / xVel;
				if((tempTime <= 1.001) && axisOverlap(minY - yVel * tempTime, minY2, maxY - yVel * tempTime, maxY2, lenience)) {
					if(tempTime > earlyColTime) {
						earlyColTime = tempTime;
						firstCol = gameObject;
						colDir = 1;
					}
				}
			}
			//Bottom of this passes through top of GO
			if(maxY > minY2 && yVel != 0) {
				double tempTime = (maxY - minY2) / yVel;
				if((tempTime <= 1.001) && axisOverlap(minX - xVel * tempTime, minX2, maxX - xVel * tempTime, maxX2, lenience)) {
					if(tempTime > earlyColTime) {
						earlyColTime = tempTime;
						firstCol = gameObject;
						colDir = 2;
					}
				}
			}
			//Top of this passes through bottom of GO
			if(minY < maxY2 && yVel != 0) {
				double tempTime = (minY - maxY2) / yVel;
				if((tempTime <= 1.001) && axisOverlap(minX - xVel * tempTime, minX2, maxX - xVel * tempTime, maxX2, lenience)) {
					if(tempTime > earlyColTime) {
						earlyColTime = tempTime;
						firstCol = gameObject;
						colDir = 3;
					}
				}
			}
		}
		//If collision
		if(colDir != -1) {
			//Respond to collision for first colliding object
			gameObjects.remove(firstCol);
			collision(firstCol, gameObjects, colDir);
		} 

	}

	public abstract void collision(GameObject gameObject, LinkedList<GameObject> gameObjects, int colDir);

	@Override
	public abstract void render(Graphics g);

	//Returns boolean
	//Parameters for min/max coordinate in axis, and lenience
	//Returns true if there if an overlap between the with coordinates min1, max1 and min2, max2 in a given axis.
	public boolean axisOverlap(double min1, double min2, double max1, double max2, double lenience) {
		if((Math.max(max1, max2) - Math.min(min1, min2)) <= (max1 - min1 + max2 - min2 - lenience)) {
			return true;
		}
		return false;
	}

	//Setters
	public void setXVel(double xVel) {
		this.xVel = xVel;
	}
	public void setYVel(double yVel) {
		this.yVel = yVel;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}

}
