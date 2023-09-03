package com.company.Obstacles;

/**
 * Imports
 */
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Packages
 */
import com.company.GameFrame;

/**
 * ObstacleGenerator class
 * <p>This class handles the drawing of the map and its obstacles/p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class ObstacleGenerator {

    /**
     * Objects, Variables, Components, ...
     */
    private final static Color wallColor = Color.DARK_GRAY, cornerColor = Color.DARK_GRAY;             // Obstacle colors
    public static String[][] map;                                                                      // Map file matrix
    public static int MAP_ROW_COUNT, MAP_COLUMN_COUNT;                                                 // Maps' dimensions
    public static List<Obstacle> passThroughObstacles = new ArrayList<>();
    public static List<Obstacle> obstacles = new ArrayList<>();

    /**
     * This method generates the visual map from map file
     *
     * @param fileName Map file path
     */
    public static List<Obstacle> generateObstacles(List<Tile> tiles, String fileName) {

        try {
            // Initialize scanners
            Scanner sc = new Scanner(new FileReader(fileName));
            Scanner len = new Scanner(new FileReader(fileName));

            // Count lines
            int lineCount = countLines(fileName) + 1, lineLength = len.next().length();

            // For later access
            MAP_ROW_COUNT = lineCount;
            MAP_COLUMN_COUNT = lineLength;

            // Initialize matrix
            map = new String[lineCount][lineLength];

            for (int i = 0; i < lineCount; i++)
                System.arraycopy(sc.nextLine().split("(?!^)"), 0, map[i], 0, lineLength);

            // Get screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth(), height = screenSize.getHeight();

            // Set game frame dimensions
            GameFrame.GAME_WIDTH = calMapScale(lineLength, 64, 4);
            GameFrame.GAME_HEIGHT = calMapScale(lineCount, 64, 4);

            // Initialize paint properties
            int smallGap = 4, largeScale = 64, smallScale = 4, scale = largeScale + smallScale;
            int xStart = (int) (Math.abs(width - calMapScale(lineLength, largeScale, smallScale)) / 2), yStart = (int) (Math.abs(height - calMapScale(lineCount, largeScale, smallScale)) / 2);

            for (int i = 0; i < lineCount; i++) {
                for (int j = 0; j < lineLength; j++) {
                    int random = new Random().nextInt(100);

                    // Obstacle obj
                    Obstacle obstacle = null;

                    if (i % 2 == 0 && j % 2 == 0)  // Corner
                        obstacle = new Corner((j / 2) * scale + xStart, (i / 2) * scale + yStart, smallScale, smallScale, cornerColor, false);
                    else if (i % 2 == 0) {            // Horizontal wall
                        if (random <= 5 && map[i][j].equals("0")) {
                            obstacles.add(new Fence((j / 2) * scale + smallGap + xStart, (i / 2) * scale + yStart,
                                    largeScale,
                                    smallScale, wallColor, true));
                        }
                        obstacle = new HorizontalWall((j / 2) * scale + smallGap + xStart , (i / 2) * scale + yStart,
                                largeScale, smallScale, wallColor, false);
                    } else if (j % 2 == 0) {          // Vertical wall
                        if (random <= 5 && map[i][j].equals("0")) {
                            obstacles.add(new Fence((j / 2) * scale + xStart, (i / 2) * scale + smallGap + yStart,
                                    smallScale, largeScale, wallColor, true));
                        }
                        obstacle = new VerticalWall((j / 2) * scale + xStart, (i / 2) * scale + smallGap + yStart,
                                smallScale, largeScale, wallColor, false);
                    } else                           // Tiles
                        tiles.add(new Tile(i, j, (j / 2) * scale + smallGap + xStart, (i / 2) * scale + smallGap + yStart, largeScale, largeScale, true));

                    // Add obstacles that are only walls or corners! Nothing more, nothing less
                    if (obstacle != null) {
                        if (!map[i][j].equals("0"))
                            obstacles.add(obstacle);
                        else {
                            obstacle.setPassThrough(true);      // Is pass through
                            passThroughObstacles.add(obstacle);
                        }
                    }
                }
            }

            defineTileDirection(tiles);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return obstacles;
    }

    /**
     * Calculates the dimension length using its map file length
     *
     * @param lineLength Map file length
     * @param largeScale Large scale
     * @param smallScale Small scale
     * @return Dimension length
     */
    public static int calMapScale(int lineLength, int largeScale, int smallScale) {
        // odd * small + even * large
        if (lineLength % 3 == 0 || lineLength % 3 == 2)
            return (int) ((Math.ceil(lineLength / 2) + 1) * smallScale + Math.ceil(lineLength / 2) * largeScale);
        else
            return (int) (Math.ceil(lineLength / 2) * smallScale + Math.ceil(lineLength / 2) * largeScale);
    }

    /**
     * Counts the number of lines in the map file
     *
     * @param filename Map file's path
     * @return Number of lines
     * @throws IOException Handled IOException
     */
    public static int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1)
                return 0;   // bail out if nothing to read

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024; )
                    if (c[i++] == '\n')
                        count++;

                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                for (int i = 0; i < readChars; ++i)
                    if (c[i] == '\n')
                        count++;
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        }
    }

    /**
     * Returns the map int Integer form
     *
     * @return Game map
     */
    public static int[][] getMap() {
        int[][] intMap = new int[map.length][map[0].length];

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                intMap[i][j] = Integer.parseInt(map[i][j]);

        return intMap;
    }

    /**
     * Defines the fist direction in which the tile is opened to
     * 0 = blocked, -1 = not_a_tile, 1 = up, 2 = right, 3 = down, 4 = left
     *
     * @param tiles List of tiles
     */
    private static void defineTileDirection(List<Tile> tiles) {

        int counter = 0;
        for (int i = 0; i < MAP_ROW_COUNT; i++) {
            for (int j = 0; j < MAP_COLUMN_COUNT; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    int dir = -1;
                    boolean up = map[i - 1][j].equals("1"), right = map[i][j + 1].equals("1"), down = map[i + 1][j].equals("1"), left = map[i][j - 1].equals("1");

                    int trueCount = 0;
                    if (up) trueCount++;
                    if (down) trueCount++;
                    if (left) trueCount++;
                    if (right) trueCount++;

                    // all conditions
                    switch (trueCount) {
                        case 4:
                            dir = 0;
                        case 3:
                            if (up && down && left && !right) dir = 2;
                            if (up && down && right && !left) dir = 4;
                            if (up && right && left && !down) dir = 3;
                            if (down && right && left && !up) dir = 1;
                            break;
                        case 2:
                            if (up && !right && !down && left) dir = 2;
                            if (up && right && !down && !left) dir = 4;
                            if (up && !right && down && !left) dir = 4;
                            if (!up && !right && down && left) dir = 2;
                            if (!up && right && !down && left) dir = 3;
                            if (!up && right && down && !left) dir = 4;
                            break;
                        case 1:
                            if (up && !right && !down && !left) dir = 3;
                            if (!up && !right && down && !left) dir = 1;
                            if (!up && !right && !down && left) dir = 2;
                            if (!up && right && !down && !left) dir = 4;
                            break;
                    }

                    boolean xTop = i - 1 == 0, xBottom = i + 2 == MAP_ROW_COUNT, yLeft = j - 1 == 0, yRight = j + 2 == MAP_COLUMN_COUNT;

                    // border
                    if (xTop && down && right && left) dir = 0;
                    if (xBottom && up && right && left) dir = 0;
                    if (yLeft && up && down && right) dir = 0;
                    if (yRight && up && down && left) dir = 0;

                    // corner
                    if (xTop && yLeft && right && down) dir = 0;
                    if (xTop && yRight && left && down) dir = 0;
                    if (xBottom && yLeft && up && right) dir = 0;
                    if (xBottom && yRight && up && left) dir = 0;

                    tiles.get(counter).setDir(dir);
                    counter++;
                }
            }
        }
    }
}