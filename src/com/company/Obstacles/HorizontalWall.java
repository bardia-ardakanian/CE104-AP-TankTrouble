package com.company.Obstacles;

/**
 * Imports
 */
import java.awt.*;

/**
 * HorizontalWall class
 * <p>This class contains information about the horizontal walls</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class HorizontalWall extends Obstacle {

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
    public HorizontalWall(int x, int y, int width, int height, Color color, boolean isPassThrough) {
        super(x, y, width, height, color, isPassThrough);
    }
}