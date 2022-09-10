//Class for level selection menu
package wizardRush;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LevelSelectMenuState extends JPanel{
	//Attributes
	JLabel levelLabel, bestTime1, bestTime2, bestTime3;
	JButton play;
	int selectedLevel;
	Handler handler;
	int testCount;
	long testTime;
	
	public LevelSelectMenuState(Handler handler){
		this.handler = handler;
		initialize(handler);
	}
	
	//No return
	//Parameter for handler
	//Initializes menu stuff
	public void initialize(Handler handler) {
		//Calls win if won
		if(handler.getTimes().get(9).size() > 0 && ! handler.getWon()) {
			handler.win(this);
		}
		testCount = 0;
		testTime = System.currentTimeMillis();
		this.setLayout(null);
		JPanel levelsAndPreviewPanel = new JPanel();
		levelsAndPreviewPanel.setLayout(null);
		levelsAndPreviewPanel.setBounds(0, 0, 1080, 570);
		JPanel levelsPanel = new JPanel();
		levelsPanel.setBounds(50, 50, 780, 470);
		levelsPanel.setLayout(new GridLayout(3, 3, 50, 20));
		//Level buttons
		for(int i = 1; i < 10; i ++) {
			//Make new variable because actionlistner cannot reference non-static variable outside scope
			final int levelNum = i;
			JButton levelButton = new JButton("Level " + levelNum);
			levelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectLevel(levelNum);
					//Enable all levels and give money if you know the secret
					if(levelNum == 1) {
						if(System.currentTimeMillis() - testTime > 3000) {
							testCount = 0;
							testTime = System.currentTimeMillis();
						}
						testCount ++;
						if(testCount > 9) {
							handler.setTest(true);
							handler.increaseMoney(1000000000 - handler.getMoney());
							handler.openLevelSelectMenu();
						}
					}
				}
			});
			//Disable level buttons until beating previous level
			if(!handler.getTest() && i != 1 && handler.getTimes().get(new Integer(i - 1)).size() < 1) {
				levelButton.setEnabled(false);
			}
			levelsPanel.add(levelButton);
		}
		levelsAndPreviewPanel.add(levelsPanel);

		//Panel with info about level
		JPanel previewPanel = new JPanel();
		previewPanel.setBounds(880, 50, 150, 470);
		previewPanel.setLayout(new GridLayout(0, 1));
		previewPanel.add(levelLabel = new JLabel());
		previewPanel.add(new JLabel("Best times:"));
		previewPanel.add(bestTime1 = new JLabel());
		previewPanel.add(bestTime2 = new JLabel());
		previewPanel.add(bestTime3 = new JLabel());
		previewPanel.add(play = new JButton("Play"));
		play.setSize(100, 50);
		levelsAndPreviewPanel.add(previewPanel);
		
		this.add(levelsAndPreviewPanel);
		
		//Upgrade button
		JButton upgrades = new JButton("UPGRADES");
		upgrades.setBounds(50, 575, 350, 80);
		upgrades.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openUpgradeMenu();
			}
		});
		this.add(upgrades);
		
		//Main menu button
		JButton mainMenu = new JButton("Main menu");
		mainMenu.setBounds(450, 575, 350, 80);
		mainMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openMainMenu();
			}
		});
		this.add(mainMenu);
	}

	//No return
	//Parameter for level
	//Changes displayed details and the actionlistener on the play button to reflect currently selected level
	public void selectLevel(int level) {
		selectedLevel = level;
		levelLabel.setText("Level " + level);
		String[] times = new String[3];
		for(int i = 0; i < handler.getTimes().get(new Integer(level)).size(); i ++) {
			times[i] = handler.getTimes().get(new Integer(level)).get(i).toString();
		}
		for(int i = handler.getTimes().get(new Integer(level)).size(); i < 3; i ++) {
			times[i] = " - - - - - - ";
		}
		bestTime1.setText(times[0]);
		bestTime2.setText(times[1]);
		bestTime3.setText(times[2]);
		if(play.getActionListeners().length > 0) {
			play.removeActionListener(play.getActionListeners()[0]);
		}
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.startLevel(level);
			}
		});
	}
}
