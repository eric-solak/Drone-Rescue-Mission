package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Objects;

public class FindIsland {
    private final Logger logger = LogManager.getLogger();
    private DroneCommand droneCommand;

    public FindIsland(Map map) {
    }


    /**
     * Repeat "fly", "scan", and "echo" in both the left and right directions until land is found
     * @param prev JSONObject of the previous action
     * @param heading Current direction the drone is facing
     * @return JSONObject of the next move
     */
    public JSONObject noLandDetected(JSONObject prev, Direction heading, DroneCommand droneCommand) {
        JSONObject output = new JSONObject();
        this.droneCommand = droneCommand;

        try {
            if (prev.has("action")) {
                String prevAction = prev.getString("action");
                logger.info("This is the prev action: " + prevAction);

                if (Objects.equals(prevAction, "fly")) {
                    output = droneCommand.droneScan();
                } else if (Objects.equals(prevAction, "scan")) {
                    output = droneCommand.droneEcho(heading.turnLeft());
                    /*
                    output.put("action", "echo");
                    JSONObject parameters = new JSONObject();
                    parameters.put("direction", heading.turnLeft());
                    output.put("parameters", parameters);

                     */
                } else if (Objects.equals(prevAction, "echo")){
                    JSONObject parameters = prev.getJSONObject("parameters");
                    Direction direction = (Direction) parameters.get("direction");

                    logger.info("Direction from parameters: " + direction);
                    logger.info("Direction to the left: " + heading.turnLeft());

                    if (direction == heading.turnLeft()) { // First echo iteration
                        logger.info("Condition is true: direction equals heading.turnLeft()");
                        output = droneCommand.droneEcho(heading.turnRight());
                        /*
                        output.put("action", "echo");
                        parameters.put("direction", heading.turnRight());
                        output.put("parameters", parameters);

                         */
                    } else { // Second echo iteration has been called, repeat the cycle and go back to "fly"
                        output = droneCommand.dronefly();
                    }
                }
            } else {
                logger.info("Straight to else statement");
                output = droneCommand.dronefly(); // Default action
            }
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
            output.put("action", "fly");
        }

        // output = droneCommand.droneTurn(heading.turnRight());
        return output;
    }

    /**
     * Called when land is detected...
     * Flies in a straight line until the beach is reached
     * @param prev JSONObject of the previous action
     * @return JSONObject of the next move
     */
    // TODO: This should be modified, as echo returns the distance to the land. It is unnecessary to
    //  It is unnecessary to call scan when the distance to the land is already known
    public JSONObject landDetected(JSONObject prev) {
        JSONObject output = new JSONObject();
        if (prev.has("action")) {
            String prevAction = prev.getString("action");
            if (Objects.equals(prevAction, "fly")) {
                output.put("action", "scan");
            } else {
                output.put("action", "fly");
            }
        }

        return output;
    }

}