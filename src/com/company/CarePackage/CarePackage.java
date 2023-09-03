package com.company.CarePackage;

/**
 * Imports
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

/**
 * Packages
 */
import com.company.Tank;

/**
 * CarePackage class
 * <p>This class contains information related for all sub-classes objects</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public abstract class CarePackage {

    /**
     * Objects, Variables, Components, ...
     */
    private final int x, y;                             // Care package's X & Y coordinates
    private final int width = 14, height = 14;          // Care package's size
    private boolean used = false;                       // Care package's used status
    private Tank tank;                                  // Tank object the package has effects on
    private long actionStartDate;                       // The time of the initial effect
    private BufferedImage icon;                         // Care package's icon
    private double xScale = 0.3d, yScale = 0.3d;        // Care package's scale
    private boolean remove = false;                     // Indicates if the package is to be removed
    private final int lifeSpan = 40000;                 // Indicates how long a package must exist
    private float opacity = (float) 1.0;                // Just for more advanced visual effect
    private double maxScale = 0.35, minScale = 0.3;     // Max & min scale for beat animation
    protected String carePackageID;                     // Care package's ID
    private int sign;
    public boolean isTransferred;                       // Indicates whether the obj is async with other players

    /**
     * Object Constructor
     */
    public CarePackage(int x, int y) {

        this.x = x;
        this.y = y;
        this.isTransferred = false;

        // Set starting time
        this.actionStartDate = new Date().getTime();
        this.sign = new Random().nextBoolean() ? 1 : -1;
    }

    /**
     * Draws the object
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        AffineTransform at = new AffineTransform();

        at.translate(getX(), getY());
        at.translate(-getIcon().getWidth() * this.xScale / (float) 2, -getIcon().getHeight() * this.yScale / (float) 2);

        // Draw the package
        if (opacity >= 0)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        if (used) {
            opacity -= 0.05;
            at.scale(this.xScale += 0.01, this.yScale += 0.01);
        } else {
            at.scale(this.xScale, this.yScale);
            this.xScale += 0.002 * this.sign;
            this.yScale += 0.002 * this.sign;

            if (this.xScale >= this.maxScale || this.xScale <= this.minScale)
                sign *= -1;
        }

        g2d.drawImage(getIcon(), at, null);
    }

    /**
     * either the tank owns a buff or not
     */
    public void activateBuff() {
        tank.setActivateBuff(true);
    }

    /**
     * either the tank owns a buff or not
     */
    public void deactivateBuff() {
        tank.setActivateBuff(false);
    }

    /**
     * Does the specific task related to the package (ie. power up, ...)
     *
     * @param tank Tank obj
     */
    public abstract void doAction(Tank tank);

    /**
     * Calculates the on going time of the package execution
     *
     * @param start Starting time
     * @return Execution time
     */
    public long timeDif(long start) {
        return Math.abs(start - new Date().getTime());
    }

    /**
     * Turn's off a specific package
     */
    public abstract void turnOff();

    /**
     * Indicates if the the package has been used already
     *
     * @return Used status
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Getter
     *
     * @return Care package's X-Axis
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     *
     * @return Care package's Y-Axis
     */
    public int getY() {
        return y;
    }

    /**
     * Getter
     *
     * @return Care package's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter
     *
     * @return Care package's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter
     *
     * @return Tank obj
     */
    public Tank getTank() {
        return tank;
    }

    /**
     * Getter
     *
     * @return The time of the initial effect
     */
    public long getActionStartDate() {
        return actionStartDate;
    }

    /**
     * Getter
     *
     * @return Care package's icon
     */
    public BufferedImage getIcon() {
        return icon;
    }

    /**
     * Getter
     *
     * @return xScale    X-Axis scale
     */
    public double getxScale() {
        return xScale;
    }

    /**
     * Getter
     *
     * @return Package ID
     */
    public String getCarePackageID() {
        return carePackageID;
    }

    /**
     * Getter
     *
     * @return xScale    Y-Axis scale
     */
    public double getyScale() {
        return yScale;
    }

    /**
     * Removes the package if either it has been fully used or has reached life span
     *
     * @return Removed status
     */
    public boolean isRemove() {
        return remove || timeDif(this.actionStartDate) >= lifeSpan;
    }

    /**
     * Setter
     *
     * @param icon Care package's icon
     */
    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    /**
     * Setter
     *
     * @param remove Remove indicator
     */
    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    /**
     * Setter
     *
     * @param actionStartDate The time of the initial effect
     */
    public void setActionStartDate(long actionStartDate) {
        this.actionStartDate = actionStartDate;
    }

    /**
     * Setter
     *
     * @param used Used status
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Setter
     *
     * @param tank Tank obj
     */
    public void setTank(Tank tank) {
        this.tank = tank;
    }

    /**
     * Getter
     *
     * @return Opacity
     */
    public float getOpacity() {
        return opacity;
    }
}