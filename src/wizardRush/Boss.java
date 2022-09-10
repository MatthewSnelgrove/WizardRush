//Class for bosses
package wizardRush;

public class Boss extends Enemy{

	public Boss(LevelState levelState, double x, double y, int width, int height, int enemyNum) {
		super(levelState, x, y, width, height, enemyNum, 1, 1, 1);
	}
}