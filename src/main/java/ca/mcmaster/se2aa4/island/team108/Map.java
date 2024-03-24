package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;

public class Map {

    private final Logger logger = LogManager.getLogger();
    Position position = new Position(0, 0);


    public HashMap<String, int[]> creekMap;
    public HashMap<String, int[]> siteMap;

    

    public Map(){
        creekMap = new HashMap<String, int[]>();
        siteMap = new HashMap<String, int[]>();
    }

    public void addCreek(String creekID, int[] dronePosition){
        logger.info("New creek added: " + Arrays.toString(dronePosition));
        creekMap.put(creekID, dronePosition); //i.e "creeks" : 5,5 example of what would be storied in hashmap

    }

    public void addSite(String siteID, int[] dronePosition){
        logger.info("site added");
        siteMap.put(siteID, dronePosition);
    }
    

    
    //below 2 function help to visualize hashmap. currently has bugs
    public String getCreekCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CreekCoordinates:\n");
        for (java.util.Map.Entry<String, int[]> creek : creekMap.entrySet()) {
            String ID = creek.getKey();
            int[] position = creek.getValue();
            stringBuilder.append("CreekID: ").append(ID).append(", Position: ").append(Arrays.toString(position)).append("\n");
        }
        return stringBuilder.toString();
    }

    public String getSiteCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SiteCoordinates:\n");
        for (java.util.Map.Entry<String, int[]> site : siteMap.entrySet()) {
            String ID = site.getKey();
            int[] position = site.getValue();
            stringBuilder.append("SiteID: ").append(ID).append(", Position: ").append(Arrays.toString(position)).append("\n");
        }
        return stringBuilder.toString();
    }

  


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
        }
        return position;
    }

    public Position updateDronePositionForTurning (Direction current_heading, Direction new_heading) {
       switch (current_heading) {
           case N:
               switch (new_heading) {
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
               switch (new_heading) {
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
               switch (new_heading) {
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
               switch (new_heading) {
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
       }
        return position;
    }
}