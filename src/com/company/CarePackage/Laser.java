package com.company.CarePackage;

/**
 * Imports
 */
import java.io.File;
import java.util.List;

/**
 * Packages
 */
import com.company.Tank;
import com.company.Obstacles.Tile;
import com.company.TextureReference;

import javax.imageio.ImageIO;

/**
 * Laser class
 * <p>This class inherits from the super class 'CarePackage'</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Laser extends CarePackage {

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Laser(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("laser"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@laser" + System.currentTimeMillis();
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {
        tank.setExplosiveSpeedCoefficient(3);
        setUsed(true);
        activateBuff();
    }

    /**
     * Turn's off the package effect
     */
    @Override
    public void turnOff() {
        setRemove(true);
        deactivateBuff();
    }
}