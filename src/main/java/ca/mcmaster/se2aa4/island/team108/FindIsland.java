package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class FindIsland {
    private final Logger logger = LogManager.getLogger();
    private DroneCommand droneCommand;
    private String prev;
    private enum State {NotFound, CheckEcho, Perpendicular, MoveToIsland};
    private State currentState = State.NotFound;
    private int FirstTurnDirection = 0;
    private Queue<JSONObject> commandQ = new LinkedList<>();
    public FindIsland(Map map) {
    }

    /**
     * Repeat "fly", "scan", and "echo" in both the left and right directions until land is found
     *
     * @param prevAction JSONObject of the previous action
     * @param heading    Current direction the drone is facing
     * @return JSONObject of the next move
     */
    public JSONObject noLandDetected(JSONObject prevAction, Direction heading, DroneCommand droneCommand, JSONObject extraInfo) throws Exception {
        this.droneCommand = droneCommand;

        getPrevAction(prevAction);
        if (currentState == State.NotFound  && commandQ.isEmpty()) {
            flyForward(heading, extraInfo, prevAction);
        } else if (currentState == State.CheckEcho) {
            checkingEcho(heading, extraInfo, prevAction);
        }
        logger.info("Command Queue: " + commandQ.toString());
        return getNextCommand();
    }

    public JSONObject landDetected(JSONObject prevAction, Direction heading, DroneCommand droneCommand, JSONObject extraInfo) throws Exception {
        this.droneCommand = droneCommand;
        getPrevAction(prevAction);
        
        if (currentState == State.Perpendicular){
            if (commandQ.isEmpty()){
                findEdge(heading, extraInfo, prevAction);
            }
        } else if (currentState == State.MoveToIsland) {
            flyToLand(heading, extraInfo, prevAction);
        }
        logger.info("Command Queue: " + commandQ.toString());
        return getNextCommand();
    }

    private void flyToLand(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception{

        if (Objects.equals(prev, "echo")) {
            if (extraInfo.getString("found").equals("GROUND")) {
                int distance_to_shore = extraInfo.getInt("range");
                if (distance_to_shore == 0) {
                    commandQ.add(droneCommand.droneScan());
                } else {
                    for (int i = 0; i < distance_to_shore; i++) {
                        commandQ.add(droneCommand.dronefly());
                    }
                    commandQ.add(droneCommand.droneScan());
                }
            }else{
                logger.info("ISLAND NOT FOUND");
            }
        }

    }

    private void findEdge(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception {

        if (extraInfo.getString("found").equals("GROUND")) {
            JSONObject parameters = prevAction.getJSONObject("parameters");
            Direction echoDirection = (Direction) parameters.get("direction");
            if (echoDirection == heading.turnLeft()) {
                commandQ.add(droneCommand.dronefly());
                commandQ.add(droneCommand.droneEcho(heading.turnLeft()));
            }
        } else { // Found water or out of range
            commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
            commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft()));
            commandQ.add(droneCommand.droneTurn(heading.turnLeft().turnLeft().turnRight()));
            commandQ.add(droneCommand.droneEcho(heading.turnLeft()));
            currentState = State.MoveToIsland;
        }

    }

    private void checkingEcho(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception {
        if (Objects.equals(prev, "echo")) {
            if (extraInfo.getString("found").equals("GROUND")) {
                JSONObject parameters = prevAction.getJSONObject("parameters");
                Direction echoDirection = (Direction) parameters.get("direction");
                commandQ.clear();
                if (echoDirection == heading.turnLeft()) {
                    currentState = State.MoveToIsland;
                    commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
                    setFirstTurn();
                } else if (echoDirection == heading.turnRight()) {
                    currentState = State.MoveToIsland;
                    commandQ.add(droneCommand.droneTurn(heading.turnRight()));
                } else {
                    currentState = State.Perpendicular;
                    commandQ.add(droneCommand.droneTurn(heading.turnRight()));
                    commandQ.add(droneCommand.droneEcho(heading));
                }
            // If we have checked all directions and there is no ground we move forward
            } else if (extraInfo.getString("found").equals("OUT_OF_RANGE") && commandQ.isEmpty()) {
                commandQ.add(droneCommand.dronefly());
                currentState = State.NotFound;
            }
        }
    }

    private void setFirstTurn() {
        FirstTurnDirection += 1;
    }

    public int getFirstTurnDirection(){
        return FirstTurnDirection;
    }
    
    private void getPrevAction(JSONObject prevAction) {
        try {
            if (prevAction.has("action")) {
                prev = prevAction.getString("action");
                logger.info("This is the previous action: " + prevAction);
            } else {
                // Default fly on first move
                commandQ.add(droneCommand.dronefly());
            }
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    private JSONObject getNextCommand() {
        if (!commandQ.isEmpty()) {
            return commandQ.remove();
        }
        // No commands added to Queue, drone stops
        return droneCommand.droneStop();
    }

    private void flyForward(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception{

        // Echo in all directions
        if (Objects.equals(prev, "fly")) {
            commandQ.add(droneCommand.droneEcho(heading));
            commandQ.add(droneCommand.droneEcho(heading.turnRight()));
            commandQ.add(droneCommand.droneEcho(heading.turnLeft()));
            currentState = State.CheckEcho;
        }

    }

}