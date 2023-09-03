package com.company.Explosives;

/**
 * Packages
 */
import com.company.Obstacles.Fence;
import com.company.Obstacles.Obstacle;
import com.company.Tank;
import com.company.TextureReference;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * BombingRun class
 * <p>This class contains the bombs used in the bombing run</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class BombingRun extends Explosive {

    /**
     * Objects, Variables, Components, ...
     */
    private final int radius = 30;              // Radius
    private BufferedImage icon;                 // Icon
    private boolean hit = false;                // Hit status

    /**
     * Object Constructor
     *
     * @param x      Explosive's X-Axis
     * @param y      Explosive's Y-Axis
     * @param damage Explosive's damage
     * @param scale  Explosive's icon scale
     */
    public BombingRun(int x, int y, int damage, double scale) {
        super(x, y, damage, scale);
        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath("nuke")));
        } catch (Exception ex) {
            this.icon = null;
            ex.printStackTrace();
        }

        double width = icon.getWidth() * scale, height = icon.getHeight() * scale;
        setWidth((int) width);
        setHeight((int) height);

        // Set ID
        this.explosiveID = "@bombrun" + System.currentTimeMillis();
    }

    /**
     * Updates the obj
     */
    @Override
    public void update() {
        if (!hit) scale -= 0.05d;
        else scale = 0;
        width = (int) (icon.getWidth() * scale);
        height = (int) (icon.getHeight() * scale);

        if (scale <= 0.1) {
            this.hit = true;
        }
    }

    /**
     * Checks collision
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     * @return Indicator
     */
    @Override
    public boolean hasCollision(int x, int y, double width, double height) {
        return (Math.abs(x - this.x) < radius && Math.abs(y - this.y) < radius) ||
                (Math.abs(x + width - this.x) < radius && Math.abs(y - this.y) < radius) ||
                Math.abs(x - width - this.x) < radius && Math.abs(y - this.y) < radius ||
                Math.abs(x - this.x) < radius && Math.abs(y + height - this.y) < radius ||
                Math.abs(x - this.x) < radius && Math.abs(y - height - this.y) < radius;
    }

    /**
     * Checks whether obj is inside
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     * @param X      X-Axis
     * @param Y      Y-Axis
     * @return Indicator
     */
    private boolean inside(int x, int y, double width, double height, int X, int Y) {
        return (x <= X && X <= x + width) && (y <= Y && Y <= y + height);
    }

    @Override
    public void checkCollision(int x, int y, double width, double height, Obstacle obstacle) {
        if (hasCollision(x , y , width , height) && obstacle instanceof Fence && hit) {
            obstacle.setHealth(obstacle.getHeight() - damage);
        }
        //if (hit) isDead = true;
    }

    @Override
    public void checkHit(Tank tank) {
        if (hasCollision(tank.getX() , tank.getY() ,
                tank.getIcon().getWidth()*tank.getScale() ,
                tank.getIcon().getHeight()*tank.getScale()) && hit && !tank.getShield() /*&& !tank.getGhost()*/){
            tank.setHealth(tank.getHealth() - damage);
        }
        //if (hit) isDead = true;
    }

    /**
     * Draws the bombing run
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        // Update bomb's coordinates
        update();

        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);                                                                     // Translate icon
        at.scale(scale, scale);                                                                           // Scale icon
        at.translate(-this.icon.getWidth() / (float) 2, -this.icon.getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.drawImage(this.icon, at, null);                                                          // Draw hall
    }

    /**
     * Is hit indicator
     *
     * @return Indicator
     */
    public boolean isHit() {
        return hit;
    }
}