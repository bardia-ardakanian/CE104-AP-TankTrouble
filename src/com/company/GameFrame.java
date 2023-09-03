package com.company;

/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

/**
 * Packages
 */
import com.company.CarePackage.CarePackage;
import com.company.EasterEgg.AirSupport;
import com.company.Obstacles.Fence;
import com.company.Obstacles.Obstacle;
import com.company.Explosives.*;

/**
 * Missile class
 * <p>This class does a triple-buffering as a modern BufferStrategy which operates as the main game engine which inherits from JFrame</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class GameFrame extends JFrame {

	/**
	 * Objects, Variables, Components, ...
	 */
	public static int GAME_HEIGHT;    // 720p game resolution
	public static int GAME_WIDTH;      // wide aspect ratio (16/9)

	private BufferStrategy bufferStrategy;

	/**
	 * Object Constructor
	 *
	 * @param title JFrame title
	 */
	public GameFrame(String title) {

		// Initialize default properties
		super(title);

		setResizable(false);                                     // Set resizable
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);            // Go full screen
		this.setUndecorated(true);                               // Remove title bar
		this.setBackground(Color.WHITE);                         // Set background color
	}

	/**
	 * This method creates a BufferStrategy for the rendering
	 */
	public void initBufferStrategy() {
		createBufferStrategy(5);
		bufferStrategy = getBufferStrategy();
	}

	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 *
	 * @param state GameState object
	 */
	public void render(GameManager state) throws IOException {
		// Render single frame
		do {
			do {
				// Get new Graphics2D to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, state);
				} finally {
					graphics.dispose();    // Dispose the graphics
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();

			// Force system to draw
			Toolkit.getDefaultToolkit().sync();

		} while (bufferStrategy.contentsLost());
	}

	/**
	 * Rendering all game elements based on the game state.
	 *
	 * @param g2d     Graphics2D context
	 * @param manager GameState object
	 */
	private void doRendering(Graphics2D g2d, GameManager manager) {

		// Draw background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getScreenWidth(), getScreenHeight());

		// Draw background
		g2d.drawImage(manager.getBackground(), (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2, GAME_WIDTH, GAME_HEIGHT, null);

		// Draw game title (Back ground)
		g2d.setColor(Color.BLACK);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(24f));
		g2d.drawString(this.getTitle(), (getScreenWidth() - getStringWidth(g2d)) / 2, 40);

		// Draw separator
		drawSeparator(g2d, (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2 - 30, GAME_WIDTH);

		// Draw obstacles (Mid ground)
		for (Obstacle obstacle : manager.getObstacles()) {
			if (obstacle != null) {

				if (obstacle instanceof Fence)
					((Fence) obstacle).draw(g2d);
				else {
					g2d.setColor(obstacle.getColor());
					g2d.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
				}
			}
		}

		// Draw CarePackages
		for (CarePackage carePackage : manager.getCarePackages()) {
			//if (!carePackage.isUsed())
			if (carePackage.getOpacity() > 0)
				carePackage.draw(g2d);
		}

		// Draw player (Tank & tracks) (Fore ground)
		for (Player player : manager.getPlayers()) {
			player.draw(g2d);

			// Draw AI's debug
//			if (player.getTank() instanceof TankAI)
//				((TankAI)player.getTank()).debug(g2d);
		}

		// Draw explosives (Bomb & missile) (Fore ground)
		for (Explosive explosive : manager.getExplosives()) {
			if (explosive instanceof Bomb) ((Bomb) explosive).draw(g2d);    // Draw Explosive (bomb)
			else if (explosive instanceof Missile) ((Missile) explosive).draw(g2d);    // Draw Explosive (Missile)
			else if (explosive instanceof BombingRun) ((BombingRun) explosive).draw(g2d);	// Draw Explosive (Bombing Run)
		}

		// Draw explosions (Fore ground)
		for (Explosion explosion : manager.getExplosions()) {
			explosion.draw(g2d);
		}

		// Draw air-support
		for (AirSupport airSupport : manager.getAirSupports()){
			airSupport.draw(g2d);
		}

		// Draw games status - Bottom panel
		manager.drawGameStates(g2d, (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2, GAME_WIDTH, GAME_HEIGHT);
	}

	/**
	 * Draws a thin separator line
	 */
	private void drawSeparator(Graphics2D g2d, int x, int y, int width) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, width, 4);
	}

	/**
	 * Calculates the string width in pixels
	 *
	 * @param g2d Graphics2D
	 * @return Width
	 */
	private int getStringWidth(Graphics2D g2d) {
		return g2d.getFontMetrics().stringWidth("Tank Trouble");
	}

	/**
	 * Get screen width
	 *
	 * @return width
	 */
	private int getScreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	/**
	 * Get screen height
	 *
	 * @return height
	 */
	private int getScreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
}