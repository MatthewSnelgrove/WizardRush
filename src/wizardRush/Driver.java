//Matthew Snelgrove
//Wizard Rush
//Reach in each level to clear it. Clear the final level to beat the game.
package wizardRush;
import java.awt.*;
import javax.swing.*;
public class Driver {

	public static void main(String[] args) {
		//Create frame and instantiate handler
		JFrame frame = new JFrame ("Wizard Rush");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1080, 720));
		frame.setMaximumSize(new Dimension(1080, 720));
		frame.setPreferredSize(new Dimension(1080, 720));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);	
		Handler handler = new Handler(frame);
		frame.setBackground(new Color(255,0,0));
		frame.setVisible(true);
		frame.pack();
	}
}