package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;

public class Map {

    private final Logger logger = LogManager.getLogger();
    private Position position = new Position(0, 0);
    
    private HashMap<String, int[]> creekCoordinates;
    private HashMap<String, int[]> siteCoordinates;

    public Map(){
        creekCoordinates = new HashMap<String, int[]>();
        siteCoordinates = new HashMap<String, int[]>();
    }

    public void addCreek(String creekID, int[] dronePosition){
        logger.info("New creek added: " + Arrays.toString(dronePosition));
        creekCoordinates.put(creekID, dronePosition); //i.e "creeks" : 5,5 example of what would be storied in hashmap
        
    }

    public void addSite(String siteID, int[] dronePosition){
        siteCoordinates.put(siteID, dronePosition);
    }

    //below 2 function help to visualize hashmap. currently has bugs
    public String getCreekCoordinatesAsString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("creek coordinates:\n");
    for (java.util.Map.Entry<String, int[]> creek : creekCoordinates.entrySet()) {
        String ID = creek.getKey();
        int[] position = creek.getValue();
        stringBuilder.append("CreekID: ").append(ID).append(", Position: ").append(Arrays.toString(position)).append("\n");
    }
    return stringBuilder.toString();
}

    public String getSiteCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("creek coordinates:\n");
        for (java.util.Map.Entry<String, int[]> site : siteCoordinates.entrySet()) {
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
                }
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
                }
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
                }
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
                }
        }
        return position;
    }
}
