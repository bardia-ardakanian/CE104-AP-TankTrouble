package com.company.Obstacles;

import java.awt.*;

/**
 * Tile class
 * <p>This class contains information about the tiles on the map</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Tile extends Obstacle {

    /**
     * Objects, Variables, Components, ...
     */
    private final int i, j;               // i & j of the text map
    private int dir;                // The first direction in which the tile is opened to

    /**
     * Object Constructor
     *
     * @param i      i
     * @param j      j
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     */
    public Tile(int i, int j, int x, int y, int width, int height, boolean isPassThrough) {
        super(x, y, width, height, Color.WHITE, isPassThrough);

        this.i = i;
        this.j = j;
    }

    /**
     * Getter
     *
     * @return Y-Axis
     */
    public int getY() {
        return y;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     *
     * @return Width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter
     *
     * @return Height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Direction of the tile to the open side
     *
     * @return Direction index
     */
    public int getDir() {
        return dir;
    }

    /**
     * Direction of the tile to the open side
     *
     * @param dir Direction index
     */
    public void setDir(int dir) {
        this.dir = dir;
    }
}