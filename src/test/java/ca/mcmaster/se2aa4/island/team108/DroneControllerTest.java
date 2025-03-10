
package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DroneControllerTest {
    private DroneController droneController;
    private AreaMap map;

    @BeforeEach
    void startPoint() {
        map = new AreaMap();
        droneController = new DroneController(map);
    }
    //boundary test case
    @Test
    void getNextMoveWithNoPrevAction() {
        JSONObject prevAction = new JSONObject();
        JSONObject extraInfo = new JSONObject();
        Direction heading = Direction.N;
        JSONObject move = droneController.getNextMove(extraInfo, prevAction, heading, map);
        assertEquals("fly", move.getString("action"));
    }

    //regular test case
    @Test
    void getNextMoveWithPrevActionAsHeading() {
        JSONObject prevAction = new JSONObject("{\"action\":\"heading\"}");
        JSONObject extraInfo = new JSONObject();
        Direction heading = Direction.N;
        JSONObject move = droneController.getNextMove(extraInfo, prevAction, heading, map);
        assertEquals("echo", move.getString("action"));
    }

    //another boundary
    @Test
    void getNextMoveWithNullAsPrevAction() {
        JSONObject prevAction = null;
        JSONObject extraInfo = new JSONObject();
        Direction heading = Direction.N;
        JSONObject move = droneController.getNextMove(extraInfo, prevAction, heading, map);
        assertEquals("stop", move.getString("action"));
    }
}

