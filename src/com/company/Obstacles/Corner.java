package com.company.Obstacles;

/**
 * Imports
 */
import java.awt.*;
import java.io.Serializable;

/**
 * Corner class
 * <p>This class contains information about the corners of the map</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Corner extends Obstacle implements Serializable {

    /**
     * Object Constructor
     *
     * @param x              X-Axis
     * @param y              Y-Axis
     * @param width          Width
     * @param height         height
     * @param color          Color
     * @param isDestructible isDestructible indicator
     */
    public Corner(int x, int y, int width, int height, Color color, boolean isDestructible) {
        super(x, y, width, height, color, isDestructible);
    }
}