package com.company.PathFinding;

/**
 * Imports
 */
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains information about each node
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.3
 */
public class Cell {

    /**
     * f(n) = g(n) + h(n) ,where g(n) is the cost of the path from the start node to n,
     * and h(n) is a heuristic function that estimates the cost of the cheapest path from n to the goal
     */
    public int g, h, f;
    /**
     * Indicates the (x, y) of the cell
     */
    public int i, j;
    /**
     * A list of all 4 neighbors (up, down, left, right) if not abundant.
     */
    public List<Cell> neighbors;
    /**
     * Points to the previous cell, more like a linked list.
     */
    public Cell previous;
    /**
     * Indicates whether cell is considered a wall(abundant) or not.
     */
    public boolean isWall;
    /**
     * Indicates the value of the cell that can be -1 & o
     */
    public int value;

    /**
     * Object constructor
     * <p>Initializes (x, y) coordinates and the Initial value of the current cell</p>
     *
     * @param i     Indicates the x coordinate
     * @param j     Indicates the y coordinates
     * @param value Indicates the Initial value of the cell
     */
    public Cell(int i, int j, int value) {
        this.i = i;
        this.j = j;

        this.value = value; // Set Value
        this.isWall = value == 1;

        // Initialize Neighbors List
        neighbors = new ArrayList<>();
    }

    /**
     * Adds neighbors(up, down, left, right cells) if not abundant and in the bounds
     *
     * @param map Contains the entire map as cells
     */
    public void addNeighbors(Cell[][] map) {
        if (i < map.length - 1 && map[i + 1][j].value == 0)
            this.neighbors.add(map[i + 1][j]);

        if (i > 0 && map[i - 1][j].value == 0)
            this.neighbors.add(map[i - 1][j]);

        if (j < map.length - 1 && map[i][j + 1].value == 0)
            this.neighbors.add(map[i][j + 1]);

        if (j > 0 && map[i][j - 1].value == 0)
            this.neighbors.add(map[i][j - 1]);
    }
}