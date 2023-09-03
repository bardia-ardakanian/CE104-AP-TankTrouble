package com.company.Explosives;

/**
 * Packages
 */

import com.company.TextureReference;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * Explosion class
 * <p>Visual effect of a simulated explosion with random properties</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Explosion {

    /**
     * Variables, Objects, Components, ...
     */
    private BufferedImage icon;                     // Explosion icon
    private final int x, y;                         // Explosion's current X & Y Axis
    private double scale;                           // Explosion's  scale
    private double rotation;                        // Explosion's theta
    private float opacity;                          // Explosion's opacity
    private final int rotationDir;                  // Explosion's rotation direction (For more randomness)
    private final double fadeOutInterval;           // Explosion's fadeout interval (How fast it vanishes)
    private final double rotationInterval;          // Explosion's rotation interval (How fast it rotates)
    private final double scaleInterval;             // Explosion's scale interval (How fast it scales)
    private final String explosionName;             // Explosion's name

    Random random = new Random();

    /**
     * Object constructor
     *
     * @param x                Explosion's X-Axis
     * @param y                Explosion's Y-Axis
     * @param scale            Explosion's scale
     * @param fadeOutInterval  Explosion's fade out interval
     * @param rotationInterval Explosion's rotation interval
     * @param scaleInterval    Explosion's scale interval
     * @param explosionName    Explosion's name
     */
    public Explosion(int x, int y, double scale, double fadeOutInterval, double rotationInterval, double scaleInterval, String explosionName) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.fadeOutInterval = fadeOutInterval;
        this.rotationInterval = rotationInterval;
        this.scaleInterval = scaleInterval;
        this.rotationDir = (random.nextInt(2) == 0) ? 1 : -1;   // Choose a random rotation direction
        this.explosionName = explosionName;

        // Load explosion icon
        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath(explosionName)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Initial opacity
        this.opacity = 1;
    }

    /**
     * Draws all orders
     *
     * @param g2d Order1
     */
    public void draw(Graphics2D g2d) {

        AffineTransform at = new AffineTransform();

        // Draw the tank
        at.translate(this.x, this.y);                                                                     // Translate icon
        at.scale(this.scale, this.scale);                                                                 // Scale icon
        at.rotate(rotation * rotationDir);                                                          // Rotation effect
        at.translate(-this.icon.getWidth() / (float) 2, -this.icon.getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));                   // Set opacity (Fade-out motion)
        g2d.drawImage(this.icon, at, null);                                                          // Draw hall

        // Reduce opacity, Scale up & rotate to create a effect
        this.opacity -= fadeOutInterval;
        this.rotation += rotationInterval;
        this.scale += scaleInterval;
    }

    /**
     * Gets the opacity status
     *
     * @return Vanished status
     */
    public boolean isVanished() {
        return this.opacity <= 0;
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
     * @return Tank's turretX
     */
    public int getY() {
        return this.y;
    }
}