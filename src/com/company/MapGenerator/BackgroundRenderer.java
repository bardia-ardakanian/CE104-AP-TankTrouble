package com.company.MapGenerator;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * Packages
 */
import com.company.TextureReference;

/**
 * BackgroundRenderer class
 * <p>This class generates the background for the playground of the game</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class BackgroundRenderer {

    /**
     * Variables, Objects, Components, ...
     */
    private static Random random = new Random();

    /**
     * Renders the matrix into an image
     */
    public static BufferedImage render(String mapType, int[][] map, int mapWidth, int mapHeight, int textureWidth, int textureHeight) {

        int yCount = map.length;
        int xCount = map[0].length;

        BufferedImage[][] images = new BufferedImage[yCount][xCount];

        // Get a matrix of tile images
        for (int i = 0; i < yCount; i++)
            for (int j = 0; j < xCount; j++)
                try {
                    images[i][j] = getTexture(mapType);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

        // Create the background
        BufferedImage fullMap = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = fullMap.createGraphics();

        // Draw the textures onto the background
        for (int i = 0; i < yCount; i++)
            for (int j = 0; j < xCount; j++)
                g2d.drawImage(images[i][j], textureHeight * j, textureWidth * i, null);

        g2d.dispose();  // Dispose Graphics

        // Return the created map
        return fullMap;
    }

    /**
     * Returns the appropriate texture to use according to the map type
     *
     * @return BufferedImage of the selected texture
     */
    private static BufferedImage getTexture(String mapType) throws Exception {
        switch (mapType) {
            case "jungle":
                return random.nextInt(2) == 1 ? ImageIO.read(new File(TextureReference.getPath("jungleTile01"))) : ImageIO.read(new File(TextureReference.getPath("jungleTile02")));
            case "city":
                return ImageIO.read(new File(TextureReference.getPath("cityTile")));
            case "mud":
                return ImageIO.read(new File(TextureReference.getPath("mudTile")));
            case "sand":
                return ImageIO.read(new File(TextureReference.getPath("sandTile")));
            default:
                return null;
        }
    }
}