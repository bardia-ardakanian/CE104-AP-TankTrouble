package com.company;

import com.company.Obstacles.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankAI extends Tank {

    /**
     * Objects, Variables, Components, ...
     */
    private final int[][] map;                      // Entire map of the game
    List<Obstacle> obstacles;                       // Pass through objects
    Random random = new Random();

    /**
     * AI variables
     */
    private final int sight = (int)(64 * 0.4);         // AI's sight
    private List<Sensor> sensors;
    private double desiredRotation;
    private final double randomDirWhenForwardDetect;

    /**
     * Object Constructor
     * @param tankName              Tank's name
     * @param x                     Tank's X-Axis
     * @param y                     Tank's Y-Axis
     * @param explosiveName         Tank's explosion name
     */
    public TankAI(String tankName, int x, int y, String explosiveName) {
        super(tankName, x, y, explosiveName);

        this.desiredRotation = 0;
        this.obstacles = new ArrayList<>();

        // Get the entire map of the game
        this.map = ObstacleGenerator.getMap();
        randomDirWhenForwardDetect = 0;

        // Get list of obstacles (Combined)
        this.obstacles = ObstacleGenerator.obstacles;

//        		List<Cell> path = AStar.getShortestPath(17, 1, 5, 24, ObstacleGenerator.map, ObstacleGenerator.map.length, ObstacleGenerator.map[0].length, 1000);
//		for (Cell cell: path)
//			System.out.println(cell.i + ", " + cell.j);
    }

    /**
     * Step 1, calculate five arbitrary points as sensors in the five directions relative to the tank; as followed: Left, Forward-Left, Forward, Forward-Right & Right
     */
    public int[] decide() {

        // Variables
        int dir = 0, rotation = 0;

        // Step 1
        sensors = new ArrayList<>();

        sensors.add(new Sensor(this.x + (int)(Math.cos(this.rotation) * this.sight), this.y + (int)(Math.sin(this.rotation) * this.sight)));                                    // Front sensor
        sensors.add(new Sensor(this.x + (int)(Math.cos(this.rotation - Math.PI / 2) * this.sight * 0.6), this.y + (int)(Math.sin(this.rotation - Math.PI / 2) * this.sight * 0.6)));        // Right sensor
        sensors.add(new Sensor(this.x + (int)(Math.cos(this.rotation + Math.PI / 2) * this.sight * 0.6), this.y + (int)(Math.sin(this.rotation + Math.PI / 2) * this.sight * 0.6)));        // Left sensor
        sensors.add(new Sensor(this.x + (int)(Math.cos(this.rotation + Math.PI / 4) * this.sight * 0.8), this.y + (int)(Math.sin(this.rotation + Math.PI / 4) * this.sight * 0.8)));        // Forward-Right sensor
        sensors.add(new Sensor(this.x + (int)(Math.cos(this.rotation - Math.PI / 4) * this.sight * 0.8), this.y + (int)(Math.sin(this.rotation - Math.PI / 4) * this.sight * 0.8)));        // Forward-Left sensor

        runSensors();

        if (sensors.get(0).pick) {                                      // Obstacle reported ahead!
            if (!sensors.get(1).pick)                                   // Go right
                this.desiredRotation = this.rotation - Math.PI / 4;
            else                                                        // Right blocked?, go left
                this.desiredRotation = this.rotation + Math.PI / 4;
            dir = -1;
        } else {  // Path is clear. Correct rotation error and move on
            dir = 1;

            if (sensors.get(1).pick && sensors.get(3).pick)
                this.desiredRotation = this.rotation + Math.PI / 900;
            else if (sensors.get(2).pick && sensors.get(3).pick)
                this.desiredRotation = this.rotation - Math.PI / 900;
        }

        // Decide rotation
        if (this.desiredRotation < this.rotation)           // Rotate right
            rotation = -1;
        else if (this.desiredRotation > this.rotation)      // Rotate left
            rotation = 1;

        return new int[] {rotation, dir};
    }

    /**
     * Get's list of obstacles in sight (Combined)
     */
    private void runSensors() {
        for (Sensor sensor : sensors)
            for (Obstacle obstacle : obstacles)
                if (!(obstacle instanceof Corner))
                    if (!sensor.pick)       // If not picking anything, search. If not, keep the sensor beep
                        sensor.checkPick(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
    }

    public void debug(Graphics2D g2d) {

        int radius = 5;

        for (Sensor sensor: sensors)
            g2d.fillOval(sensor.x - radius / 2, sensor.y - radius / 2, radius, radius);
    }
}

/**
 * Sensors indicate a collision with game objects
 */
class Sensor {

    /**
     * Objects, Variables, Components, ...
     */
    public int x, y;                    // Sensor's coordinates
    public boolean pick;                // Sensor's obstacle picker indicator
    public double theta;                // Sensor's angle (Radiant)

    /**
     * Object Constructor
     * @param x     X-Axis
     * @param y     Y-Axis
     */
    public Sensor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Determines whether sensor hits the obstacle or not
     *
     * @param x             X-Axis
     * @param y             Y-Axis
     * @param width         Width
     * @param height        Height
     */
    public void checkPick(int x, int y, int width, int height) { this.pick = this.x >= x && this.x <= x + width && this.y >= y && this.y <= y + height; }
}