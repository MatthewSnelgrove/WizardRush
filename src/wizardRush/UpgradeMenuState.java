//Class for upgrade menu
package wizardRush;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UpgradeMenuState extends JPanel{
	//Attibutes
	private JLabel money;
	private JButton maxHP = new JButton();
	private JButton regen = new JButton();
	private JButton speed = new JButton();
	private JButton damage = new JButton();
	private JButton pierce = new JButton();
	private JButton shotDelay = new JButton();
	private JButton secondaryCooldown = new JButton();
	private JButton maxJumps = new JButton();
	private Clip buyUpgrade, noUpgrade;
	public UpgradeMenuState(Handler handler) {
		initialize(handler);
	}

	//No return
	//No paramters
	//Loads sounds
	public void loadSounds() {
		try {
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/buyUpgrade.wav"));
			buyUpgrade = AudioSystem.getClip();
			buyUpgrade.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/noUpgrade.wav"));
			noUpgrade = AudioSystem.getClip();
			noUpgrade.open(sound);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}

	//No return
	//Parameter for handler
	//Initializes menu
	public void initialize(Handler handler) {
		loadSounds();
		this.setLayout(null);
		money = new JLabel("$" + handler.getMoney());
		money.setFont(new Font("TimesRoman", Font.BOLD, 30));
		money.setBounds(230, 50, 500, 50);
		this.add(money);

		//Upgrade buttons menu
		JPanel upgradeButtons = new JPanel();
		upgradeButtons.setLayout(new GridLayout(4, 4));
		upgradeButtons.setBounds(25, 125, 1030, 450);

		//Check if maxed
		if(handler.getStatLevel("maxHP") == 9) {
			maxHP.setText("MAXED");
			maxHP.setEnabled(false);
		}
		else {
			//Set text
			maxHP.setText("<html>Increase max HP to " + handler.getNextMaxHP() + "<br>Cost: $" + handler.getMaxHPCost() + "</html>");
		}
		//Add actionlistener
		maxHP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getMaxHPCost())) {
					handler.increaseStatLevel("maxHP");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("maxHP") == 9) {
						maxHP.setText("MAXED");
						maxHP.setEnabled(false);
					}
					else {
						maxHP.setText("<html>Increase max HP to " + handler.getNextMaxHP() + "<br>Cost: $" + handler.getMaxHPCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(maxHP);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/Heart.png")));
		System.out.println(maxHP.getWidth()+ " " + maxHP.getHeight());

		//Repeat for other buttons

		if(handler.getStatLevel("regen") == 9) {
			regen.setText("MAXED");
			regen.setEnabled(false);
		}
		else {
			regen.setText("<html>Increase regen to " + handler.getNextRegen() + "<br>Cost: $" + handler.getRegenCost() + "</html>");
		}
		regen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getRegenCost())) {
					handler.increaseStatLevel("regen");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("regen") == 9) {
						regen.setText("MAXED");
						regen.setEnabled(false);
					}
					else {
						regen.setText("<html>Increase regen to " + handler.getNextRegen() + "<br>Cost: $" + handler.getRegenCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(regen);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/Regen.png")));


		if(handler.getStatLevel("speed") == 9) {
			speed.setText("MAXED");
			speed.setEnabled(false);
		}
		else {
			speed.setText("<html>Increase speed to " + handler.getNextSpeed() + "<br>Cost: $" + handler.getSpeedCost() + "</html>");
		}
		speed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getSpeedCost())) {
					handler.increaseStatLevel("speed");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("speed") == 9) {
						speed.setText("MAXED");
						speed.setEnabled(false);
					}
					else {
						speed.setText("<html>Increase speed to " + handler.getNextSpeed() + "<br>Cost: $" + handler.getSpeedCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(speed);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/Speed.png")));

		if(handler.getStatLevel("damage") == 9) {
			damage.setText("MAXED");
			damage.setEnabled(false);
		}
		else {
			damage.setText("<html>Increase damage to " + handler.getDamage() + "<br>Cost: $" + handler.getDamageCost() + "</html>");
		}
		damage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getDamageCost())) {
					handler.increaseStatLevel("damage");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("damage") == 9) {
						damage.setText("MAXED");
						damage.setEnabled(false);
					}
					else {
						damage.setText("<html>Increase damage to " + handler.getDamage() + "<br>Cost: $" + handler.getDamageCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(damage);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/Damage.png")));


		if(handler.getStatLevel("pierce") == 9) {
			pierce.setText("MAXED");
			pierce.setEnabled(false);
		}
		else {
			pierce.setText("<html>Increase pierce to " + handler.getNextPierce() + "<br>Cost: $" + handler.getPierceCost() + "</html>");
		}
		pierce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getPierceCost())) {
					handler.increaseStatLevel("pierce");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("pierce") == 9) {
						pierce.setText("MAXED");
						pierce.setEnabled(false);
					}
					else {
						pierce.setText("<html>Increase pierce to " + handler.getNextPierce() + "<br>Cost: $" + handler.getPierceCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(pierce);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/Pierce.png")));


		if(handler.getStatLevel("shotDelay") == 9) {
			shotDelay.setText("MAXED");
			shotDelay.setEnabled(false);
		}
		else {
			shotDelay.setText("<html>Decrease shot delay to " + handler.getNextShotDelay() + "<br>Cost: $" + handler.getShotDelayCost() + "</html>");
		}
		shotDelay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getShotDelayCost())) {
					handler.increaseStatLevel("shotDelay");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("shotDelay") == 9) {
						shotDelay.setText("MAXED");
						shotDelay.setEnabled(false);
					}
					else {
						shotDelay.setText("<html>Decrease shot delay to " + handler.getNextShotDelay() + "<br>Cost: $" + handler.getShotDelayCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(shotDelay);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/ShotDelay.png")));


		if(handler.getStatLevel("secondaryCooldown") == 9) {
			secondaryCooldown.setText("MAXED");
			secondaryCooldown.setEnabled(false);
		}
		else {
			secondaryCooldown.setText("<html>Decrease secondary cooldown to " + handler.getNextSecondaryCooldown() + "<br>Cost: $" + handler.getSecondaryCooldownCost() + "</html>");
		}
		secondaryCooldown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getSecondaryCooldownCost())) {
					handler.increaseStatLevel("secondaryCooldown");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("secondaryCooldown") == 9) {
						secondaryCooldown.setText("MAXED");
						secondaryCooldown.setEnabled(false);
					}
					else {
						secondaryCooldown.setText("<html>Decrease secondary cooldown to " + handler.getNextSecondaryCooldown() + "<br>Cost: $" + handler.getSecondaryCooldownCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(secondaryCooldown);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/SecondaryCooldown.png")));


		if(handler.getStatLevel("maxJumps") == 9) {
			maxJumps.setText("MAXED");
			maxJumps.setEnabled(false);
		}
		else {
			maxJumps.setText("<html>Increase max jumps to " + handler.getNextMaxJumps() + "<br>Cost: $" + handler.getMaxJumpsCost() + "</html>");
		}
		maxJumps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(handler.decreaseMoney(handler.getMaxJumpsCost())) {
					handler.increaseStatLevel("maxJumps");
					money.setText("$" + handler.getMoney());
					playBuyUpgradeSound();
					if(handler.getStatLevel("maxJumps") == 9) {
						maxJumps.setText("MAXED");
						maxJumps.setEnabled(false);
					}
					else {
						maxJumps.setText("<html>Increase max jumps to " + handler.getNextMaxJumps() + "<br>Cost: $" + handler.getMaxJumpsCost() + "</html>");
					}
				}
				else {
					playNoUpgradeSound();
				}
			}		
		});
		upgradeButtons.add(maxJumps);
		upgradeButtons.add(new JLabel(new ImageIcon("src/res/sprites/MaxJumps.png")));

		this.add(upgradeButtons);

		JButton levelSelectMenuButton = new JButton("Level select");
		levelSelectMenuButton.setBounds(25, 615, 400, 50);
		levelSelectMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openLevelSelectMenu();
			}
		});
		this.add(levelSelectMenuButton);

		JButton mainMenuButton = new JButton("Main menu");
		mainMenuButton.setBounds(450, 615, 400, 50);
		mainMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handler.openMainMenu();
			}

		});
		this.add(mainMenuButton);
	}

	//No return
	//No parameters
	//Players upgrade sound
	public void playBuyUpgradeSound() {
		buyUpgrade.setFramePosition(0);
		buyUpgrade.start();
	}

	//No return
	//No parameters
	//Players noUpgrade sound if can't afford
	public void playNoUpgradeSound() {
		noUpgrade.setFramePosition(0);
		noUpgrade.start();
	}


}
