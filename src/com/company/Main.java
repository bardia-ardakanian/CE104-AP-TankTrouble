package com.company;

/**
 * Imports
 */
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Driver class
 */
public class Main {

	/**
	 * Objects, Variables, Components, ...
	 */
	private static GameFrame frame;
	private static GameManager manager;

	/**
	 * Driver method
	 *
	 * @param args Args
	 */
	public static void main(String[] args) {
		// Initialize the global thread-pool
		ThreadPool.init();

		frame = new GameFrame("Tank Trouble");
		try {
			manager = new GameManager(2, 0, "map.txt", "jungle", "239.1.2.3", 1234);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Show the game menu ...

		// After the player clicks 'PLAY' ...
		EventQueue.invokeLater(() -> {

			frame.setLocationRelativeTo(null);                            // put frame at center of screen
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.initBufferStrategy();

			// Create and execute the game-loop
			GameLoop game = new GameLoop(frame, manager);
			game.init();
			ThreadPool.execute(game);
		});
	}
}