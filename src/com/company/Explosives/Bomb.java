package com.company.Explosives;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Bomb class
 * <p>Inherits from the Explosive parent.</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Bomb extends Explosive {

    /**
     * Object Constructor
     * <p>For bombs, the maximum number of allowed reflection, is by default set to 4.</p>
     *
     * @param x        X-Axis
     * @param y        Y-Axis
     * @param rotation Bomb's theta
     * @param thrust   Bomb's thrust
     * @param damage   Bomb's damage
     * @param scale    Bomb's icon scale
     * @param width    Bomb's width
     * @param height   Bomb's height
     * @param color    Bomb's color
     */
    public Bomb(int x, int y, double rotation, int thrust, int damage, double scale, int width, int height, Color color) {
        super(x, y, rotation, thrust, damage, scale, width, height, color);
        this.explosiveID = "@bomb" + System.currentTimeMillis();
    }

    /**
     * Draws the explosive object
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        // Update bomb's coordinates
        update();

        AffineTransform at = new AffineTransform();

        // Draw the bomb
        at.translate(this.x, this.y);                                                       // Translate icon
        at.rotate(-Math.PI / 2 + this.rotation);                                      // Rotate icon (Upward by default)
        at.scale(this.scale, this.scale);                                                   // Scale icon
        at.translate(-(this.height) / (float) 2 + 7, -width / (float) 2);            // Translate (Again), for easier rotation around the center
        g2d.setColor(color);                                                                // Set color
        g2d.fillOval((int) at.getTranslateX(), (int) at.getTranslateY(), width, height);    // Draw explosive

        // Draw outline border
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) at.getTranslateX(), (int) at.getTranslateY(), width, height);
    }
}