package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissionLoggerTest {

    private MissionLogger missionLogger;

    @BeforeEach
    public void initialize() {
        missionLogger = new MissionLogger();

    }

    @Test
    public void log() {
        JSONObject output = new JSONObject();
        JSONArray array = new JSONArray();

        String pointOfInterest1 = "POI 1";
        String pointOfInterest2 = "POI 2";
        array.put(pointOfInterest1);
        array.put(pointOfInterest2);

        output.put("creeks", array);
        output.put("sites", array);

        List<String> logBook = new ArrayList<>();
        logBook.add("POI 1");
        logBook.add("POI 2");

        missionLogger.input(output);

        assertEquals(logBook, missionLogger.getCreeks());
        assertEquals(logBook, missionLogger.getSites());
    }
}
