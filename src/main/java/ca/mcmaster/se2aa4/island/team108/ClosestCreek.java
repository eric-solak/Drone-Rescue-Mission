package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.rmi.ssl.SslRMIClientSocketFactory;


public class ClosestCreek {
    
    public String findClosestCreek(HashMap<String, int[]> emergencySiteMap, HashMap<String, int[]> creekMap) {
    double minDistance = Double.MAX_VALUE;
    String closestCreekID = "";

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

    private double calculateDistance(int[] siteCoordinates, int[] creekCoordinates){
        int distX = creekCoordinates[0] - siteCoordinates[0];
        int distY = creekCoordinates[1] - siteCoordinates[1];
        double hypotenuse = Math.hypot(distX, distY);
        return hypotenuse;
    }

    
}
