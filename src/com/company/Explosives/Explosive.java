package com.company.Explosives;

/**
 * Imports
 */

import com.company.Obstacles.Fence;
import com.company.Obstacles.Obstacle;
import com.company.Tank;

import java.awt.*;

/**
 * Explosive class
 * <p>Contains a few other sub-classes which are inherited from this class</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Explosive {

    /**
     * Variables, Objects, Components, ...
     */
    protected String explosiveID;             // Identifier
    protected int x, y;                       // Explosive's current X & Y Axis
    protected int width, height;              // Explosive's width & height
    protected double scale;                   // Explosive's  scale
    protected double rotation;                // Explosive's theta
    protected int thrust;                     // Explosive's speed
    protected int damage;                     // Explosive's damage
    protected int xVel, yVel;                 // Explosive's X & Y velocity
    public boolean isTransferred;             // Indicates whether the obj is async with other players

    private boolean canExplode;               // Indicates whether explosive can cause damage to tank if collided. This is used when first the explosive is
    // Fired and is already collided with the tank. This way it doesn't explode until it completely moves out of tank's turret

    public boolean isDead;                    // Indicates death of the object
    protected long startSecond;               // Start second

    protected Color color;                    // Explosive's color

    /**
     * Object Constructor
     *
     * @param x        Explosive's X-Axis
     * @param y        Explosive's Y-Axis
     * @param rotation Explosive's theta
     * @param scale    Explosive's icon scale
     * @param thrust   Explosive's thrust
     * @param damage   Explosive's damage
     * @param width    Explosive's width
     * @param height   Explosive's height
     * @param color    Explosive's color
     */
    public Explosive(int x, int y, double rotation, int thrust, int damage, double scale, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.thrust = thrust;
        this.damage = damage;
        this.scale = scale;
        this.width = width;
        this.height = height;
        this.color = color;
        this.canExplode = false;
        this.isTransferred = false;

        // Set start time in seconds
        this.startSecond = System.currentTimeMillis() / 1000;

        // Initial velocity
        this.xVel = (int) (Math.cos(this.rotation) * this.thrust);
        this.yVel = (int) (Math.sin(this.rotation) * this.thrust);
    }

    /**
     * Second Object Constructor
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param damage Damage
     * @param scale  Scale
     */
    public Explosive(int x, int y, int damage, double scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.damage = damage;
    }

    /**
     * Getter
     *
     * @return Explosive ID
     */
    public String getExplosiveID() {
        return explosiveID;
    }

    /**
     * Updates the explosive's location (Scalar)
     */
    public void update() {
        // Scalar translation
        this.x += this.xVel;
        this.y += this.yVel;

        // Check if object should die or not (Object dies after 4 seconds)
        if (System.currentTimeMillis() / 1000 - startSecond >= 4)
            isDead = true;
    }

    /**
     * Indicates whether we have a collision or not
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     */
    public void checkCollision(int x, int y, double width, double height, Obstacle obstacle) {

        // Check if we have collision (Native .Shape method)
        if (hasCollision(x, y, width, height)) {

            // If obstacle is a Fence, then it's destructible
            if (obstacle instanceof Fence) {
                if (thrust >= 7)
                    obstacle.setHealth(0);    // Destroy obstacle instantly
                else
                    obstacle.setHealth(obstacle.getHealth() - this.damage);     // Damage the obstacle (ie. fence)

                this.isDead = true;
            } else {
                // Calculate distances to sides (left, right, top, bottom)
                int[] distancesToSides = new int[]{
                        Math.abs(x - this.x),
                        (int) Math.abs(x + width - this.x),
                        Math.abs(y - this.y),
                        (int) Math.abs(y + height - this.y)
                };

                // Get minimum distance
                int minDistance = getMinDistance(distancesToSides);

                // Reverse velocity
                if (distancesToSides[0] == minDistance || distancesToSides[1] == minDistance)
                    this.xVel *= -1;
                else
                    this.yVel *= -1;
            }
        }
    }

    /**
     * Checks collision status
     * <p>Note that the anti-radius value is set to explosive's with & height</p>
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     */
    public boolean hasCollision(int x, int y, double width, double height) {
        return (this.x + this.width >= x && this.x - this.width <= x + width && this.y + this.height >= y && this.y - this.height <= y + height);
    }

    /**
     * Calculates the minimum value in the given array
     *
     * @param distancesToSides Distance to sides array
     * @return Minimum distance
     */
    private int getMinDistance(int[] distancesToSides) {
        int minDistance = distancesToSides[0];
        for (Integer distance : distancesToSides)
            if (distance < minDistance)
                minDistance = distance;

        // Return the minimum value
        return minDistance;
    }

    /**
     * Checks whether tank is hit by any type of explosives or not. If so, a certain amount of damaged is caused and if tank's health is empty, it explodes
     * <p>Once the explosive is out of turret and has no collision to the tank whatsoever, it can explode!</p>
     *
     * @param tank Tank obj
     */
    public void checkHit(Tank tank) {

        // Get tank's properties
        int x = tank.getX(), y = tank.getY();
        int width = (int) (tank.getIcon().getWidth() * tank.getScale()), height = (int) (tank.getIcon().getHeight() * tank.getScale());

        // If the explosive is activated & tank does not have any kind of shield
        if (canExplode && (this.x + this.width > x && this.x < x + width / 2 && this.y + this.height > y && this.y < y + height / 2)) {
            if (!tank.getShield())
                tank.setHealth(tank.getHealth() - this.damage);
            this.isDead = true;     // Set's the current explosive to disposal
        } else if (!canExplode && !(this.x + this.width > x && this.x < x + width / 2 && this.y + this.height > y && this.y < y + height / 2))
            canExplode = true;
    }

    /**
     * Returns true if the object has reached maximum number of reflections or has reached its lifespan
     *
     * @return Reflection count and lifespan indicator
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Getter
     *
     * @return Explosive's X-Axis
     */
    public int getX() {
        return x;
    }

    /**
     * Setter
     *
     * @param dead isDead indicator
     */
    public void setDead(boolean dead) {
        isDead = dead;
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
     * Getter
     *
     * @return Explosive's Y-Axis
     */
    public int getY() {
        return this.y;
    }

    /**
     * Getter
     *
     * @return Rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Getter
     *
     * @return Scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Getter
     *
     * @return Damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Getter
     *
     * @return Thrust
     */
    public int getThrust() {
        return thrust;
    }

    /**
     * Getter
     *
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter
     *
     * @param width Width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Setter
     *
     * @param height Height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}