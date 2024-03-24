package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Arrays;

public class MoveDrone implements DroneCommand {
    private final Logger logger = LogManager.getLogger();
    private final AreaMap islandMap;
    private Direction currentHeading;
    protected Position position;

    
    public MoveDrone(AreaMap map) {
        this.islandMap = map;
    }
    
    @Override
    public void setHeading(Direction heading) {
        this.currentHeading = heading;
    }

    @Override
    public Position getPosition(){
        return this.position;
    }

    @Override
    public JSONObject dronefly() {
        JSONObject output = new JSONObject();
        islandMap.updateDronePosition(currentHeading);
        position = islandMap.updateDronePosition(currentHeading);
        logger.info("Updated position: " + Arrays.toString(position.getCoords()));
        return output.put("action", "fly");

    }

    @Override
    public JSONObject droneScan() {
        JSONObject output = new JSONObject();
        return output.put("action", "scan");
    }

    @Override
    public JSONObject droneEcho(Direction heading) throws IllegalStateException {
        JSONObject output = new JSONObject();
        output.put("action", "echo");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", heading);
        output.put("parameters", parameters);
        return output;
    }

    @Override
    public JSONObject droneTurn(Direction newHeading) throws IllegalStateException {
        JSONObject output = new JSONObject();
        output.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", newHeading);
        output.put("parameters", parameters);
        position = islandMap.updateDronePositionForTurning(currentHeading, newHeading);
        currentHeading = newHeading;
        return output;
    }


    @Override
    public JSONObject droneStop() {
        JSONObject output = new JSONObject();
        return output.put("action", "stop");
    }

}

