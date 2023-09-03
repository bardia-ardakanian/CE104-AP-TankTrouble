package com.company.CarePackage;

/**
 * Imports
 */
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Packages
 */
import com.company.Tank;
import com.company.Obstacles.Tile;
import com.company.TextureReference;

/**
 * Power class
 * <p>This class inherits from the super class 'CarePackage'</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Power extends CarePackage {

    /**
     * Objects, Variables, Components, ...
     */
    private final int firePower;

    /**
     * Object Constructor
     *
     * @param x X-Axis
     * @param y Y-Axis
     */
    public Power(int x, int y) {
        super(x, y);
        try {
            setIcon(ImageIO.read(new File(TextureReference.getPath("deadshot"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.carePackageID = "@power" + System.currentTimeMillis();

        int max = 3, min = 2;
        firePower = (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * The main action of the package
     *
     * @param tank Tank obj
     */
    @Override
    public void doAction(Tank tank) {
        tank.setFirePowerCoefficient(firePower);
        setUsed(true);
        activateBuff();
    }

    /**
     * Turn off's the package effect
     */
    @Override
    public void turnOff() {
        setRemove(true);
        deactivateBuff();
    }
}