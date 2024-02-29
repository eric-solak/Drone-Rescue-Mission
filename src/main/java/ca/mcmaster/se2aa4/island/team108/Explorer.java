package ca.mcmaster.se2aa4.island.team108;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {



    private final Logger logger = LogManager.getLogger();

    public int counter = 0;
    Direction heading;
    Energy batteryLevel;
    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        heading = Direction.valueOf(direction);
        batteryLevel = new Energy(info.getInt("budget"));
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }


    private final DroneController droneController;

    public Explorer() {
        // Instantiate DroneController
        this.droneController = new DroneController();
    }

    private JSONObject extraInfo = new JSONObject();
    JSONObject prevAction = new JSONObject();
    @Override
    public String takeDecision() {
        JSONObject nextAction = droneController.getNextMove(extraInfo, prevAction, heading);
        prevAction = nextAction;

        JSONObject decision = new JSONObject();

        if (nextAction.has("action")) {
            String action = nextAction.getString("action");
            decision.put("action", action);
        } else {
            decision.put("action", "stop");
        }

        if (nextAction.has("parameters")) {
            // Retrieve the value of "parameters" key
            JSONObject parameters = nextAction.getJSONObject("parameters");
            decision.put("parameters", parameters);
        }

        logger.info("** Decision: {}", decision.toString());

        return decision.toString();
    }


    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
