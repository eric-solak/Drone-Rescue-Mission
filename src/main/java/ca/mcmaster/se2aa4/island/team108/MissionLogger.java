package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MissionLogger implements MissionLogBook {

    private final Logger logger = LogManager.getLogger();
    private final List<String> creekLogBook = new ArrayList<>();
    private final List<String> siteLogBook = new ArrayList<>();

    @Override
    public void input(JSONObject action) {
        logger.info("MissionLogger reached");
        try {
            JSONArray creeksArray = action.getJSONArray("creeks");
            JSONArray sitesArray = action.getJSONArray("sites");

            logger.info("Creeks array {}", creeksArray);
            if (creeksArray != null && !creeksArray.isEmpty()) {
                for (int i = 0; i < creeksArray.length(); i++) {
                    String creekID = creeksArray.getString(i);
                    creekLogBook.add(creekID);
                    logger.info("Logged creek ID: {}", creekID);
                }
            }

            if (sitesArray != null && !sitesArray.isEmpty()) {
                for (int i = 0; i < sitesArray.length(); i++) {
                    String siteID = sitesArray.getString(i);
                    siteLogBook.add(siteID);
                    logger.info("Logged site ID:" + siteID);
                }
            }
        } catch (Exception e) {
            logger.info("Action was not SCAN");
        }
    }

    public List<String> getCreeks() {
        return creekLogBook;
    }

    public List<String> getSites() {
        return siteLogBook;
    }
}

