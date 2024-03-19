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
    private Queue<JSONObject> commandQ = new LinkedList<>();
    private DroneCommand droneCommand = new MoveDrone();
    private boolean containsWater;
    private boolean completeUTurn;
    private int isNextTurnLeft = 0;
    /**
     * Calculates the next move during gridSearch
     * @param extraInfo JSONObject that contains the results of the previous action.
     * @param prev JSONObject of the previous action
     * @param heading Current direction the drone is facing
     * @return JSONObject of the next move
     */
    // TODO: Implement Map. Map (or a class that uses Map) can tell GridSearch
    //  // which tiles have been searched, so GridSearch will know when it has to turnRight or turnLeft
    
    public JSONObject nextMove(JSONObject extraInfo, JSONObject prev, Direction heading) {

        // Dequeues and returns the next action from the queue
        logger.info("Current command Queue: " + commandQ.toString());
        if (!commandQ.isEmpty()){
            return commandQ.remove();

        // If the queue is empty we get the next action
        } else{
            try {
                getNextAction(extraInfo, prev, heading);
            } catch (Exception e){
                logger.info("Grid Search Error: " + e);
                commandQ.add(droneCommand.droneStop());
            }
        }

        if (!commandQ.isEmpty()) {
            logger.info("Next command in grid search:" + commandQ.peek().toString());
        }

        return commandQ.remove();
    }

    private void getNextAction(JSONObject extraInfo, JSONObject prev, Direction heading) throws Exception {

        String prevAction = prev.getString("action");
        JSONArray biomeArray = extraInfo.optJSONArray("biomes");

        // The previous action dictates what the next action taken will be
        if (Objects.equals(prevAction, "fly")) {
            onFly();
        } else if (Objects.equals(prevAction, "scan")) {
            containsWater = biomeArray != null && biomeArray.toList().contains("OCEAN");
            onScan(heading);
        } else if (Objects.equals(prevAction, "heading")) {
            onUTurn(heading, extraInfo);
        } else if (Objects.equals(prevAction, "echo")) {
            onEcho(extraInfo, prevAction, heading);
        }
    }
    private void onFly() throws Exception {
        commandQ.add(droneCommand.droneScan());
    }

    private void onUTurn(Direction heading, JSONObject extraInfo) throws Exception {
        commandQ.add((droneCommand.droneEcho(heading)));
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
    private void onEcho(JSONObject extraInfo, String prevAction, Direction heading) throws Exception{
        // If we echo forward after a uTurn and there is no land ahead we have finished scanning the island
        logger.info("Uturn boolean:" + completeUTurn);
        if (completeUTurn && extraInfo.getString("found").equals("OUT_OF_RANGE")){
            commandQ.add(droneCommand.droneStop());
            return;
        } else if (completeUTurn){
            completeUTurn = false;
        }
        // If we echo and there is land we fly to it and scan
        if (extraInfo.getString("found").equals("GROUND")) {
            int distance_to_shore = extraInfo.getInt("range");
            if (distance_to_shore == 0){
                commandQ.add(droneCommand.droneScan());
                commandQ.add(droneCommand.dronefly());
            } else {
                for (int i = 0; i < distance_to_shore; i++) {
                    commandQ.add(droneCommand.dronefly());
                }
                commandQ.add(droneCommand.droneScan());
                commandQ.add(droneCommand.dronefly());
                commandQ.add(droneCommand.droneScan());
            }
            // If we reach the edge of the island we do a uTurn
        } else {
            if ((isNextTurnLeft%2 == 0)) {
                leftUTurn(heading);
            } else {
                rightUTurn(heading);
            }
            isNextTurnLeft += 1;
        }
    }
}
