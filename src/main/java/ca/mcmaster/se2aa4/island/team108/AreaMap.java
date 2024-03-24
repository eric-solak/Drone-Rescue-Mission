package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class AreaMap {

    private final Logger logger = LogManager.getLogger();
    Position position = new Position(0, 0);


    public Map<String, int[]> creekMap;
    public Map<String, int[]> siteMap;


    public AreaMap() {
        creekMap = new HashMap<>();
        siteMap = new HashMap<>();
    }

    /**
     * Add a creek with its corresponding position (coordinates)
     * @param creekID String of the creek found
     * @param dronePosition Coordinates of the creek position
     */
    public void addCreek(String creekID, int[] dronePosition){
        logger.info("New creek added: " + Arrays.toString(dronePosition));
        creekMap.put(creekID, dronePosition); //i.e "creeks" : 5,5 example of what would be storied in hashmap

    }

    /**
     * Add a site with its corresponding position (coordinates)
     * @param siteID String of the site found
     * @param dronePosition Coordinates of the site position
     */
    public void addSite(String siteID, int[] dronePosition){
        logger.info("site added");
        siteMap.put(siteID, dronePosition);
    }


    /**
     * Returns the coordinates of the creek
     * @return Creek Coordinates (String)
     */
    public String getCreekCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CreekCoordinates:\n");
        for (java.util.Map.Entry<String, int[]> creek : creekMap.entrySet()) {
            String creekID = creek.getKey();
            int[] position = creek.getValue();
            stringBuilder.append("CreekID: ").append(creekID).append(", Position: ").append(Arrays.toString(position)).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Returns the coordinates of the site
     * @return Site Coordinates (String)
     */
    public String getSiteCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SiteCoordinates:\n");
        for (java.util.Map.Entry<String, int[]> site : siteMap.entrySet()) {
            String siteID = site.getKey();
            int[] position = site.getValue();
            stringBuilder.append("SiteID: ").append(siteID).append(", Position: ").append(Arrays.toString(position)).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Updates the drones internal coordinate position when flying
     * @param heading The direction the drone is facing
     * @return New position
     */
    public Position updateDronePosition (Direction heading) {
        switch (heading) {
            case N:
                position.y++;
                break;
            case E:
                position.x++;
                break;
            case S:
                position.y--;
                break;
            case W:
                position.x--;
                break;
            default:
                break;
        }
        return position;
    }

    /**
     * Updates the drones internal coordinate position when turning
     * @param currentHeading Direction before turning
     * @param newHeading Direction after turning
     * @return New position
     */
    public Position updateDronePositionForTurning (Direction currentHeading, Direction newHeading) {
       switch (currentHeading) {
           case N:
               switch (newHeading) {
                   case E:
                       position.y++;
                       position.x++;
                       break;
                   case W:
                       position.y++;
                       position.x--;
                       break;
                default:
                    break;
               }
               break;
           case S:
               switch (newHeading) {
                   case E:
                       position.y--;
                       position.x++;
                       break;
                   case W:
                       position.y--;
                       position.x--;
                       break;
                default:
                    break;
               }
               break;
           case W:
               switch (newHeading) {
                   case N:
                       position.y++;
                       position.x--;
                       break;
                   case S:
                       position.y--;
                       position.x--;
                       break;
                    default:
                        break;
               }
               break;
           case E:
               switch (newHeading) {
                   case N:
                       position.y++;
                       position.x++;
                       break;
                   case S:
                       position.y--;
                       position.x++;
                       break;
                    default:
                        break;
               }
               break;
           default:
               break;
       }
        return position;
    }
}