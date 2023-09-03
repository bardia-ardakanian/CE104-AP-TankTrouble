package com.company.Obstacles;

/**
 * Imports
 */

import java.awt.*;

/**
 * Obstacle class
 * <p>This class contains sub-classes that form walls and corners of the map</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Obstacle {

    /**
     * Objects, Variables, Components, ...
     */
    protected final int x, y;                     // Obstacle's X & Y Axis
    protected final int width, height;            // Obstacle's Width & height
    protected Color color;                        // Obstacle's Color
    protected int health;                         // Obstacle's Health (In case it is destructible)
    protected boolean isPassThrough;              // Can be passed through or not

    /**
     * Object Constructor
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     * @param color  Color
     */
    public Obstacle(int x, int y, int width, int height, Color color, boolean isPassThrough) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.health = 50;
        this.isPassThrough = isPassThrough;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter
     *
     * @return Y-Axis
     */
    public int getY() {
        return this.y;
    }

    /**
     * Getter
     *
     * @return Obj width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Getter
     *
     * @return Obj height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter
     *
     * @return Obj color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Setter
     *
     * @param color Obstacle's color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter
     *
     * @return Health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Setter
     *
     * @param health Health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Setter
     *
     * @param passThrough Pass through
     */
    public void setPassThrough(boolean passThrough) {
        isPassThrough = passThrough;
    }

    /**
     * Indicates the obstacle has been destroyed
     *
     * @return Destroyed status
     */
    public boolean isDestroyed() {
        return this.health <= 0 || this.health > 50;
    }
}