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
 * Health class
 * <p>This class inherits from the super class 'CarePackage'</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Health extends CarePackage {

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Health(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("joggernog"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@health" + System.currentTimeMillis();
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {

        int extraHealth = tank.getHealth() + 10;

        if (extraHealth > 100)
            extraHealth = 100;

        tank.setHealth(extraHealth);          // Add health (10% of tank's current health)
        setUsed(true);                        // Set package as used, so it cannot be used again
        activateBuff();
    }

    /**
     * Turn's off the package's effect
     */
    @Override
    public void turnOff() {
        setRemove(true);
        deactivateBuff();
    }
}