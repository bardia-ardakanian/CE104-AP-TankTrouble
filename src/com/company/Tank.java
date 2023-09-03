package com.company;

/**
 * Packages
 */
import com.company.Explosives.Bomb;
import com.company.Explosives.Explosive;
import com.company.Explosives.Missile;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Tank class
 * <p>This class draws the tank as well as handling its frame-by-frame updates. Also the sub-classes draw the tracks and the backfire as more advanced visual effects like tracks.</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.3.4
 */
public class Tank {

    /**
     * Variables, Objects, Components, ...
     */
    protected BufferedImage icon;                             // Tank's icon (Hall and Turret attached)
    protected BufferedImage backfireIcon;                     // Tank's backfire icon
    protected final Color color;                              // Tank's color

    protected int thrust = 4;                                 // Tank's thrust power
    protected int x, y;                                       // Tank's X and Y coordinates
    protected int xVel, yVel;                                 // Tank's x & Y velocity
    protected int health;                                     // Tank's health
    protected double rotation;                                // Tanks rotation deg (Radiant)
    protected final double rotationSpeed = Math.PI / 40;      // Tank's rotation speed (Radiant)
    protected String explosiveName;                           // Tank's explosive name
    protected final double scale = 0.5d;                      // Tank's scale
    protected int marched;                                    // Tank's march status (Used when stuck)
    protected final int coolDownSpan = 20;                    // Tank's cool down span after each fire
    protected int coolDown;                                   // Tank's cool down counter
    protected String tankName;                                // Tank's name (Used as indicator for its color)
    protected int firePowerCoefficient;                       // Tank's fire power coefficient
    protected int explosiveSpeedCoefficient;                  // Tank's explosive shot coefficient
    protected List<Track> tracks;                             // Tank's list of tracks

    // Care packages
    protected boolean activateBuff;                           // Tank's active buff status (Power care package)
    protected boolean shield = false;                                 // Tank's shield status (Shield care package)
    protected boolean ghost = false;                                  // Tank's ghost status (Ghost care package) (Ghost
    // through walls and stuff)

    /**
     * Object Constructor
     * <p>Initializes object's primitive fields fore first show</p>
     *
     * @param tankName      Tank's name
     * @param x             Tank's X-Axis
     * @param y             Tank's Y-Axis
     * @param explosiveName Tank's explosive name
     */
    public Tank(String tankName, int x, int y, String explosiveName) {

        // Load tank's icon
        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath(tankName)));
        } catch (Exception ex) {
            this.icon = null;
            ex.printStackTrace();
        }

        // Sets the explosive name and backfire icon proper to the explosive
        setExplosiveName(explosiveName);

        this.tankName = tankName;
        this.x = x;
        this.y = y;
        this.rotation = 0;                              // Default value (Set afterwards according to tank's surroundings)
        this.tracks = new ArrayList<>();
        this.health = 100;                              // Default value (Startup value)
        this.coolDown = 0;
        this.marched = 0;
        this.firePowerCoefficient = 1;                  // Default value (Startup value)
        this.explosiveSpeedCoefficient = 1;             // Default value (Startup value)
        this.activateBuff = false;                      // Default value (Startup value)

        // Set tank color according to its name
        switch (tankName) {
            case "tank_blue":
                this.color = Color.BLUE;
                break;
            case "tank_black":
                this.color = Color.BLACK;
                break;
            case "tank_red":
                this.color = Color.RED;
                break;
            case "tank_green":
                this.color = Color.GREEN;
                break;
            case "tank_sand":
                this.color = new Color(255, 253, 208);
                break;
            default:
                this.color = Color.WHITE;
                break;
        }
    }

    /**
     * Draws the tank, tracks and backfire
     * <p>Note that tracks must be drawn before the tank itself in order to seem like real life</p>
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d, boolean isDead) {

        AffineTransform at = new AffineTransform();

        // Draw tank's track
        for (Track track : tracks)
            track.draw(g2d);

        // Reduce opacity when in ghost mode
        if (this.ghost)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.6));

        // Draw the tank
        at.translate(this.x, this.y);                                                                     // Translate icon
        at.rotate(-Math.PI / 2 + this.rotation);                                                    // Rotate icon (Upward by default)
        at.scale(this.scale, this.scale);                                                                 // Scale icon
        at.translate(-this.icon.getWidth() / (float) 2, -this.icon.getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.drawImage(this.icon, at, null);                                                          // Draw hall

        // Draw tank's backfire
        if (this.coolDown > this.coolDownSpan / 2 && !isDead)
            Backfire.draw(g2d, this.x, this.y, this.rotation, this.scale, backfireIcon);
    }

    /**
     * Updates the tank's location
     *
     * @param rotation Rotation deg
     * @param dir      Direction
     */
    public void move(int rotation, int dir) {

        // Update theta
        this.rotation += (rotation * this.rotationSpeed);

        // Calculate velocity
        this.xVel = (int) (Math.cos(this.rotation) * this.thrust * dir);
        this.yVel = (int) (Math.sin(this.rotation) * this.thrust * dir);

        // If we are set to move, apply velocity to location
        if (dir != 0) {
            this.x += this.xVel;
            this.y += this.yVel;
        }

        // Cool down step
        if (this.coolDown != 0)
            this.coolDown--;   // Timer
    }

    /**
     * Indicates whether tank is moving forward
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     * @return Forward movement status
     */
    public boolean isForward(int x, int y, int width, int height) {
        boolean isForward = false, antiClockwise = this.rotation < 0;

        // (0: left, 1: right, 2: top, 3: bottom)
        int slide = collisionSide(x, y, width, height);

        double PI = Math.PI, rotation = Math.abs(this.rotation % (2 * PI));

        switch (slide) {
            case 0:
                isForward = (rotation > 0 && rotation < PI / 2) || (rotation > 3 * PI / 2 && rotation < 2 * PI);
                break;         // Left
            case 1:
                isForward = rotation > PI / 2 && rotation < 3 * PI / 2;
                break;                                                  // Right
            case 2:
                isForward = antiClockwise ? rotation >= PI && rotation <= 2 * PI : rotation >= 0 && rotation <= PI;
                break;       // Top
            case 3:
                isForward = antiClockwise ? rotation >= 0 && rotation <= PI : rotation >= PI && rotation <= 2 * PI;
                break;      // Down
        }

        return isForward;       // Forward status
    }

    /**
     * Determines whether we have a collision or not
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     * @return Collision status
     */
    public boolean hasCollision(int x, int y, int width, int height) {
        boolean collision = false;

        if (this.x >= x && this.x <= x + width) {
            if (this.y <= y + height + this.icon.getHeight() * scale / 2 && this.y >= y + height)
                collision = true;
            else if (this.y >= y - this.icon.getHeight() * scale / 2 && this.y <= y)
                collision = true;
        } else if (this.y >= y && this.y <= y + height) {
            if (this.x >= x + width && this.x <= x + width + this.icon.getWidth() * scale / 2)
                collision = true;
            else if (this.x >= x - this.icon.getWidth() * scale / 2 && this.x <= x)
                collision = true;
        }

        return collision;   // Return collision status
    }

    /**
     * Indicates whether we have a collision or not
     *
     * @param x      X-Axis
     * @param y      Y-Axis
     * @param width  Width
     * @param height Height
     */
    public int collisionSide(int x, int y, int width, int height) {

        // Calculate distances to sides (left, right, top, bottom)
        int[] distancesToSides = new int[]{
                Math.abs(x - this.x),
                Math.abs(x + width - this.x),
                Math.abs(y - this.y),
                Math.abs(y + height - this.y)
        };

        // Get minimum distance
        int minDistance = getMinDistance(distancesToSides);

        // Get the collision side index (0: left, 1: right, 2: top, 3: bottom)
        for (int i = 0; i < distancesToSides.length; i++)
            if (distancesToSides[i] == minDistance)
                return i;

        return -1;  // Never happens, just for the sake of compiler
    }

    /**
     * Calculates the minimum value in the given array
     *
     * @param distancesToSides Distance to sides array
     * @return Minimum distance
     */
    private int getMinDistance(int[] distancesToSides) {
        int minDistance = distancesToSides[0];
        for (Integer distance : distancesToSides)
            if (distance < minDistance)
                minDistance = distance;

        return minDistance;
    }

    /**
     * Returns an instance of explosive to be added to the explosives list
     * <p>Note that the default explosive is set to 'bomb'; meaning explosive type (ie. explosiveName) must be set inorder to change tank's explosive!</p>
     *
     * @return Explosive instance
     */
    public Explosive fire() {

        // Start cool down
        startCoolDown();

        // Move just a little bit further the turret
        int xTemp = (int) (Math.cos(this.rotation) * (this.icon.getWidth() * this.scale / 2)) + this.x;
        int yTemp = (int) (Math.sin(this.rotation) * (this.icon.getHeight() * this.scale / 2)) + this.y;

        int firePowerCoefficient = this.firePowerCoefficient, explosiveSpeedCoefficient = this.explosiveSpeedCoefficient;
        this.firePowerCoefficient = 1;              // reset the fire power after one shot
        this.explosiveSpeedCoefficient = 1;         // reset the explosive speed after one shot

        // Create and return new explosive depending the tank's current explosive type
        switch (explosiveName) {
            case "bomb":
                return new Bomb(xTemp, yTemp, this.rotation, 6 * explosiveSpeedCoefficient, 15 * firePowerCoefficient, this.scale, 5, 5, this.color);
            case "missile":
                return new Missile(xTemp, yTemp, this.rotation, 7 * explosiveSpeedCoefficient, 25 * firePowerCoefficient, this.scale, 5, 5, this.color);
        }

        // Never happens, just for the sake of compiler
        return null;
    }

    /**
     * Checks for tank collision
     *
     * @param player Player obj
     * @return Collision status
     */
    public boolean checkTankCollision(Player player) {

        if (player.isDead)
            return false;

        double tankWidth = this.icon.getWidth() * scale,
                tankHeight = this.icon.getHeight() * scale,
                playerWidth = player.getTank().icon.getWidth() * player.getTank().getScale(),
                playerHeight = player.getTank().icon.getHeight() * player.getTank().getScale(),
                tankCenterX = this.x + tankWidth / 2,
                tankCenterY = this.y + tankHeight / 2,
                playerCenterX = player.getTank().getX() + playerWidth / 2,
                playerCenterY = player.getTank().getY() + playerHeight / 2;

        return Math.abs(tankCenterX - playerCenterX) <= Math.abs(tankWidth + playerWidth) / 2 && Math.abs(tankCenterY - playerCenterY) <= Math.abs(tankHeight + playerHeight) / 2;
    }

    /**
     * Pushes the tank to the a side
     *
     * @param tank Tank obj
     */
    public void pushTank(Tank tank) {

        // Get collision side
        int dim = 20, collisionSide = collisionSide(tank.getX() - dim, tank.getY() - dim,
                (int) (this.icon.getWidth() * tank.getScale() + 2 * dim),
                (int) (this.icon.getHeight() * tank.getScale() + 2 * dim));
        double push = 0.1;

        // Repel
        switch (collisionSide) {
            case 0:
                this.x -= push;
                break;
            case 1:
                this.x += push;
                break;
            case 2:
                this.y -= push;
                break;
            case 3:
                this.y += push;
                break;
        }
    }

    /**
     * Get Buff status
     */
    public boolean isActivateBuff() {
        return activateBuff;
    }

    /**
     * activate and deactivate buff
     *
     * @param activateBuff Buff
     */
    public void setActivateBuff(boolean activateBuff) {
        this.activateBuff = activateBuff;
    }

    /**
     * Setter
     *
     * @param x X-Axis
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter
     *
     * @param icon Tank icon
     */
    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    /**
     * Setter
     *
     * @param y Y-Axis
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter
     *
     * @param health Health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Setter
     *
     * @param rotation Rotation
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * Indicates whether tank can fire or not
     *
     * @return Fire status
     */
    public boolean canFire() {
        return this.coolDown <= 0;
    }

    /**
     * Resets the cool down interval
     */
    public void startCoolDown() {
        this.coolDown = this.coolDownSpan;
    }

    /**
     * Getter
     *
     * @return Tank icon's scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Setter
     *
     * @param thrust Tank's thrust power
     */
    public void setThrust(int thrust) {
        this.thrust = thrust;
    }

    /**
     * Getter
     *
     * @return March status
     */
    public int getMarched() {
        return marched;
    }

    /**
     * Setter
     *
     * @param marched March status
     */
    public void setMarched(int marched) {
        this.marched = marched;
    }

    /**
     * Setter
     *
     * @param explosiveName Back fire name
     */
    public void setExplosiveName(String explosiveName) {

        // Load the proper backfire icon using the explosive name
        try {
            this.explosiveName = explosiveName;

            switch (explosiveName) {
                case "bomb":
                    this.backfireIcon = ImageIO.read(new File(TextureReference.getPath("Backfire_small")));
                    break;
                case "missile":
                    this.backfireIcon = ImageIO.read(new File(TextureReference.getPath("Backfire_medium")));
                    break;
            }
        } catch (Exception ex) {
            this.backfireIcon = null;
            ex.printStackTrace();
        }
    }


    /**
     * Getter
     *
     * @return Health
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Getter
     *
     * @return Tank name
     */
    public String getTankName() {
        return tankName;
    }

    /**
     * Setter
     *
     * @param tankName Tank name
     */
    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    /**
     * Getter
     *
     * @return Tank's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter
     *
     * @return Tank's turretX
     */
    public int getY() {
        return this.y;
    }

    /**
     * Setter
     *
     * @param shield Tank's shield indicator
     */
    public void setShield(boolean shield) {
        this.shield = shield;
    }

    /**
     * Setter
     *
     * @return Ghost effect
     */
    public boolean getGhost() {
        return !this.ghost;
    }

    /**
     * Getter
     *
     * @return Explosive's name
     */
    public String getExplosiveName() {
        return explosiveName;
    }

    /**
     * Getter
     *
     * @return Shield status
     */
    public boolean getShield() {
        return this.shield;
    }

    /**
     * Setter
     *
     * @param ghost Ghost effect
     */
    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    /**
     * Setter
     *
     * @param setExplosiveSpeedCoefficient Explosion's speed coefficient
     */
    public void setExplosiveSpeedCoefficient(int setExplosiveSpeedCoefficient) {
        this.explosiveSpeedCoefficient = setExplosiveSpeedCoefficient;
    }

    /**
     * Setter
     *
     * @param firePowerCoefficient Fire power coefficient
     */
    public void setFirePowerCoefficient(int firePowerCoefficient) {
        this.firePowerCoefficient = firePowerCoefficient;
    }

    /**
     * Getter
     *
     * @return Tank's hall icon
     */
    public BufferedImage getIcon() {
        return this.icon;
    }

    /**
     * Getter
     *
     * @return Rotation Deg
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Adds a new track to the tracks list
     *
     * @param track Track obj
     */
    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    /**
     * Getter
     *
     * @return Tank's thrust power
     */
    public int getThrust() {
        return this.thrust;
    }
}

/**
 * Track class
 * <p>this class contains the traces of tank's movement for more advanced visual effects</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
class Track {

    /**
     * Variables, Objects, Components, ...
     */
    private final int x, y;                     // Track's X & Y Axis
    private BufferedImage icon;                 // Track's icon
    private int lifeSpan;                       // Track's life span
    private final double rotation;              // Track's rotation deg
    private final double scale;                 // Track's icon scale
    private final int maxLifeSpan = 15;         // Track's maximum life span
    private final int lifeSpanInterval = 1;     // Track's life span interval

    /**
     * Object Constructor
     *
     * @param x        Track's X-Axis
     * @param y        Track's Y-Axis
     * @param rotation Track's Rotation
     * @param scale    Track's scale
     */
    public Track(int x, int y, double rotation, double scale) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scale = scale;
        this.lifeSpan = 0;

        // Load icon
        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath("tracks")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Draws the track
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {

        AffineTransform at = new AffineTransform();

        at.translate(x, y);                                                                     // Translate icon
        at.rotate(-Math.PI / 2 + rotation);                                               // Rotate icon (Upward by default)
        at.scale(scale, scale);                                                                 // Scale icon
        at.translate(-icon.getWidth() / (float) 2, -icon.getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.drawImage(icon, at, null);                                                     // Draw track

        // Reduce lifespan
        this.lifeSpan += lifeSpanInterval;
    }

    /**
     * Indicates whether tracks are visible or not
     *
     * @return Visibility status
     */
    public boolean isVanished() {
        return this.lifeSpan >= this.maxLifeSpan;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter
     *
     * @return Y-Axis
     */
    public int getY() {
        return this.y;
    }
}

/**
 * Backfire class
 * <p>The backfire coming through the turret of the tank when fired an explosive</p>
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

        at.translate(x, y);                                                                             // Translate icon
        at.rotate(Math.PI / 2 + rotation);                                                        // Rotate icon (Upward by default)
        at.scale(scale, scale);                                                                         // Scale icon
        at.translate(-icon.getWidth() / (float) 2, -icon.getHeight() / (float) 2 - 30);          // Translate (Again), for easier rotation around the center
        g2d.drawImage(icon, at, null);                                                             // Draw hall
    }
}