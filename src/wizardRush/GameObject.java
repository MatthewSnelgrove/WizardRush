//Class for all objects within the game world
package wizardRush;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.*;

import javax.swing.ImageIcon;

public abstract class GameObject {
	//Attributes shared by all GameObjects
	protected double x, y;
	protected int drawX, drawY, width, height;
	protected Image sprite;
	protected LevelState levelState;

	//Constructor
	public GameObject(LevelState levelState, double x, double y, int width, int height, Image sprite) {
		this.levelState = levelState;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		drawX = (int) Math.round(x - levelState.getIniPCol() * 50 + 390);
		drawY = (int) Math.round(y - levelState.getIniPRow() * 50 + 440);
		levelState.getGameObjects().add(this);
	}

	public abstract void update();

	public abstract void render(Graphics g);

	//No parameters
	//Returns boolean
	//Returns true if object is on screen.
	public boolean isOnScreen() {
		if(drawX < 780 && drawX + width > 0 && drawY < 720 && drawY + height > 0) {
			return true;
		}
		return false;
	}
	
	//Getters
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}