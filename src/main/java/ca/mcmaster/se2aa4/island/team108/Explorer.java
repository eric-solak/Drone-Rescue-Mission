package ca.mcmaster.se2aa4.island.team108;

import java.io.StringReader;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private final DroneController droneController;
    private final MissionLogger missionLogger;
    private final Map map;
    Direction heading;
    Energy batteryLevel;

    public Explorer() {
        // Instantiate DroneController
        this.map = new Map();
        this.droneController = new DroneController(map);
        this.missionLogger = new MissionLogger();
    }

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

    private JSONObject extraInfo = new JSONObject();
    JSONObject prevAction = new JSONObject();
    JSONObject prevPrevAction = new JSONObject();

    @Override
    public String takeDecision() {

        JSONObject nextAction = droneController.getNextMove(extraInfo, prevAction, heading);
        prevPrevAction = prevAction;
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

            // Update the direction if drone turned left or right
            String action = decision.getString("action");
            if (parameters.has("direction") && action.equals("heading")) {
                heading = (Direction) parameters.get("direction");
                logger.info("Direction changed to: " + heading);
            }
        }

        logger.info("** Decision: {}", decision.toString());
        return decision.toString();
    }


    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));

        Energy cost = new Energy(response.getInt("cost"));
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
        batteryLevel = batteryLevel.subtract(cost);
        logger.info("The current battery level is now {}", batteryLevel);

        missionLogger.input(extraInfo);
        if (response.has("action")) {
            if (response.get("action") == "stop") {
                deliverFinalReport();
            }
        }
    }

    @Override
    public String deliverFinalReport() {
        
        logger.info("Final Report Reached");
        String creeks = missionLogger.getCreeks().toString();
        String sites = missionLogger.getSites().toString();
        logger.info("Creeks {}", creeks);
        logger.info("Sites {}", sites);

        String creekMapAsString = map.getCreekCoordinatesAsString();
        logger.info("CreekID: Position",creekMapAsString);
        String siteMapAString = map.getCreekCoordinatesAsString();
        logger.info("SiteID: Position", siteMapAString);
        logger.info("-----");
        return ("Creeks found: " + creekMapAsString + "\nSites found: " + siteMapAString);
    }

}
