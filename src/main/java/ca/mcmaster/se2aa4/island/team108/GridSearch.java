package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearch {
    private final Logger logger = LogManager.getLogger();
    private State currentState = State.SCANNING;
    // Simplify U-turn logic by using a flag instead of a counter
    private boolean isNextTurnLeft = true;
    private int counter = 1;
    private enum State {
        FLYING_STRAIGHT,
        PREPARING_FOR_UTURN,
        TURNING_LEFT_FOR_UTURN,
        TURNING_RIGHT_FOR_UTURN,
        SCANNING,
        FLY,
        SCAN_FLY
    }

    public GridSearch() {
    }

    public JSONObject nextMove(JSONObject extraInfo, JSONObject prev, Direction heading) {
        JSONObject output = new JSONObject();
        JSONArray biomesArray = extraInfo.optJSONArray("biomes");
        boolean containsWaterOnly = biomesArray != null && biomesArray.toList().contains("OCEAN") && biomesArray.toList().size() == 1;
        boolean containsWater = biomesArray != null && biomesArray.toList().contains("OCEAN");
        String turnDirection = "";
        if (counter >= 700){
            return output;
        }
            switch (currentState) {
                case SCANNING:

                    if (containsWaterOnly) {
                        currentState = State.PREPARING_FOR_UTURN;
                        output.put("action", "scan");
                    } else {
                        currentState = State.FLYING_STRAIGHT;
                        output.put("action", "fly");
                    }
                    break;

                case FLYING_STRAIGHT:
                    currentState = State.SCANNING;
                    output.put("action", "scan");
                    break;

                case FLY:
                    output.put("action", "scan");
                    currentState = State.SCAN_FLY;
                    break;
                case SCAN_FLY:
                    if (containsWater){
                        output.put("action", "fly");
                        currentState = State.FLY;
                    } else {
                        output.put("action", "scan");
                        currentState = State.FLYING_STRAIGHT;
                    }
                    break;

                case PREPARING_FOR_UTURN:
                    // Perform a U-turn by turning in the designated direction\
                    JSONObject parameters = new JSONObject();
                    //Decide the direction for the U-turn based on the current heading or previous move
                    Direction newDirection = isNextTurnLeft ? heading.turnLeft() : heading.turnRight();
                    parameters.put("direction", newDirection);
                    output.put("parameters", parameters);
                    output.put("action", "heading");
                    if (isNextTurnLeft) {
                        currentState = State.TURNING_LEFT_FOR_UTURN;
                    } else {
                        currentState = State.TURNING_RIGHT_FOR_UTURN;
                    }
                    // Alternate the direction for the next U-turn
                    isNextTurnLeft = !isNextTurnLeft;
                    // After turning, go back to scanning in the new direction

                    break;
                case TURNING_LEFT_FOR_UTURN:
                    // After turning left, initiate another left turn to complete the U-turn
                    // Assume the drone moves one unit during the turn
                    currentState = State.FLY;
                    JSONObject parameterss = new JSONObject();

                    // Determining new direction based on current heading
                    Direction newDirectionn = heading.turnLeft();
                    turnDirection = "left";
                    

                    // Setting parameters for direction change
                    parameterss.put("direction", newDirectionn);
                    output.put("parameters", parameterss);
                    output.put("action", "heading");

                    break;

                case TURNING_RIGHT_FOR_UTURN:
                    currentState = State.FLY;
                    JSONObject parametersss = new JSONObject();

                    // Determining new direction based on current heading
                    Direction newDirectionnn = heading.turnRight();
                    turnDirection = "right";

                    // Setting parameters for direction change
                    parametersss.put("direction", newDirectionnn);
                    output.put("parameters", parametersss);
                    output.put("action", "heading");

                    break;

                default:
                    logger.error("Unhandled state: " + currentState);
                    output.put("action", "stop"); // Fallback action
                    break;
            }

        counter += 1;
        logger.info("GridSearch decision: " + output.toString(2));
        return output;
    }
}
