package com.company;

/**
 * Imports
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadPool class
 * <p>This class contains tht thread pool used for rendering the game</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class ThreadPool {

	/**
	 * Objects, Variables, Components, ...
	 */
	private static ExecutorService executor;

	/**
	 * Initializes a new CachedThreadPool.
	 */
	public static void init() {
		executor = Executors.newCachedThreadPool();
	}

	/**
	 * This method initializes and then executes the executor
	 */
	public static void execute(Runnable runnable) {
		if (executor == null)
			init();
		executor.execute(runnable);
	}
}