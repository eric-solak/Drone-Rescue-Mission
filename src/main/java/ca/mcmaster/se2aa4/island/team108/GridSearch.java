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

        JSONObject output = new JSONObject();

        JSONArray biomesArray = extraInfo.optJSONArray("biomes");
        boolean containsWaterOnly = biomesArray != null && biomesArray.toList().contains("OCEAN") && biomesArray.toList().size() == 1;
        String prevAction = prev.getString("action");

        logger.info("Current command Queue: " + commandQ.toString());
        if (!commandQ.isEmpty()){
            return commandQ.remove();
        }

        try {
            if (Objects.equals(prevAction, "fly")) {
                commandQ.add(droneCommand.droneScan());
            } else if (Objects.equals(prevAction, "scan")) {
                if (containsWaterOnly){
                    if ((isNextTurnLeft%2 == 0)) {
                        leftUTurn(heading);
                    } else {
                        rightUTurn(heading);
                    }
                    isNextTurnLeft += 1;
                } else {
                    commandQ.add(droneCommand.dronefly());
                }
            } else if (Objects.equals(prevAction, "heading")) {
                commandQ.add(droneCommand.droneScan());
                commandQ.add(droneCommand.droneEcho(heading));
            } else if (Objects.equals(prevAction, "echo")) {
                if (extraInfo.getString("found").equals("GROUND")) {
                    int distance_to_shore = extraInfo.getInt("range");
                    for (int i=0;i<=distance_to_shore;i++){
                        commandQ.add(droneCommand.dronefly());
                    }
                } else {
                    logger.info("End of Island: Grid Search Complete");
                    return droneCommand.droneStop();
                }
            }

        } catch (Exception e){
            logger.info("Grid Search Error: " + e);
            return output.put("action", "stop");
        }
        if (!commandQ.isEmpty()) {
            logger.info("Next command in grid search:" + commandQ.peek().toString());
        }
        return commandQ.remove();

    }
    private void leftUTurn(Direction heading) throws Exception {
        //relative to current direction before uturn
        commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft().turnLeft()));
        commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));

    }
    private void rightUTurn(Direction heading) throws Exception {
        commandQ.add(droneCommand.droneTurn(heading.turnRight()));
        commandQ.add(droneCommand.dronefly());
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight()));
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight().turnRight()));
        commandQ.add(droneCommand.droneTurn(heading.turnRight().turnRight()));

    }
}
