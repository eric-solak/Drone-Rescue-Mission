package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;


public class GridSearch {
    private final Logger logger = LogManager.getLogger();

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
        logger.info("GRID SEARCH REACHED");
        JSONObject output = new JSONObject();

        String prevAction = prev.getString("action");
        logger.info("This is the prev action: " + prevAction);

        if (Objects.equals(prevAction, "fly")) {
            output.put("action", "scan");
        } else if (Objects.equals(prevAction, "scan")) {
            JSONArray biomesArray = extraInfo.getJSONArray("biomes");
            boolean containsBeach = biomesArray.toList().contains("BEACH");
            if (!containsBeach) {
                output.put("action","fly");
            } else {
                JSONObject direction = new JSONObject();
                direction.put("direction", heading.turnLeft());
                output.put("parameters", direction);
                output.put("action", "heading");
            }
        } else { // prevAction == "heading"

            output.put("action","stop");
        }

        return output;
    }
}
