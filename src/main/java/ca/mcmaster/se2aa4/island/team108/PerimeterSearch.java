package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class PerimeterSearch {
    private final Logger logger = LogManager.getLogger();

    /**
     * Calculates the next move during perimeterSearch
     * @param extraInfo JSONObject that contains the results of the previous action
     * @param prev JSONObject of the previous action
     * @param heading Current direction the drone is facing
     * @return JSONObject of the next move
     */
    // TODO: Implement Map. Map (or a class that uses Map) can tell PerimeterSearch
    //  Which tiles have been searched, so PerimeterSearch will know when it has reached the original position of the search
    //  Additionally, Map will be very helpful for traversing around the island
    public JSONObject nextMove(JSONObject extraInfo, JSONObject prev, Direction heading) { // Counterclockwise around beach
        JSONObject output = new JSONObject();

        return output;
    }
}
