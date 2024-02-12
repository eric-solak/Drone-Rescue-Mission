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

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }
    private int flyCount = 0;
    private final int maxFlyActions = 25; // Maximum number of times to fly forward
    private enum State { FLYING, SCANNING, ECHOING, STOPPING, TURNING }
    private State currentState = State.FLYING;

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();

        switch (currentState) {
            case FLYING:
                if (flyCount < maxFlyActions) {
                    decision.put("action", "fly");
                    flyCount++;
                    if (flyCount == maxFlyActions) {
                        currentState = State.TURNING;
                    }
                }
                break;
            case TURNING:
                decision.put("heading", "S");
                currentState = State.SCANNING;
            case SCANNING:
                decision.put("action", "scan");
                currentState = State.ECHOING;
                break;
            case ECHOING:
                decision.put("action", "echo");
                decision.put("parameters", new JSONObject().put("direction", "S"));
                currentState = State.STOPPING;
                break;
            case STOPPING:
                decision.put("action", "stop");
                break;
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
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
