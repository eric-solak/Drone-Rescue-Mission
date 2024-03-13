package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;

public class MoveDrone implements DroneCommand {
    @Override
    public JSONObject dronefly() throws Exception {
        JSONObject output = new JSONObject();
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
    public JSONObject droneTurn(Direction heading) throws IllegalStateException {
        JSONObject output = new JSONObject();
        output.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", heading);
        output.put("parameters", parameters);
        return output;
    }

    @Override
    public JSONObject droneStop() {
        JSONObject output = new JSONObject();
        return output.put("action", "stop");
    }

}


