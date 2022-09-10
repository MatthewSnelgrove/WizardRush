//Class for goal that player must reach in each level
package wizardRush;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Goal extends Static{
	private static final Image SPRITE = new ImageIcon("src/res/sprites/Goal.png").getImage();

	public Goal(LevelState levelState, double x, double y, int width, int height) {
		super(levelState, x, y, width, height, SPRITE);
	}

	@Override
	public void update() {
	}

}
