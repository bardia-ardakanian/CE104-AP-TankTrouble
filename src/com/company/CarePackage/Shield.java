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
 * Shield class
 * <p>This class inherits from the super class 'CarePackage'</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Shield extends CarePackage {

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Shield(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("shield"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@shield" + System.currentTimeMillis();
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {
        tank.setShield(true);
        setUsed(true);
        setActionStartDate(new Date().getTime());
        activateBuff();
    }

    /**
     * Turn's of the package execution
     */
    @Override
    public void turnOff() {
        if (timeDif(getActionStartDate()) >= 15000) {
            getTank().setShield(false);
            setRemove(true);
            deactivateBuff();
        }
    }
}