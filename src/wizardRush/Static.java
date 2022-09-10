//Class for objects that don't move
package wizardRush;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Static extends GameObject{

	public Static(LevelState levelState, double x, double y, int width, int height, Image sprite) {
		super(levelState, x, y, width, height, sprite);
	}
	
	//Parameter for graphics
	//No return
	//Draws to graphics
	public void render(Graphics g) {
		drawX = (int) Math.round(x - levelState.getPlayer().x + 390);
		drawY = (int) Math.round(y - levelState.getPlayer().y + 440);
		if(isOnScreen()) {
			g.drawImage(sprite, drawX, drawY, width, height, null);
		}
	}
}
