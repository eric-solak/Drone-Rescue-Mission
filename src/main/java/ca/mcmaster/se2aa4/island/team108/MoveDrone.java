package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Arrays;

public class MoveDrone implements DroneCommand {
    private final Logger logger = LogManager.getLogger();
    protected Map islandMap;
    private Direction current_heading;
    protected Position position;

    
    public MoveDrone(Map map) {
        this.islandMap = map;
    }
    
    @Override
    public void setHeading(Direction heading) {
        this.current_heading = heading;
    }

    @Override
    public Position getPosition(){
        return this.position;
    }

    @Override
    public JSONObject dronefly() {
        JSONObject output = new JSONObject();
        islandMap.updateDronePosition(current_heading);
        position = islandMap.updateDronePosition(current_heading);
        logger.info("Updated position: " + Arrays.toString(position.getCoords()));
        return output.put("action", "fly");

    }

    @Override
    public JSONObject droneScan() throws Exception {
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
    public JSONObject droneTurn(Direction new_heading) throws IllegalStateException {
        JSONObject output = new JSONObject();
        output.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", new_heading);
        output.put("parameters", parameters);
        position = islandMap.updateDronePositionForTurning(current_heading, new_heading);
        current_heading = new_heading; 
        //logger.info("Updated position: " + Arrays.toString(position.getCoords()));
        return output;
    }


    @Override
    public JSONObject droneStop() {
        JSONObject output = new JSONObject();
        return output.put("action", "stop");
    }

}

