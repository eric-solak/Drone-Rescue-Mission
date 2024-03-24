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
    private FindIslandState currentState = FindIslandState.NotFound;
    private int FirstTurnDirection = 0;
    private final Queue<JSONObject> commandQ = new LinkedList<>();


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
        logger.info("Current State: " + currentState.toString());
        if (Objects.equals(currentState,FindIslandState.NotFound)  && commandQ.isEmpty()) {
            flyForward(heading);
        } else if (Objects.equals(currentState,FindIslandState.CheckEcho)) {
            checkingEcho(heading, extraInfo, prevAction);
        }
        logger.info("Command Queue: " + commandQ);
        return getNextCommand();
    }

    public JSONObject landDetected(JSONObject prevAction, Direction heading, DroneCommand droneCommand, JSONObject extraInfo) throws Exception {
        this.droneCommand = droneCommand;
        getPrevAction(prevAction);
        
        if (currentState == FindIslandState.Perpendicular){
            if (commandQ.isEmpty()){
                findEdge(heading, extraInfo, prevAction);
            }
        } else if (currentState == FindIslandState.MoveToIsland) {
            flyToLand(extraInfo);
        }
        logger.info("Command Queue: " + commandQ);
        return getNextCommand();
    }

    private void flyToLand(JSONObject extraInfo) throws Exception{

        if (Objects.equals(prev, "echo")) {
            if ("GROUND".equals(extraInfo.getString("found"))) {
                int distanceToShore = extraInfo.getInt("range");
                if (distanceToShore == 0) {
                    commandQ.add(droneCommand.droneScan());
                } else {
                    for (int i = 0; i < distanceToShore; i++) {
                        commandQ.add(droneCommand.dronefly());
                    }
                    commandQ.add(droneCommand.droneScan());
                }
            } else {
                logger.info("ISLAND NOT FOUND");
            }
        }

    }

    private void findEdge(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception {

        if ("GROUND".equals(extraInfo.getString("found"))) {
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
            currentState = FindIslandState.MoveToIsland;
        }

    }

    private void checkingEcho(Direction heading, JSONObject extraInfo, JSONObject prevAction) throws Exception {
        if (Objects.equals(prev, "echo")) {
            if ("GROUND".equals(extraInfo.getString("found"))) {
                JSONObject parameters = prevAction.getJSONObject("parameters");
                Direction echoDirection = (Direction) parameters.get("direction");
                commandQ.clear();
                if (echoDirection == heading.turnLeft()) {
                    currentState = FindIslandState.MoveToIsland;
                    commandQ.add(droneCommand.droneTurn(heading.turnLeft()));
                    setFirstTurn();
                } else if (echoDirection == heading.turnRight()) {
                    currentState = FindIslandState.MoveToIsland;
                    commandQ.add(droneCommand.droneTurn(heading.turnRight()));
                } else {
                    currentState = FindIslandState.Perpendicular;
                    commandQ.add(droneCommand.droneTurn(heading.turnRight()));
                    commandQ.add(droneCommand.droneEcho(heading));
                }
            // If we have checked all directions and there is no ground we move forward
            } else if ("OUT_OF_RANGE".equals(extraInfo.getString("found")) && commandQ.isEmpty()) {
                commandQ.add(droneCommand.dronefly());
                currentState = FindIslandState.NotFound;
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

    public void setCurrentState(FindIslandState newState){
        this.currentState = newState;
    }

    private void flyForward(Direction heading) {

        // Echo in all directions
        if (Objects.equals(prev, "fly")) {
            commandQ.add(droneCommand.droneEcho(heading));
            commandQ.add(droneCommand.droneEcho(heading.turnRight()));
            commandQ.add(droneCommand.droneEcho(heading.turnLeft()));
            currentState = FindIslandState.CheckEcho;
        }

    }

}