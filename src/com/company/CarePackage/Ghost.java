package com.company.CarePackage;

/**
 * Imports
 */
import java.io.File;
import java.util.Date;
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
public class Ghost extends CarePackage {

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Ghost(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("ghost"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@ghost" + System.currentTimeMillis();
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {
        tank.setGhost(true);
        setUsed(true);
        setActionStartDate(new Date().getTime());
        activateBuff();
    }

    /**
     * Turn's off the package's effect
     */
    @Override
    public void turnOff() {
        if (timeDif(getActionStartDate()) >= 5000) {
            getTank().setGhost(false);
            setRemove(true);
            deactivateBuff();
        }
    }
}