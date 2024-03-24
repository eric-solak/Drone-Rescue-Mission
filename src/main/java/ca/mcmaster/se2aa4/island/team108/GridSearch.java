package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Objects;


public class GridSearch {
    private final Logger logger = LogManager.getLogger();
    private final Queue<JSONObject> commandQ = new LinkedList<>();
    private final DroneCommand droneCommand;
    private boolean containsWater;
    private boolean completeUTurn;
    private int isNextTurnLeft = 0;
    private Position position;
    private AreaMap map;
    private final ClosestCreek closestCreek;

    public GridSearch(DroneCommand droneCommand, AreaMap map, ClosestCreek closestCreek) {
        this.droneCommand = droneCommand;
        this.map = map;
        this.closestCreek = closestCreek;
    }

    /**
     * Calculates the next move during gridSearch
     * @param extraInfo JSONObject that contains the results of the previous action.
     * @param prev JSONObject of the previous action
     * @param heading Current direction the drone is facing
     * @param droneCommand Responsible for keeping track of drone movement
     * @param position Current coordinates of the drone
     * @param map Map of all discovered land
     * @return JSONObject of the next move
     */
    public JSONObject nextMove(JSONObject extraInfo, JSONObject prev, Direction heading, DroneCommand droneCommand, Position position, AreaMap map) {
        //this.droneCommand = droneCommand;
        this.map = map;
        this.position = position;
        //this.closestCreek = new ClosestCreek(map.siteMap, map.creekMap);

        // Dequeues and returns the next action from the queue
        logger.info("Current command Queue: " + commandQ);
        if (!commandQ.isEmpty()){
            return commandQ.remove();

            // If the queue is empty we get the next action
        } else{
            try {
                getNextAction(extraInfo, prev, heading);
            } catch (Exception e){
                logger.error("Grid Search Error: " + e.getMessage());
                commandQ.add(droneCommand.droneStop());
            }
        }

        return commandQ.remove();
    }

    public String printClosestCreekID(){
        return closestCreek.findClosestCreek();
    }

    /**
     * Repeat "fly" and "scan" until an edge is found, then turn
     * @param extraInfo JSONObject that contains the results of the previous action.
     * @param prev JSONObject of the previous action
     * @param heading Current direction the drone is facing
     */
    private void getNextAction(JSONObject extraInfo, JSONObject prev, Direction heading) throws Exception {

        String prevAction = prev.getString("action");

        // The previous action dictates what the next action taken will be
        if (Objects.equals(prevAction, "fly")) {
            onFly();
        } else if (Objects.equals(prevAction, "scan")) {
            JSONArray biomeArray = extraInfo.optJSONArray("biomes");
            JSONArray creekArray = extraInfo.optJSONArray("creeks");
            boolean containsCreek = creekArray != null && !creekArray.isEmpty();
            JSONArray siteArray = extraInfo.optJSONArray("sites");
            boolean containsSite = creekArray != null && !siteArray.isEmpty();
            containsWater = biomeArray != null && biomeArray.toList().contains("OCEAN");
            if(containsSite){
                logger.info("Site found");
                for (int i = 0; i < siteArray.length(); i++) {
                    String siteID = siteArray.getString(i);
                    map.addSite(siteID, position.getCoords());
                }
            } else if(containsCreek){
                logger.info("Creek found");
                for (int i = 0; i < creekArray.length(); i++) {
                    String creekID = creekArray.getString(i);
                    map.addCreek(creekID, position.getCoords());
                }
            }
            logger.info("Creek Map: {}", map.getCreekCoordinatesAsString());
            logger.info("Site Map: {}", map.getSiteCoordinatesAsString());
            logger.info("The closest creek found is: {}", printClosestCreekID());
            

            onScan(heading);
        } else if (Objects.equals(prevAction, "heading")) {
            onUTurn(heading);
        } else if (Objects.equals(prevAction, "echo")) {
            onEcho(extraInfo, heading);
        }
    }

    private void onFly() throws Exception {
        commandQ.add(droneCommand.droneScan());
    }

    private void onUTurn(Direction heading) {
        commandQ.add(droneCommand.droneEcho(heading));
    }

    private void leftUTurn(Direction heading) throws Exception {
        //relative to current direction before uturn
        commandQ.add(droneCommand.droneTurn(heading.turnRight()));
        commandQ.add(droneCommand.droneTurn(heading));
        commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));
        completeUTurn = true;

    }

    private void rightUTurn(Direction heading) throws Exception {

        commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
        commandQ.add(droneCommand.droneTurn(heading));
        commandQ.add(droneCommand.droneTurn(heading.turnRight()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight()));
        completeUTurn = true;

    }

    private void onScan(Direction heading) throws Exception{
        if (containsWater){
            commandQ.add(droneCommand.droneEcho(heading));
        } else {
            commandQ.add(droneCommand.dronefly());
        }
    }

    public void getFirstTurn(Integer value){
        isNextTurnLeft += value;
    }

    /**
     * Checks if drone has reached the end of the island (end of search)
     * @param extraInfo JSONObject that contains the results of the previous action
     * @param heading Direction the drone is currently facing
     */
    private void onEcho(JSONObject extraInfo, Direction heading) throws Exception{
        // If we echo forward after a uTurn and there is no land ahead we have finished scanning the island
        if (completeUTurn && "OUT_OF_RANGE".equals(extraInfo.getString("found"))){
            commandQ.add(droneCommand.droneStop());
            return;
        } else if (completeUTurn){
            completeUTurn = false;
        }
        // If we echo and there is land we fly to it and scan
        if ("GROUND".equals(extraInfo.getString("found"))) {
            int distanceToShore = extraInfo.getInt("range");
            if (distanceToShore == 0){
                commandQ.add(droneCommand.droneScan());
                commandQ.add(droneCommand.dronefly());
            } else {
                for (int i = 0; i < distanceToShore; i++) {
                    commandQ.add(droneCommand.dronefly());
                }
                commandQ.add(droneCommand.droneScan());
                commandQ.add(droneCommand.dronefly());
                commandQ.add(droneCommand.droneScan());
            }
            // If we reach the edge of the island we do a uTurn
        } else {
            int distanceToEdge = extraInfo.getInt("range");
            if (isNextTurnLeft%2 == 0) {
                if (distanceToEdge <=2){
                    modifiedLeftUTurn(heading);
                } else {
                    leftUTurn(heading);
                }
            } else {
                if (distanceToEdge <=2){
                    modifiedRightUTurn(heading);
                } else{
                    rightUTurn(heading);
                }

            }
            isNextTurnLeft += 1;
        }
    }

    private void modifiedRightUTurn(Direction heading) throws Exception {
        commandQ.add(droneCommand.droneTurn(heading.turnRight()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight()));
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight().turnRight()));
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight()));
        completeUTurn = true;
    }

    private void modifiedLeftUTurn(Direction heading) throws Exception {
        commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft().turnLeft()));
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));
        completeUTurn = true;
    }
}