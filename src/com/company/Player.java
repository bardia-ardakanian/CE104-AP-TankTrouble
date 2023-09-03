package com.company;

/**
 * Imports
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 * Player class
 * <p>This class contains information each player of the game as well as te dedicated game objects to the player</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class Player implements Serializable {

    /**
     * Variables, Objects, Components, ...
     */
    private String name;                    // Player's name
    private final Tank tank;                // Player's tank
    private int deathCount, killCount;      // Player's win/death count indicator
    public boolean isAI;                    // AI indicator
    private float frameIndex = 0;           // Index of the frame, so tracks are added to Even frame indexes
    private String teamID;                  // Player's teamID
    private String playerID;                // Player's ID
    public boolean isDead;                  // Player's control indicator
    public boolean exploded;                // This makes the explosion be drawn only once
    public boolean isME;                    // players self indicated (Other's are across the server)

    /**
     * Object Constructor
     *
     * @param name          Player's name
     * @param tankName      Player's Tanks name
     * @param x             Player's initial X-Axis
     * @param y             Player's initial Y-Axis
     * @param teamID        Player's teamID
     * @param explosiveName Player's explosiveName
     * @param isAI          AI indicator
     */
    Player(String name, String tankName, int x, int y, String teamID, String explosiveName, boolean isAI, String playerID, boolean isME) {
        this.name = name;
        this.deathCount = 0;
        this.killCount = 0;
        this.isAI = isAI;
        this.teamID = teamID;
        this.playerID = playerID;
        this.isDead = false;
        this.exploded = false;
        this.isME = isME;

        // Initialize tank
        tank = isAI ? new TankAI(tankName, x, y, explosiveName) : new Tank(tankName, x, y, explosiveName);
    }

    /**
     * The method updates the tank's properties
     *
     * @param rotation defines the rotation angle
     * @param dir      Direction which the tank is moving towards
     */
    public void move(int rotation, int dir) {

        if (!isDead) {
            // Update tank's location
            tank.move(rotation, dir);

            // Add new track
            if ((frameIndex++) % 2 == 0)
                this.tank.addTrack(new Track(tank.getX(), tank.getY(), tank.getRotation(), tank.getScale()));
        }
    }

    /**
     * Getter
     *
     * @return Name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     *
     * @param teamID Team ID
     */
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    /**
     * Setter
     *
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Passes Graphics2D to tank object
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {
        this.tank.draw(g2d, isDead);
    }

    /**
     * Draws the player's status (ie. tank health, ...)
     */
    public void drawStatus(Graphics2D g2d, int x, int y) {

        AffineTransform at = new AffineTransform();

        // Drawing pre-initializing
        int offset = 12;
        int difY = 10;
        float fontSize = 10.0f;
        g2d.setColor(Color.BLACK);

        // Draw the tank prototype
        at.translate(x, y + 10);                                                                                        // Translate icon
        at.rotate(-Math.PI);                                                                                        // Rotate icon (Upward by default)
        at.scale(tank.getScale() * 1.5, tank.getScale() * 1.5);                                             // Scale icon
        at.translate(-tank.getIcon().getWidth() / (float) 2, -tank.getIcon().getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.drawImage(tank.getIcon(), at, null);                                                               // Draw hall

        // Draw player name
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(fontSize));
        g2d.drawString(name, x + tank.getIcon().getWidth() / 2, y + offset - difY);

        // Draw player's tank health
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(fontSize));
        g2d.drawString("HP: " + tank.getHealth() + "%", x + tank.getIcon().getWidth() / 2, y + (2 * offset) - difY);

        // Draw player kill/death ratio
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(fontSize));
        g2d.drawString("K/D: " + getRatio(), x + tank.getIcon().getWidth() / 2, y + (3 * offset) - difY);
    }

    /**
     * Setter
     *
     * @param playerID Player ID
     */
    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    /**
     * Getter
     *
     * @return Tank
     */
    public Tank getTank() {
        return tank;
    }

    /**
     * Getter
     *
     * @return Player ID
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * Setter
     *
     * @param deathCount Death count
     */
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    /**
     * Setter
     *
     * @param killCount Kill count
     */
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    /**
     * Getter
     *
     * @return Death count
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * Getter
     *
     * @return Player teamID
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * Getter
     *
     * @return Kill count
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * Getter
     *
     * @return Controller status
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Calculates the win/loss ration
     *
     * @return Ratio
     */
    public String getRatio() {
        return (deathCount != 0) ? String.valueOf(killCount / (float) deathCount) : "-";
    }
}