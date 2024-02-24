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
    /*
    private int flyCount = 0;
    private final int FlyIterations = 25;
    private enum State {Fly, Scan, Echo, Stop, Turn, Fly2, Uturn}
    private State currentState = State.Fly;
    @Override
    public String takeDecision(){
        JSONObject decision = new JSONObject();
        switch (currentState){
            case Fly:
                if (flyCount < FlyIterations){
                    decision.put("action", "fly");
                    flyCount++;
                    if (flyCount==FlyIterations) {
                        currentState = State.Turn;
                    }
                }
                break;
            case Turn:
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "S"));
                currentState = State.Uturn;
                break;
            case Scan:
                decision.put("action", "scan");
                currentState = State.Echo;
                break;
            case Echo:
                decision.put("action","echo");
                decision.put("parameters", new JSONObject().put("direction", "E"));
                currentState = State.Fly2;
                break;
            case Uturn:
                decision.put("action","echo");
                decision.put("parameters", new JSONObject().put("direction", "W"));
                currentState = State.Scan;
                break;
            case Fly2:
                if (flyCount > 0){
                    decision.put("action","fly");
                    flyCount--;
                    if (flyCount == 0){
                        currentState = State.Stop;
                    }
                }
                break;
            case Stop:
                decision.put("action", "stop");
                break;
        }
        logger.info("** Decision: {}", decision.toString());
        return decision.toString();
    }
     */
    private enum State {Scan, Stop}
    private State currentState = State.Scan;

    @Override
    public String takeDecision(){
        JSONObject decision = new JSONObject();
        switch (currentState){
            case Stop:
                decision.put("action", "stop");
                break;
            case Scan:
                decision.put("action", "scan");
                currentState = State.Stop;
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
