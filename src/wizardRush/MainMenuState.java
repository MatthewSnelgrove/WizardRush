//Class for main menu
package wizardRush;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenuState extends JPanel{

	public MainMenuState(Handler handler) {
		initialize(handler);
	}

	//No return
	//Parameter for handler
	//Initializes menu stuff
	public void initialize(Handler handler) {
		this.setLayout(null);
		JLabel title = new JLabel("Wizard Rush");
		title.setFont(new Font("TimesRoman", Font.BOLD, 75));
		title.setBounds(315, 50, 500, 200);
		this.add(title);
		
		JLabel name = new JLabel("Matthew Snelgrove");
		name.setFont(new Font("TimesRoman", Font.BOLD, 40));
		name.setBounds(355, 175, 500, 200);
		this.add(name);
		
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openLevelSelectMenu();
			}
		});
		playButton.setAlignmentX(CENTER_ALIGNMENT);
		playButton.setBounds(440, 400, 200, 100);
		this.add(playButton);

		JButton aboutMenuButton = new JButton("About");
		aboutMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openAboutMenu();
			}
		});
		aboutMenuButton.setAlignmentX(CENTER_ALIGNMENT);
		aboutMenuButton.setBounds(465, 575, 150, 75);
		this.add(aboutMenuButton);
		this.setBackground(Color.RED);
		this.revalidate();
		this.repaint();
		
	}

}
