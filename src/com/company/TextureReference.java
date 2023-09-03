package com.company;

/**
 * Imports
 */
import java.util.HashMap;
import java.util.Map;

/**
 * Texture reference class
 * <p>This class is used to store a hashed map connecting each texture to its corresponding path</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class TextureReference {

    /**
     * This hashmap relates each game texture to its corresponding path for easier access and modification
     */
    private final static Map<String, String> textureMap = new HashMap<>() {
        {
            // Tank textures
            put("tank_black", "Assets\\Textures\\Tanks\\tank_black.png");
            put("tank_blue", "Assets\\Textures\\Tanks\\tank_blue.png");
            put("tank_red", "Assets\\Textures\\Tanks\\tank_red.png");
            put("tank_green", "Assets\\Textures\\Tanks\\tank_green.png");
            put("tank_sand", "Assets\\Textures\\Tanks\\tank_sand.png");

            // Explosive textures
            put("missile_black", "Assets\\Textures\\Explosives\\missile_black.png");
            put("missile_blue", "Assets\\Textures\\Explosives\\missile_blue.png");
            put("missile_green", "Assets\\Textures\\Explosives\\missile_green.png");
            put("missile_red", "Assets\\Textures\\Explosives\\missile_red.png");
            put("missile_sand", "Assets\\Textures\\Explosives\\missile_sand.png");

            // Explosion textures
            put("explosion_smoke", "Assets\\Textures\\Explosions\\explosion_smoke.png");
            put("explosion_fire", "Assets\\Textures\\Explosions\\explosion_fire.png");

            // Backfires
            put("Backfire_small", "Assets\\Textures\\Backfires\\Backfire_small.png");
            put("Backfire_medium", "Assets\\Textures\\Backfires\\Backfire_medium.png");
            put("Backfire_large", "Assets\\Textures\\Backfires\\Backfire_large.png");

            // Background tiles
            put("jungleTile01", "Assets//Textures//Environment//Jungle//jungleTile01.png");
            put("jungleTile02", "Assets//Textures//Environment//Jungle//jungleTile01.png");
            put("mudTile", "Assets//Textures//Environment//Mud//mudTile01.png");
            put("cityTile", "Assets//Textures//Environment//City//cityTile01.png");
            put("sandTile", "Assets//Textures//Environment//Sand//sandTile01.png");

            // Power-ups
            put("joggernog", "Assets\\Textures\\Perks\\jog.png");
            put("stamina", "Assets\\Textures\\Perks\\stamina.png");
            put("deadshot", "Assets\\Textures\\Perks\\deadshot.png");
            put("shield", "Assets\\Textures\\Perks\\phd.png");
            put("laser", "Assets\\Textures\\Perks\\2tap.png");
            put("ghost", "Assets\\Textures\\Perks\\ghost.png");

            // Utilities
            put("tracks", "Assets\\Textures\\Utilities\\tracks.png");
            put("fenceVertical", "Assets\\Textures\\Utilities\\fenceVertical.png");
            put("fenceHorizontal", "Assets\\Textures\\Utilities\\fenceHorizontal.png");
            put("rip", "Assets\\Textures\\Utilities\\rip.png");

            // AirSupport
            put("plane" , "Assets\\Textures\\Support\\Sprites\\Ships\\spaceShips_008.png");
            put("nuke" , "Assets\\Textures\\Support\\Sprites\\Rocket parts\\spaceRocketParts_012.png");
            put("target" , "Assets\\Textures\\Target\\target.png");
        }
    };

    /**
     * This method returns the corresponding path using its Key
     *
     * @param texture Texture name
     * @return Path to file
     * @throws Exception NullPointerException - Texture does not exist!
     */
    public static String getPath(String texture) throws Exception {
        if (textureMap.containsKey(texture))
            return textureMap.get(texture);
        else
            throw new Exception("Texture does not exist!");
    }
}