package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;

import java.util.Objects;

public class FindIsland {

    public JSONObject noLandDetected(JSONObject prev) {
        JSONObject output = new JSONObject();

        try {
            if (prev.has("action")) {
                String prevAction = prev.getString("action");

                if (Objects.equals(prevAction, "fly")) {
                    output.put("action", "scan");
                } else if (Objects.equals(prevAction, "scan")) {
                    output.put("action", "echo");
                    JSONObject parameters = new JSONObject();
                    parameters.put("direction", "N");
                    output.put("parameters", parameters);
                } else if (Objects.equals(prevAction, "echo")){
                    JSONObject parameters = prev.getJSONObject("parameters");
                    String direction = parameters.getString("direction");

                    if (Objects.equals(direction, "N")) {
                        output.put("action", "echo");
                        parameters.put("direction", "S");
                        output.put("parameters", parameters);
                    } else {
                        output.put("action", "fly");
                    }
                }
            } else {
                output.put("action", "fly"); // Default action
            }
        } catch (Exception e) {
            output.put("action", "fly");
        }

        return output;
    }

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
