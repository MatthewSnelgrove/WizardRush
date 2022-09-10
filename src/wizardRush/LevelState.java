//Class that controls game logic within a level
package wizardRush;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class LevelState extends JPanel implements Runnable, KeyListener, MouseListener{

	//Attibutes
	private Thread thread;
	private Handler handler;
	private BufferedImage game;
	private BufferedImage GUI;
	private boolean running = true;
	private Clip playerShoot, jump, enemyDefeated, playerDefeated, levelClear;
	private int fps = 60;
	private long time;
	private int level;
	private static final int TILE_WIDTH = 50;
	private static final int TILE_HEIGHT = 50;
	private LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	private Tile[][] tiles;
	private int rows;
	private int cols;
	private Image[] tileSprites;
	private int iniPRow;
	private int iniPCol;
	private double gravity;
	private Goal goal;
	private boolean levelEnd;
	private int totalEnemies;
	private Image background;
	private Boss boss;
	private Player player;


	public LevelState(Handler handler, int level) {
		this.handler = handler;
		this.level = level;
		initialize();
	}

	//No return
	//No parameters
	//Loads sprites
	public void loadSprites() {
		tileSprites = new Image[3];
		tileSprites[0] = null;
		tileSprites[1] = new ImageIcon("src/res/sprites/dirtTile.png").getImage();
		tileSprites[2] = new ImageIcon("src/res/sprites/grassTile.png").getImage();
		background = new ImageIcon("src/res/sprites/Background.png").getImage();
	}

	//No return
	//No parameters
	//Loads tiles
	public void loadTiles(BufferedReader inFile) {
		try {
			cols = Integer.parseInt(inFile.readLine());
			rows = Integer.parseInt(inFile.readLine());
			iniPCol = Integer.parseInt(inFile.readLine());
			iniPRow = Integer.parseInt(inFile.readLine());
			gravity = Double.parseDouble(inFile.readLine());
			tiles = new Tile[rows][cols];
			for(int i = 0; i < rows; i ++) {
				String[] s = inFile.readLine().split(" ");
				for(int j = 0; j < cols; j ++) {
					int tileType = Integer.parseInt(s[j]);
					if(tileType != 0) {
						if(tileType == 9) {
							goal = new Goal(this, 50 * j, 50 * i, 50, 50);
						}
						else {
							tiles[i][j] = new Tile(this, 50 * j, 50 * i, tileSprites[tileType]);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//No return
	//No parameters
	//Loads enemies
	public void loadEnemies(BufferedReader inFile) {

		try {
			totalEnemies = Integer.parseInt(inFile.readLine());
			for(int i = 0; i < totalEnemies; i ++) {
				Enemy enemy = new Enemy(this, 50 * Integer.parseInt(inFile.readLine()), 50 * Integer.parseInt(inFile.readLine()), 50, 50,
						Integer.parseInt(inFile.readLine()), Double.parseDouble(inFile.readLine()), Double.parseDouble(inFile.readLine()), Double.parseDouble(inFile.readLine()));
			}
			String s;
			//Boss
			if((s = inFile.readLine()) != null) {
				boss = new Boss(this, 50 * Integer.parseInt(s), 50 * Integer.parseInt(inFile.readLine()), 350, 350, Integer.parseInt(inFile.readLine()));
				totalEnemies ++;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//No return
	//No parameters
	//Loads sounds
	public void loadSounds() {
		try {
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/playerShoot.wav"));
			playerShoot = AudioSystem.getClip();
			playerShoot.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/jump.wav"));
			jump = AudioSystem.getClip();
			jump.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/enemyDefeated.wav"));
			enemyDefeated = AudioSystem.getClip();
			enemyDefeated.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/playerDefeated.wav"));
			playerDefeated = AudioSystem.getClip();
			playerDefeated.open(sound);	
			sound = AudioSystem.getAudioInputStream(new File ("src/res/sfx/levelClear.wav"));
			levelClear = AudioSystem.getClip();
			levelClear.open(sound);	
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	//No return
	//Parameter for level
	//Initializes level - loads everything, created BufferedImages for game window and GUI, adds listeners, starts thread
	public void initialize() {
		loadSounds();
		loadSprites();
		BufferedReader inFile;
		try {
			inFile = new BufferedReader(new FileReader("src/res/levels/Level" + level + ".txt"));
			loadTiles(inFile);
			player = new Player(this, gravity * 5.34);
			loadEnemies(inFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		game = new BufferedImage(780, 720, BufferedImage.TYPE_INT_RGB);
		GUI = new BufferedImage(300, 720, BufferedImage.TYPE_INT_RGB);
		time = System.currentTimeMillis();
		handler.getFrame().addMouseListener(this);
		handler.getFrame().addKeyListener(this);

		thread = new Thread(this);
		thread.start();

	}


	//No return
	//No parameters
	//Runs thread
	@Override
	public void run() {	
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long startTime;
		long endTime;
		//Game loop
		while(running) {
			startTime = System.currentTimeMillis();
			update();
			repaint();
			endTime = System.currentTimeMillis();
			if (1000 / fps - (endTime - startTime) > 0) {
				try {
					Thread.sleep((1000 / fps) - (endTime - startTime));
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}

	//No return
	//No parameters
	//Updates all game objects
	public void update() {
		if(levelEnd) {
			levelEnd();
		}
		LinkedList<GameObject> gameObjectsClone = (LinkedList<GameObject>) gameObjects.clone();
		for(GameObject gameObject : gameObjectsClone) {
			gameObject.update();
		}
	}

	//No return
	//Parameter for graphics
	//Draws background in relation to player position as well as all gameObjects
	public void renderGame(Graphics g) {
		g.drawImage(background, -(int) player.getX() / 10 % 1000, -((int) player.getY() - 50 * rows) / 20 % 1000 - 1650, 1000, 4000, null);
		g.drawImage(background, -(int) player.getX() / 10 % 1000 + 1000, -((int) player.getY() - 50 * rows) / 20 % 1000 - 1650, 1000, 4000, null);
		LinkedList<GameObject> gameObjectsClone = (LinkedList<GameObject>) gameObjects.clone();
		for(GameObject gameObject : gameObjectsClone) {
			gameObject.render(g);
		}
	}

	//No return
	//Parameter for graphics
	//Draws GUI
	public void renderGUI(Graphics g) {
		g.setColor(new Color(150, 75, 25));
		g.fillRect(0, 0, 300, 720);
		g.setColor(new Color(225, 175, 25));
		g.drawString("$" + handler.getMoney(), 25, 25);
		g.setColor(Color.WHITE);
		g.drawString(getTimeStr(), 25, 150);
		g.setColor(Color.GRAY);
		g.fillRect(25, 300, 250, 75);
		g.setColor(Color.RED);
		g.fillRect(25, 300, (int) Math.round(Math.max(player.getHP() / (double) player.getMaxHP(), 0) * 250), 75);
		g.setColor(Color.WHITE);
		g.drawString("" + player.getHP(), 50, 325);
		g.drawString("Enemies:", 25, 400);
		g.drawString(totalEnemies - enemies.size() + " / " + totalEnemies, 25, 450);


	}

	//Takes graphics parameter
	//No return
	//Renders to panel's graphics
	public void paintComponent(Graphics g) {
		Graphics gameGraphics = game.getGraphics();
		gameGraphics.setColor(Color.WHITE);
		gameGraphics.fillRect(0, 0, 780, 720);
		renderGame(gameGraphics);
		Graphics GUIGraphics = GUI.getGraphics();
		GUIGraphics.setColor(Color.WHITE);
		GUIGraphics.fillRect(0, 0, 780, 720);
		renderGUI(GUIGraphics);
		g.drawImage(game, 0, 0, null);
		g.drawImage(GUI, 780, 0, null);

		//Level ending animation
		if(levelEnd) {
			BufferedImage all = new BufferedImage(1080, 720, BufferedImage.TYPE_INT_RGB);
			Graphics allGraphics = all.getGraphics();

			if(player.getHP() > 0) {
				allGraphics.setColor(Color.GREEN);
			}
			else {
				allGraphics.setColor(Color.RED);
			}
			allGraphics.fillRect(0, 0, 1080, 720);
			//Game window and GUI shrink towards centre
			allGraphics.drawImage(game, (60 - fps) * 540 / 55, (60 - fps) * 360 / 55, (fps - 5) * 780 / 55, (fps - 5) * 720 / 55, null);
			allGraphics.drawImage(GUI, (60 - fps) * -240 / 55 + 780, (60 - fps) * 360 / 55, (fps - 5) * 300 / 55, (fps - 5) * 720 / 55, null);
			g.drawImage(all, 0, 0, null);
		}
	}

	//Returns String
	//No parameters
	//Converts time in milli to hour:minute:second.centisecond
	public String getTimeStr() {
		long milli = System.currentTimeMillis() - time;
		long hours = milli / 3600000;
		milli -= hours * 3600000;
		long minutes = milli / 60000;
		milli -= minutes * 60000;
		long seconds = milli / 1000;
		milli -= seconds * 1000;
		long centi = milli / 10;
		if(hours > 0) {
			return new StringBuilder(hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)  + "." + String.format("%02d", centi) ).toString();
		}
		else if(minutes > 0) {
			return new StringBuilder(minutes + ":" + String.format("%02d", seconds) + "." + String.format("%02d", centi)).toString();
		}
		return new StringBuilder(seconds + "." + String.format("%02d", centi)).toString();
	}
	
	//No return
	//No parameters
	//Ends level
	public void levelEnd() {
		player.setInvulnerable(true);

		fps /= 1.02;
		if(fps < 5) {
			handler.openLevelSelectMenu();
			running = false;
		}
	}
	
	//No return
	//No parameters
	//Records time to level times, sorts times, and removes worst times if more than 3
	public void recordTime() {
		handler.getTimes().get(level).add(getTimeStr());
		handler.getTimes().get(level).sort(new Comparator<String>() {
			//Comparator
			@Override
			public int compare(String s1, String s2) {
				int s1Milli = 10 * Integer.parseInt(s1.substring(s1.indexOf(".") + 1));
				if(s1.lastIndexOf(":") != -1) {
					s1Milli += 1000 * Integer.parseInt(s1.substring(s1.lastIndexOf(":") + 1, s1.indexOf(".")));
				}
				else {
					s1Milli += 1000 * Integer.parseInt(s1.substring(0, s1.indexOf(".")));
				}
				if(s1.indexOf(":") != s1.lastIndexOf(":")) {
					s1Milli += 60000 * Integer.parseInt(s1.substring(s1.indexOf(":") + 1, s1.lastIndexOf(":")));
					s1Milli += 3600000 * Integer.parseInt(s1.substring(0, s1.indexOf(":")));
				}
				else if(s1.indexOf(":") != -1){
					s1Milli += 60000 * Integer.parseInt(s1.substring(0, s1.lastIndexOf(":")));
				}

				int s2Milli = 10 * Integer.parseInt(s2.substring(s2.indexOf(".") + 1));
				if(s2.lastIndexOf(":") != -1) {
					s2Milli += 1000 * Integer.parseInt(s2.substring(s2.lastIndexOf(":") + 1, s2.indexOf(".")));
				}
				else {
					s2Milli += 1000 * Integer.parseInt(s2.substring(0, s2.indexOf(".")));
				}
				if(s2.indexOf(":") != s2.lastIndexOf(":")) {
					s2Milli += 60000 * Integer.parseInt(s2.substring(s2.indexOf(":") + 1, s2.lastIndexOf(":")));
					s2Milli += 3600000 * Integer.parseInt(s2.substring(0, s2.indexOf(":")));
				}
				else if(s2.indexOf(":") != -1){
					s2Milli += 60000 * Integer.parseInt(s2.substring(0, s2.lastIndexOf(":")));
				}
				return s1Milli - s2Milli;
			}
		});
		if(handler.getTimes().get(level).size() > 3) {
			handler.getTimes().get(level).remove(3);
		}
	}

	//MouseListener for shooting
	@Override
	public void mousePressed(MouseEvent e) {
		player.setShooting(true);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		player.setShooting(false);
	}
	
	//KeyListsner for movement & secondary attack
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == 87) {
			player.setJumping(true);
		}
		if(key == 68) {
			player.setRight(true);
		}
		if(key == 65) {
			player.setLeft(true);
		}
		if(key == 32) {
			player.setSecondaryShooting(true);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == 87) {
			player.setJumping(false);
			player.setCanJump(true);
		}
		if(key == 68) {
			player.setRight(false);
		}
		if(key == 65) {
			player.setLeft(false);
		}
		if(key == 32) {
			player.setSecondaryShooting(false);
		}
	}


	//Getters & setters
	public Tile[][] getTiles(){
		return tiles;
	}
	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public void setLevelEnd(boolean levelEnd) {
		this.levelEnd = levelEnd;
	}
	public int getIniPRow() {
		return iniPRow;
	}
	public int getIniPCol() {
		return iniPCol;
	}
	public int getTileWidth() {
		return TILE_WIDTH;
	}
	public int getTileHeight() {
		return TILE_HEIGHT;
	}
	public LinkedList<GameObject> getGameObjects(){
		return gameObjects;
	}
	public LinkedList<Enemy> getEnemies(){
		return enemies;
	}
	public Point getMoustPos() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, this);
		return p;
	}
	public double getGravity() {
		return gravity;
	}
	public Handler getHandler() {
		return handler;
	}
	public Goal getGoal() {
		return goal;
	}
	public boolean getLevelEnd() {
		return levelEnd;
	}

	//Restarts clips and plays sounds
	public void playJumpSound() {
		jump.setFramePosition (0);
		jump.start();
	}
	public void playEnemyDefeatedSound() {
		enemyDefeated.setFramePosition (0);
		enemyDefeated.start();
	}
	public void playPlayerDefeatedSound() {
		playerDefeated.setFramePosition (0);
		playerDefeated.start();
	}
	public void playPlayerShootSound() {
		playerShoot.setFramePosition (0);
		playerShoot.start();
	}
	public void playLevelClearSound() {
		levelClear.setFramePosition(0);
		levelClear.start();
	}
	public Boss getBoss() {
		return boss;
	}
	public void defeatBoss() {
		boss = null;
	}
	@Override
	public void mouseClicked(MouseEvent e) {}		
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
