//Class for about menu
package wizardRush;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutMenuState extends JPanel{
	public AboutMenuState(Handler handler) {
		initialize(handler);
	}
	public void initialize(Handler handler){
		//Set menu attributes
		this.setLayout(new GridLayout(0, 1, 1, 1));
		this.add(new JLabel("By Matthew Snelgrove"));
		this.add(new JLabel("WASD to move. Point and click to shoot."));
		this.add(new JLabel("Press spacebar to use a powerful attack. It has a long down, however, so use it wisely."));
		this.add(new JLabel("Reach the goal in level 9 to beat the game"));
		this.add(new JLabel("To activate the goal in levels 3, 6, and 9, you must defeat the boss."));
		this.add(new JLabel("To make it easier (possible) to beat the final boss, use money earned from defeating enemies to upgrade your stats."));
		this.add(new JLabel("Have fun!"));
		JButton mainMenuButton = new JButton("Back");
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openMainMenu();
			}
		});
		this.add(mainMenuButton);
		this.setBackground(new Color(0,255,0));
	}
}