package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.io.File;

public class Map {

    private Position dronePosition = new Position(1, 1);
    
    private HashMap<String, Position> creekCoordinates;
    private HashMap<String, Position> siteCoordinates;

    

    public Map(){
        creekCoordinates = new HashMap<>();
        siteCoordinates = new HashMap<>();
    }

    public void addCreek(String creekID, Position dronePosition){
        
        creekCoordinates.put(creekID, dronePosition); //i.e "creeks" : 5,5 example of what would be storied in hashmap
        
    }

    public void addSite(String siteID, Position dronePosition){
        siteCoordinates.put(siteID, dronePosition);
    }

    //below 2 function help to visualize hashmap. currently has bugs
    public String getCreekCoordinatesAsString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("creek coordinates:\n");
    for (String creekID : creekCoordinates.keySet()) {
        Position position = creekCoordinates.get(creekID);
        stringBuilder.append("CreekID: ").append(creekID).append(", Position: ").append(position.toString()).append("\n");
    }
    return stringBuilder.toString();
}

    public String getSiteCoordinatesAsString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("site coordinates:\n");
    for (String siteID : siteCoordinates.keySet()) {
        Position position = siteCoordinates.get(siteID);
        stringBuilder.append("siteID: ").append(siteID).append(", Position: ").append(position.toString()).append("\n");
    }
    return stringBuilder.toString();
}
    

    public void updateDronePosition (Direction heading) {
        switch (heading) {
            case N:
                dronePosition.y--;
                break;
            case E:
                dronePosition.x++;
                break;
            case S:
                dronePosition.y++;
                break;
            case W:
                dronePosition.x--;
                break;

            
            default:
                break;
        }
    }

    public void updateDronePositionForTurning (String turnDirection, Direction newDirection) {
        switch (turnDirection) {
            case "left":
                switch (newDirection) {
                    case N:
                        dronePosition.y--;
                        dronePosition.x--;
                        break;
                    case E:
                        dronePosition.y--;
                        dronePosition.x++;
                        break;
                    case S:
                        dronePosition.y++;
                        dronePosition.x++;
                        break;
                    case W:
                        dronePosition.y++;
                        dronePosition.x--;
                        break;           
                    default:
                        break;
                }
                break;
            case "right":
                switch (newDirection) {
                    case N:
                        dronePosition.y--;
                        dronePosition.x++;
                        break;
                    case E:
                        dronePosition.y++;
                        dronePosition.x++;
                        break;
                    case S:
                        dronePosition.y++;
                        dronePosition.x--;
                        break; 
                    case W:
                        dronePosition.y--;
                        dronePosition.x--;
                        break;          
                    default:
                        break;
    
        }
    }




}}
