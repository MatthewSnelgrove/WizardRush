//Class for projectiles
package wizardRush;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public class Projectile extends Dynamic{
	//Attributes
	private LinkedList<Living> alreadyHit = new LinkedList<Living>();
	private double speed, seekingAmount, lifespan, rotation;
	private int pierce, damage;
	private long lifespanTimer;
	private boolean playerProj;
	private Living target;


	public Projectile(LevelState levelState, double x, double y, double speed, double seekingAmount, double lifespan,
			int pierce, int damage, int width, int height, double rotation, Image sprite, boolean playerProj) {
		super(levelState, x - width / 2.0, y - height / 2.0, Math.cos(rotation) * speed, Math.sin(rotation) * speed, width, height, sprite);
		this.speed = speed;
		this.seekingAmount = seekingAmount * Math.PI / 180.0;
		this.lifespan = lifespan * 1000;
		this.pierce = pierce;
		this.damage = damage;
		this.playerProj = playerProj;
		this.rotation = rotation;
		lifespanTimer = System.currentTimeMillis();
		if(!playerProj) {
			target = levelState.getPlayer();
		}
	}

	//No return
	//Parameter for graphics
	//Draws projectile based on player position
	@Override
	public void render(Graphics g) {
		drawX = (int) Math.round(x - levelState.getPlayer().x + 390);
		drawY = (int) Math.round(y - levelState.getPlayer().y + 440);
		if(isOnScreen()) {
			//Rotate graphics to draw rotated projectiles
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform old = g2d.getTransform();        
			g2d.rotate(rotation, drawX + width / 2, drawY + height / 2);
			g2d.drawImage(sprite, drawX, drawY, width, height, null);
			g2d.setTransform(old);
		}
	}


	//No return
	//Parameters for colliding object, gameObjects to check next, and colliding direction
	//Reponds to collision
	@Override
	public void collision(GameObject col, LinkedList<GameObject> gameObjects, int colDir) {
		//Remove if hit tile
		if(col.getClass() == Tile.class) {
			levelState.getGameObjects().remove(this);
		}
		else {
			//Reset target
			if(target == col) {
				target = null;
			}
			if(playerProj && col.getClass() == Enemy.class || col.getClass() == Boss.class) {
				if(pierce >= 0 && !alreadyHit.contains(col)) {
					pierce --;
					alreadyHit.add((Living) col);
					((Living) col).setHP(((Living) col).getHP() - damage);
				}
			}
			if(!playerProj && col.getClass() == Player.class) {
				if(pierce >= 0 && !alreadyHit.contains(col)) {
					pierce --;
					alreadyHit.add((Living) col);
					if(!((Player) col).getInvulnerable()) {
						((Living) col).setHP(((Living) col).getHP() - damage);
					}
				}
			}
			checkCollision(gameObjects);
		}
	}

	//No return
	//No parameters
	//Updates projectile every frame
	@Override
	public void update() {
		if(seekingAmount != 0) {
			if(playerProj) {
				//Sets new target if needed
				if(!levelState.getEnemies().contains(target)) {
					target = null;
					double minDistance = Double.MAX_VALUE;
					for(Enemy enemy : levelState.getEnemies()) {
						if(!alreadyHit.contains(enemy)) {
							double distance = Math.pow(enemy.getX() - x, 2) + Math.pow(enemy.getY() - y, 2);
							if(distance < minDistance){
								minDistance = distance;
								target = enemy;
							}	
						}
					}
				}
			}
			//Changes rotation capped at max rotation per frame
			if(target != null) {
				double targetRotation = Math.atan2((target.getY() + target.getHeight() / 2) - (y + height / 2), (target.getX() + target.getWidth() / 2) - (x + width / 2));
				if(Math.abs(targetRotation - rotation) % (2 * Math.PI) < seekingAmount) {
					rotation = targetRotation;
				}
				else {
					//Rotating counterclockwise
					if(targetRotation > rotation)
					{
						//If counterclockwise rotation from current rotation to target rotation less than 180 degrees, rotate counter clockwise. Otherwise, rotate clockwise.
						if(targetRotation - rotation < Math.PI){
							rotation += seekingAmount;
						}
						else {
							rotation -= seekingAmount;
						}    
					}
					//Clockwise
					else {//If clockwise rotation from current rotation to target rotation less than 180 degrees, rotate clockwise. Otherwise, rotate counter clockwise.
						if(rotation - targetRotation < Math.PI){
							rotation -= seekingAmount;
						}
						else {
							rotation += seekingAmount;
						}    
					}
				}
			}
			//Set velocity based on rotation
			xVel = Math.cos(rotation) * speed;
			yVel = Math.sin(rotation) * speed;
		}
		//Move based on velocity
		tempX = x + xVel;
		tempY = y + yVel;
		int tileHeight = levelState.getTileHeight();
		int tileWidth = levelState.getTileWidth();
		//Check collision
		LinkedList<GameObject> colliders = new LinkedList<GameObject>();
		for(int i = (int) Math.min((y / tileHeight), (tempY / tileHeight)) ; i <= (int)  Math.max(((y + height) / tileHeight), ((tempY + height) / tileHeight)); i ++) {
			for(int j = (int) Math.min((x / tileWidth), (tempX / tileWidth)) ; j <= (int)  Math.max(((x + width) / tileWidth), ((tempX + width) / tileWidth)); j ++) {
				Tile tile;
				if(i >= 0 && i < levelState.getRows() && j >= 0 && j < levelState.getCols() && (tile = levelState.getTiles()[i][j]) != null) {
					colliders.add(tile);
				}
			}
		}
		//Check enemies if player proj
		if(playerProj) {
			for(Enemy enemy: levelState.getEnemies()) {
				if(axisOverlap(tempX, enemy.tempX, tempX + width, enemy.tempX + enemy.width, -1) && axisOverlap(tempY, enemy.tempY, tempY + height, enemy.tempY + enemy.height, -1)) {
					colliders.add(enemy);
				}
			}
		}
		//Check player if enemy proj
		else {
			colliders.add(levelState.getPlayer());
		}
		checkCollision(colliders);
		move();
		//Check lifespan
		if(System.currentTimeMillis() - lifespanTimer > lifespan) {
			levelState.getGameObjects().remove(this);
		}
		//Check pierce
		if(pierce < 0) {
			levelState.getGameObjects().remove(this);
		}
	}
}
