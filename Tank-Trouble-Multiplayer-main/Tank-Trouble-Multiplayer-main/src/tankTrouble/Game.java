package tankTrouble;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game implements Runnable {

	public static int width, height;

	private JFrame frame;
	private Canvas canvas;
	private Thread thread;
	private boolean running = false;

	private BufferStrategy bs;
	private Graphics g;

	private Key key;
	public static Tank tank;
	public static TankMP tankMP;
	public static ArrayList<TankMP> tankMPs;

	public static Level level;
	public static GameServer server;
	public static GameClient client;
	public static String username = "";

//	private PowerUpFactory powerUpFactory;
	private HealthPowerUpFactory healthPowerUpFactory = new HealthPowerUpFactory();
	private SpeedPowerUpFactory speedPowerUpFactory = new SpeedPowerUpFactory();
	private ShootPowerUpFactory shootPowerUpFactory = new ShootPowerUpFactory();
	private Random random = new Random();
	private ArrayList<PowerUp> powerUps;

	public Game(int w, int h) {
		width = w;
		height = h;

		// Initialize the PowerUpFactory and power-ups list
//		powerUpFactory = new PowerUpFactory();
		powerUps = new ArrayList<>();

		frame = new JFrame("Tank Trouble");
		canvas = new Canvas();

		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		key = new Key();
		frame.addKeyListener(key);
		frame.add(canvas);
		frame.pack();

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (client != null) {
					String toSend = "-1 " + GameClient.playerNumAssigned + " ";
					client.sendData(toSend.getBytes(), GameClient.ipAddress, GameServer.port);
				}
			}
		});
	}
	public PowerUp createRandomPowerUp(int x, int y) {
		int choice = random.nextInt(3); // Choose between 0, 1, and 2

		switch (choice) {
			case 0:
				return healthPowerUpFactory.createPowerUp(x, y);
			case 1:
				return speedPowerUpFactory.createPowerUp(x, y);
			case 2:
				return shootPowerUpFactory.createPowerUp(x, y);
			default:
				throw new IllegalArgumentException("Unknown power-up type");
		}
	}

	public void initServer() {
		server = new GameServer();
		server.start();
	}

	public void initClient(String ip) {
		client = new GameClient(ip);
		client.start();
		String toSend = "00 ";
		toSend += (int) tank.pos.x + " ";
		toSend += (int) tank.pos.y + " ";
		toSend += username + " ";

		client.sendData(toSend.getBytes(), GameClient.ipAddress, GameServer.port);
	}

	public void init() {
		level = new Level(15);
		// Select a strategy based on desired attributes, or randomly
		TankStrategy strategy;
		int strategyType = new Random().nextInt(4); // randomly pick between 0, 1, 2, and 3
		switch (strategyType) {
			case 0:
				strategy = new HeavyTankStrategy();
				break;
			case 1:
				strategy = new RapidFireTankStrategy();
				break;
			case 2:
				strategy = new DefaultTankStrategy();
				break;
			case 3:
				strategy = new LightTankStrategy();
				break;
			default:
				strategy = new DefaultTankStrategy(); // Default case
		}
		tank = new Tank(new Vector(240, 240), 30, 30, new Color(51, 204, 51), new Color(31, 122, 31), strategy);

//		// Spawn power-ups when the game starts
//		spawnInitialPowerUps();

		if (JOptionPane.showConfirmDialog(null, "Do You Want To Run The Server?", "Query", 1) == 0) {
			initServer();
			username = JOptionPane.showInputDialog(null, "Enter username: ");
		} else {
			if (JOptionPane.showConfirmDialog(null, "Do You Want To Join Server", "Query", 1) == 0) {
				String address = JOptionPane.showInputDialog(null, "Enter IPAddress: ");
				username = JOptionPane.showInputDialog(null, "Enter username: ");
				initClient(address);
			}
		}

		tank.assignColor();
		if (tankMP != null)
			tankMP.assignColor();

		tankMPs = new ArrayList<TankMP>();

		// Create a few power-ups at random positions for the start of the game
		powerUps.add(createRandomPowerUp(100, 100));
		powerUps.add(createRandomPowerUp(200, 150));
		powerUps.add(createRandomPowerUp(300, 200));

		// You can also create specific types at specific locations
		// powerUps.add(powerUpFactory.createHealthPowerUp(400, 100));
		// powerUps.add(powerUpFactory.createSpeedPowerUp(500, 150));
	}

//	// Add a few power-ups at the start of the game
//	private void spawnInitialPowerUps() {
//		powerUps.add(new PowerUp(100, 100, "speed"));
//		powerUps.add(new PowerUp(250, 300, "health"));
//		powerUps.add(new PowerUp(200, 400, "speed"));
//		powerUps.add(new PowerUp(150, 150, "health"));
//		powerUps.add(new PowerUp(250, 400, "shoot"));
//		powerUps.add(new PowerUp(150, 400, "shoot"));
//	}

	public void start() {
		if (!running) {
			thread = new Thread(this);
			thread.start();
			running = true;
		}
	}

	@Override
	public void run() {
		init();
		while (running) {
			tick();
			render();
			try {
				Thread.sleep(1000 / 120);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void tick() {
		key.tick();

		// Check for collisions with power-ups
		for (PowerUp powerUp : powerUps) {
			if (powerUp.checkCollision(tank)) {
				powerUp.applyEffect(tank);  // Apply effect (e.g., restore health)
				powerUps.remove(powerUp);   // Remove the power-up once it has been collected
				break;  // Stop checking after the first collision
			}
		}

		for (int i = 0; i < tankMPs.size(); i++) {
			tankMPs.get(i).tick();
		}
		tank.tick();
	}

	public void render() {
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();

		g.clearRect(0, 0, width, height);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Render game elements
		level.render(g);
		tank.render(g);
		for (int i = 0; i < tankMPs.size(); i++) {
			tankMPs.get(i).render(g);
		}

		// Render power-ups
		for (PowerUp powerUp : powerUps) {
			powerUp.render(g);  // Render each power-up
		}

		tank.renderHealth(g);
		for (int i = 0; i < tankMPs.size(); i++) {
			tankMPs.get(i).renderHealth(g);
		}

		g.setColor(Color.orange.darker());
		g.setFont(new Font("sans-serif", Font.BOLD, 12));

		if (server != null && server.inGame) {
			g.drawString("Press L to return to lobby", Game.width - 150, Game.height - 20);
		} else if (server != null && server.inLobby) {
			g.drawString("Press G to start game", Game.width - 150, Game.height - 20);
		}

		bs.show();
		g.dispose();
	}

	public void stop() {
		if (running) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
