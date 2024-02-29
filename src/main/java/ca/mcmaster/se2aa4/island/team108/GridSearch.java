package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class GridSearch {
    private final Logger logger = LogManager.getLogger();
    public JSONObject nextMove(JSONObject extraInfo, JSONObject prev, Direction heading) {
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
            // When Map is implemented, this can be too. Map (or a class that uses Map) can tell GridSearch
            // which tiles have been searched, so GridSearch will know when it has to turnRight or turnLeft
            output.put("action","stop");
        }



        return output;
    }
}
