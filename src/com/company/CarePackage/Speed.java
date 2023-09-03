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
public class Speed extends CarePackage {

    /**
     * Objects, Variables, Components, ...
     */
    private int tankThrust;

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Speed(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("stamina"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@speed" + System.currentTimeMillis();
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {
        tankThrust = tank.getThrust();
        tank.setThrust((int) (tankThrust * 1.5));
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
            getTank().setThrust(tankThrust);
            setRemove(true);
            deactivateBuff();
        }
    }
}