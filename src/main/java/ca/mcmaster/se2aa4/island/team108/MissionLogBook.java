package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;

import java.util.List;

public interface MissionLogBook {

    void input(JSONObject obj);
    List<String> getCreeks();
    List<String> getSites();
}
