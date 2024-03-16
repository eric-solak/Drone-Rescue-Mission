package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.io.File;

public class Map {

    private Position dronePosition = new Position(1, 1);
    
    private HashMap<String, Position> siteCoordinates;

    

    public Map(){
        siteCoordinates = new HashMap<>();
    }

    public void addSite(String siteType, Position dronePosition){
        
        siteCoordinates.put(siteType, dronePosition); //i.e "creeks" : 5,5 example of what would be storied in hashmap
        
    }

    //test function to see whats stored in hashmap
    public void printSiteCoordinates() {
        System.out.println("Site coordinates:");
        for (String siteType : siteCoordinates.keySet()) {
            Position position = siteCoordinates.get(siteType);
            System.out.println("Site Type: " + siteType + ", Position: " + position.toString());
        }
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
