package com.company.Explosives;

/**
 * Packages
 */
import com.company.TextureReference;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Missile class
 * <p>Inherits from the Explosive parent. Each missile contains the missile icon as well as backfire and smoke trace as more advanced visual effect</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Missile extends Explosive {

    /**
     * Variables, Objects, Components, ...
     */
    private BufferedImage icon = null;              // Missile's icon
    private BufferedImage backfireIcon = null;      // Missile's backfire icon
    private final List<Smoke> smokes;               // Missile's List of smokes
    private final Random random = new Random();     // Random obj

    /**
     * Object Constructor
     * <p>For missiles, there are not reflection capacity</p>
     *
     * @param x        X-Axis
     * @param y        Y-Axis
     * @param rotation Missile's theta
     * @param thrust   Missile's thrust
     * @param damage   Missile's damage
     * @param scale    Missile's icon scale
     * @param width    Missile's width
     * @param height   Missile's height
     * @param color    Missile's color
     */
    public Missile(int x, int y, double rotation, int thrust, int damage, double scale, int width, int height, Color color) {
        super(x, y, rotation, thrust, damage, scale, width, height, color);
        this.explosiveID = "@missile" + System.currentTimeMillis();

        // Load missile according to tank's color
        String colorName;
        if (Color.RED.equals(color))
            colorName = "red";
        else if (Color.BLUE.equals(color))
            colorName = "blue";
        else if (Color.BLACK.equals(color))
            colorName = "black";
        else if (Color.GREEN.equals(color))
            colorName = "green";
        else
            colorName = "sand";

        this.smokes = new ArrayList<>();

        // Load missile texture
        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath("missile_" + colorName)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Load missile's backfire texture
        try {
            this.backfireIcon = ImageIO.read(new File(TextureReference.getPath("Backfire_medium")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Initial velocity
        this.xVel = (int) (Math.cos(this.rotation) * this.thrust);
        this.yVel = (int) (Math.sin(this.rotation) * this.thrust);
    }

    /**
     * Draws the explosive object
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        // Update object's coordinates
        update();

        AffineTransform at = new AffineTransform();

        // Draw backfire
        Backfire.draw(g2d, this.x, this.y, this.rotation, this.scale, this.backfireIcon);

        // Draw smoke
        smokes.add(new Smoke(this.x, this.y, random.nextInt(10) - 5));      // Add new smoke
        Iterator<Smoke> iterator = smokes.iterator();
        while (iterator.hasNext()) {
            Smoke smoke = iterator.next();

            if (smoke.isVanished())
                iterator.remove();
            else
                smoke.draw(g2d);
        }

        // Draw missile
        at = new AffineTransform();
        at.translate(this.x, this.y);                                                       // Translate icon
        at.rotate(+Math.PI / 2 + this.rotation);                                      // Rotate icon (Upward by default)
        at.scale(this.scale, this.scale);                                                   // Scale icon
        at.translate(-(this.height) / (float) 2 - 5, -width / (float) 2);           // Translate (Again), for easier rotation around the center
        g2d.drawImage(this.icon, at, null);
    }
}


/**
 * Backfire class
 * <p>The backfire coming through the back of the missile</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
class Backfire {

    /**
     * Draws the backfire
     *
     * @param g2d      Graphics 2D
     * @param x        Backfire X-Axis
     * @param y        Backfire Y-Axis
     * @param rotation Backfire Rotation
     * @param scale    Backfire Scale
     * @param icon     Backfire icon
     */
    public static void draw(Graphics2D g2d, int x, int y, double rotation, double scale, BufferedImage icon) {
        AffineTransform at = new AffineTransform();

        at.translate(x, y);                                                                                   // Translate icon
        at.rotate(Math.PI / 2 + rotation);                                                              // Rotate icon (Upward by default)
        at.scale(scale, scale);                                                                               // Scale icon
        at.translate(-icon.getWidth() / (float) 2 + 3, -icon.getHeight() / (float) 2 + 40);           // Translate (Again), for easier rotation around the center
        g2d.drawImage(icon, at, null);                                                                   // Draw hall
    }
}

/**
 * Backfire class
 * <p>The smoke trace of the backfire of the missile (Natural Randomness)</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
class Smoke {

    /**
     * Variables, Objects, Components, ...
     */
    private final int x, y;                         // Smoke's X & Y Axis
    private final int radius;                       // Smoke's radius
    private float opacity = 1;                      // Smoke's Opacity
    private final Color color = Color.LIGHT_GRAY;   // Smoke's Color

    /**
     * Object Constructor
     *
     * @param x      Smoke X-Axis
     * @param y      Smoke Y-Axis
     * @param radius Smoke radius
     */
    public Smoke(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * Draws the object
     *
     * @param g2d Graphics 2D
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);                                                                              // Set color
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));                   // Set opacity (Fade-out motion)
        g2d.drawOval(x, y, radius, radius);                                                               // Draw ellipse
        opacity -= 0.05;
    }

    /**
     * Get visibility status
     *
     * @return Visibility
     */
    public boolean isVanished() {
        return opacity < 0;
    }
}