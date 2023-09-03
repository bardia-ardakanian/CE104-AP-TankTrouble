package com.company.PathFinding;

/**
 * Imports
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class finds the shortest, fastest possible way between the two nodes (If any exist)
 *
 * @author Keivan Ipchi Hagh
 * @version 0.1.4
 */
public class AStar {

    /**
     * Indicates a list that contains cells which have not been searched yet
     */
    private static List<Cell> openSet;
    /**
     * Map of the game, but with different datatype
     */
    private static Cell[][] map;
    /**
     * Indicates a list that contains cells that have been already searched
     */
    private static List<Cell> closedSet;
    /**
     * Indicates a list that contains cells that form a non-diagonal path
     */
    private static List<Cell> path;
    /**
     * Starting and Ending cell for our A* search algorithm
     */
    private static Cell start, end;

    /**
     * Indicates the maximum number of cells to search in order to find the optimal path. This variable poses a potential
     * risk of not being able to find the best possible destination. However, knowing that the best possible destination
     * must be reached by searching fewer number of cells than <i>maxCycleIndex</i>, we are guaranteed that risk is minimum.
     */
    private static int maxCycleIndex;

    /**
     * Get Shortest Path Method
     * <p>Core A* search algorithm</p>
     *
     * @param fromX Indicates the starting point x coordinate
     * @param fromY Indicates the starting point y coordinate
     * @param toX   Indicates the destination x coordinate
     * @param toY   Indicates the destination y coordinate
     * @return The shortest possible list of cells that form a none-diagonal path
     */
    public static List<Cell> getShortestPath(int fromX, int fromY, int toX, int toY, String[][] intMap, int cols, int rows, int maxCycleIndex) {

        map = new Cell[cols][rows];
        for (int i = 0; i < cols; i++)
            for (int j = 0; j < rows; j++)
                map[i][j] = new Cell(i, j, Integer.parseInt(intMap[i][j]));

        // Add Neighbors For Each Cell
        for (int i = 0; i < cols; i++)
            for (int j = 0; j < rows; j++)
                map[i][j].addNeighbors(map);

        // Initialize Sets & Path
        openSet = new ArrayList<>();
        closedSet = new ArrayList<>();

        // Set Starting & Ending Point
        start = map[fromX][fromY];
        end = map[toX][toY];

        // Add Starting Point To OpenSet
        openSet.add(start);

        path = new ArrayList<>();

        int cycleIndex = 0;

        // Search For Some Cycles, Then Give Up And Move On
        while (++cycleIndex < maxCycleIndex) {

            // If Cells Still Exist In OpenSet
            if (openSet.size() > 0) {

                // Find The Cell With The Smallest F() In The OpenSet
                int index = 0;
                for (int i = 0; i < openSet.size(); i++)
                    if (openSet.get(i).f < openSet.get(index).f)
                        index = i;

                // Rename The Selected Cell
                Cell current = openSet.get(index);

                // Check If We Have Reached Destination
                if (current == end) {
                    // BackTrace Path
                    Cell temp = current;    // Copy To Avoid Changes In Primary Cell
                    path.add(end);
                    while (temp.previous != null) {
                        path.add(temp.previous);
                        temp = temp.previous;
                    }
                    break;
                }

                // Move Cell From OpenSet To ClosedSet
                openSet.remove(current);
                closedSet.add(current);

                // Check For Each Neighbor
                for (int i = 0; i < current.neighbors.size(); i++) {

                    Cell neighbor = current.neighbors.get(i);   // Get Selected Neighbor

                    // Check Whether Selected Cell Has Been Checked Before Or Not; Also Check If It's Not A Wall!
                    if (!closedSet.contains(neighbor) && !neighbor.isWall) {
                        int tempG = current.g + 1;  // Increment Is 1, Because We Have Matrix Not Graph

                        // If Selected Cell Is In The OpenSet
                        if (openSet.contains(neighbor)) {
                            if (tempG < neighbor.g)
                                neighbor.g = tempG;
                        } else {
                            neighbor.g = tempG;
                            openSet.add(neighbor);
                        }

                        // Calculate G(), H() And Overall F()
                        neighbor.h = heuristic(neighbor, end);  // Calculate The Heuristic between Neighbor & Ending Point
                        neighbor.f = neighbor.h + neighbor.g;
                        neighbor.previous = current;    // For BackTracing The Path
                    }
                }
            } else   // If OpenSet Is Empty
                break;
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Heuristic Method
     * <p>Calculates the taxi-path value. Note: More formulas can be used here, but this method calculates the
     * covered distance not the size of a straight line between starting and ending points!</p>
     *
     * @param from Indicates the starting point x coordinate
     * @param to   Indicates the starting point y coordinate
     * @return Taxi-Path value
     */
    private static int heuristic(Cell from, Cell to) {
        return Math.abs(from.i - to.i) + Math.abs(from.j - to.j);   // Taxi-Path Distance
    }
}