package ca.mcmaster.se2aa4.island.team108;

import java.util.Map;
import java.util.Map.Entry;

public class ClosestCreek {
    
    private final Map<String, int[]> emergencySiteMap;
    private final Map<String, int[]> creekMap;

    public ClosestCreek(Map<String, int[]> emergencySiteMap, Map<String, int[]> creekMap) {
        this.emergencySiteMap = emergencySiteMap;
        this.creekMap = creekMap;
    }

    /**
     * Finds the closest creek out of all creeks found
     * @return The creekID of the closest creek
     */
    public String findClosestCreek() {
        double minDistance = Double.MAX_VALUE;
        String closestCreekID = null;

        for (Entry<String, int[]> siteEntry : emergencySiteMap.entrySet()) {
            int[] siteCoordinates = siteEntry.getValue();

            for (Entry<String, int[]> creekEntry : creekMap.entrySet()) {
                String creekID = creekEntry.getKey();
                int[] creekCoordinates = creekEntry.getValue();

                double distance = calculateDistance(siteCoordinates, creekCoordinates);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestCreekID = creekID;
                }
            }
        }
        return closestCreekID;
    }

    /**
     * Calculates the distance from each creek to the emergency site
     * @param siteCoordinates Coordinates of the site
     * @param creekCoordinates Coordinates of the current creek
     * @return Distance
     */
    private double calculateDistance(int[] siteCoordinates, int[] creekCoordinates){
        int distX = creekCoordinates[0] - siteCoordinates[0];
        int distY = creekCoordinates[1] - siteCoordinates[1];
        return Math.hypot(distX, distY);
    } 
}
