package com.company.CarePackage;

/**
 * Imports
 */
import java.util.Date;
import java.util.List;

/**
 * Packages
 */
import com.company.Obstacles.Tile;

/**
 * CarePackageGenerator class
 * <p>This class generates the care packages of the game</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class CarePackageGenerator {

    /**
     * Objects, Variables, Components, ...
     */
    private long startPoint;
    private final List<Tile> tiles;

    /**
     * Object Constructor
     *
     * @param tiles List of tiles
     */
    public CarePackageGenerator(List<Tile> tiles) {
        startPoint = new Date().getTime();
        this.tiles = tiles;
    }

    /**
     * Adds a package to the list (Can also be received from server steam, or just random)
     *
     * @param x    X-Axis
     * @param y    Y-Axis
     * @param type Type of the package (ie. ghost, ...)
     */
    public void spawnPerk(int x, int y, String type, List<CarePackage> carePackages) {
        switch (type) {
            case "ghost":
                carePackages.add(new Ghost(x, y));
                break;
            case "health":
                carePackages.add(new Health(x, y));
                break;
            case "shield":
                carePackages.add(new Shield(x, y));
                break;
            case "power":
                carePackages.add(new Power(x, y));
                break;
            case "speed":
                carePackages.add(new Speed(x, y));
                break;
            case "lazer":
                carePackages.add(new Laser(x, y));
                break;
        }

        startPoint = new Date().getTime();
    }

    /**
     * Calculates the on going time of the package execution
     *
     * @return Execution time
     */
    public long timeDif() {
        return Math.abs(startPoint - new Date().getTime());
    }
}