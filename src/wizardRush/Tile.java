//Class for tiles
package wizardRush;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Tile extends Static{
	//Attributes
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;

	//Constructor
	public Tile(LevelState levelState, double x, double y, Image sprite) {
		super(levelState, x, y, WIDTH, HEIGHT, sprite);
	}

	@Override
	public void update() {
	}
	
	
}
