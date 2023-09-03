package com.company.Obstacles;

/**
 * Imports
 */
import com.company.TextureReference;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * HorizontalWall class
 * <p>This class contains information about the horizontal walls</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Fence extends Obstacle {

    /**
     * Variables, Objects, Components, ...
     */
    private BufferedImage icon = null;
    private final double scale = 0.6;

    /**
     * Object Constructor
     *
     * @param x             X-Axis
     * @param y             Y-Axis
     * @param width         Width
     * @param height        Height
     * @param color         Color
     * @param isPassThrough isDestructible indicator
     */
    public Fence(int x, int y, int width, int height, Color color, boolean isPassThrough) {
        super(x, y, width, height, color, isPassThrough);
    }

    /**
     * Draws the fence
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        AffineTransform at = new AffineTransform();

        if (this.width > this.height) {
            // Load Image
            try {
                this.icon = ImageIO.read(new File(TextureReference.getPath("fenceHorizontal")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            at.translate(this.x + (64 - this.icon.getWidth() * this.scale) / 2, this.y - 3);
        } else if (this.width < this.height) {
            // Load Image
            try {
                this.icon = ImageIO.read(new File(TextureReference.getPath("fenceVertical")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            at.translate(this.x - 3, this.y + (64 - this.icon.getHeight() * this.scale) / 2);
        }

        at.scale(this.scale, this.scale);
        g2d.drawImage(icon, at, null);
    }
}