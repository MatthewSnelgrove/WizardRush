//Class that handles logic that trnascends levels
package wizardRush;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Handler {
	//Attirbutes
	private boolean test = false;
	protected JFrame frame;
	private int money;
	private JPanel currPanel;
	private Map<Integer, ArrayList<String>> times;
	private int[][] maxHP, maxJumps, pierce, damage, secondaryCooldown;
	private double[][] speed, shotDelay, regen;
	private Map<String, Integer> statLevels;
	private boolean won;

	public Handler(JFrame frame) {
		this.frame = frame;

		//Initialize times
		times = new HashMap<Integer, ArrayList<String>>();
		for(int i = 1; i < 10; i ++) {
			times.put(i, new ArrayList<String>());
		}

		//Initialize stat upgrade details
		statLevels = new HashMap<String, Integer>();
		statLevels.put("maxHP", 0);
		statLevels.put("regen", 0);
		statLevels.put("speed", 0);
		statLevels.put("damage", 0);
		statLevels.put("pierce", 0);
		statLevels.put("shotDelay", 0);
		statLevels.put("secondaryCooldown", 0);
		statLevels.put("maxJumps", 0);

		//Load stats upgrade details from file
		try {
			BufferedReader inFile = new BufferedReader(new FileReader("src/res/PlayerStats/PlayerStats.txt"));
			maxHP = new int[10][2];
			loadStat(inFile, maxHP);
			regen = new double[10][2];
			loadStat(inFile, regen);
			speed = new double[10][2];
			loadStat(inFile, speed);
			damage = new int[10][2];
			loadStat(inFile, damage);
			pierce = new int[10][2];
			loadStat(inFile, pierce);
			shotDelay = new double[10][2];
			loadStat(inFile, shotDelay);
			secondaryCooldown = new int[10][2];
			loadStat(inFile, secondaryCooldown);
			maxJumps = new int[10][2];
			loadStat(inFile, maxJumps);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Open main menu when starting game
		currPanel = new JPanel();
		openMainMenu();

		money = 0;
	}

	//No return
	//Parameters for BufferedReader and stats
	//Reads stat upgrade details from file
	public void loadStat(BufferedReader BI, int[][] stat) {
		for(int i = 0; i < 10; i ++) {
			String[] statCost = new String[2];
			try {
				statCost = BI.readLine().split(" ");
				stat[i] = new int[] {Integer.parseInt(statCost[0]), Integer.parseInt(statCost[1])};
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//No return
	//Parameters for BufferedReader and stats
	//Override for doubles
	public void loadStat(BufferedReader BI, double[][] stat) {
		for(int i = 0; i < 10; i ++) {
			String[] statCost = new String[2];
			try {
				statCost = BI.readLine().split(" ");
				stat[i] = new double[] {Double.parseDouble(statCost[0]), Double.parseDouble(statCost[1])};
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//No return
	//No parameters
	//Opens main menu
	public void openMainMenu() {
		frame.remove(currPanel);
		currPanel = new MainMenuState(this);
		frame.add(currPanel);
		frame.pack();
	}

	//No return
	//No parameters
	//Opens level select menu
	public void openLevelSelectMenu() {
		frame.remove(currPanel);
		currPanel = new LevelSelectMenuState(this);
		frame.add(currPanel);
		frame.pack();
	}

	//No return
	//No parameters
	//Opens upgrade menu
	public void openUpgradeMenu() {
		frame.remove(currPanel);
		currPanel = new UpgradeMenuState(this);
		frame.add(currPanel);
		frame.pack();
	}

	public void openAboutMenu() {
		frame.remove(currPanel);
		currPanel = new AboutMenuState(this);
		frame.add(currPanel);
		frame.pack();
	}

	//No return
	//Parameter for level
	//Starts level
	public void startLevel(int level) {
		frame.remove(currPanel);
		currPanel = new LevelState(this, level);
		frame.add(currPanel);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
	}
	
	//No return
	//Parameter for change in money
	//Increases money
	public void increaseMoney(int change) {
		money += change;
	}

	//Returns boolean
	//Parameter for change in money
	//Reduces money if affordable. Returns true if reduction successful;
	public boolean decreaseMoney(int change) {
		if(change > money) {
			return false;
		}
		money -= change;
		return true;
	}
	
	//No return
	//Parameter for panel
	//Opens popup if final level cleared for the first time;
	public void win(JPanel panel) {
		if(won == false) {
			won = true;
			JOptionPane win = new JOptionPane();
			panel.add(win);
			win.showMessageDialog(panel, "Congratulations! You beat the game!");
		}
		won = true;
	}
	
	//Getters
	public int getMoney() {
		return money;
	}

	public JFrame getFrame() {
		return frame;
	}
	public Map<Integer, ArrayList<String>> getTimes(){
		return times;
	}

	public int getMaxHP() {
		return maxHP[statLevels.get("maxHP")][0];
	}
	public double getRegen() {
		return regen[statLevels.get("regen")][0];
	}
	public double getSpeed() {
		return speed[statLevels.get("speed")][0];
	}
	public int getDamage() {
		return damage[statLevels.get("damage")][0];
	}
	public int getPierce() {
		return pierce[statLevels.get("pierce")][0];
	}
	public double getShotDelay() {
		return shotDelay[statLevels.get("shotDelay")][0];
	}
	public int getSecondaryCooldown() {
		return secondaryCooldown[statLevels.get("secondaryCooldown")][0];
	}
	public int getMaxJumps() {
		return maxJumps[statLevels.get("maxJumps")][0];
	}
	public int getNextMaxHP() {
		return maxHP[statLevels.get("maxHP") + 1][0];
	}
	public double getNextRegen() {
		return regen[statLevels.get("regen") + 1][0];
	}
	public double getNextSpeed() {
		return speed[statLevels.get("speed") + 1][0];
	}
	public int getNextDamage() {
		return damage[statLevels.get("damage") + 1][0];
	}
	public int getNextPierce() {
		return pierce[statLevels.get("pierce") + 1][0];
	}
	public double getNextShotDelay() {
		return shotDelay[statLevels.get("shotDelay") + 1][0];
	}
	public int getNextSecondaryCooldown() {
		return secondaryCooldown[statLevels.get("secondaryCooldown") + 1][0];
	}
	public int getNextMaxJumps() {
		return maxJumps[statLevels.get("maxJumps") + 1][0];
	}
	public int getMaxHPCost() {
		return maxHP[statLevels.get("maxHP") + 1][1];
	}
	public int getRegenCost() {
		return (int) regen[statLevels.get("regen") + 1][1];
	}
	public int getSpeedCost() {
		return (int) speed[statLevels.get("speed") + 1][1];
	}
	public int getDamageCost() {
		return damage[statLevels.get("damage") + 1][1];
	}
	public int getPierceCost() {
		return pierce[statLevels.get("pierce") + 1][1];
	}
	public int getShotDelayCost() {
		return (int) shotDelay[statLevels.get("shotDelay") + 1][1];
	}
	public int getSecondaryCooldownCost() {
		return secondaryCooldown[statLevels.get("secondaryCooldown") + 1][1];
	}
	public int getMaxJumpsCost() {
		return maxJumps[statLevels.get("maxJumps") + 1][1];
	}
	public void increaseStatLevel(String stat) {
		statLevels.put(stat, statLevels.get(stat) + 1);
	}
	public int getStatLevel(String stat) {
		return statLevels.get(stat);
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	public boolean getTest() {
		return test;
	}
	public boolean getWon() {
		return won;
	}




}
