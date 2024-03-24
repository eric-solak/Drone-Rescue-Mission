
package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class DroneControllerTest {
    private DroneController droneController;
    private AreaMap map;
    private DroneCommand droneCommand; // This needs to be instantiated
    private FindIsland findIsland; // This needs to be instantiated
    private GridSearch gridSearch;
    private ClosestCreek closestCreek; // This needs to be instantiated

    @BeforeEach
    void startPoint() {
        map = new AreaMap();
        findIsland = new FindIsland();
        closestCreek = new ClosestCreek(map.siteMap, map.creekMap);
        droneCommand = new MoveDrone(map);
        gridSearch = new GridSearch(droneCommand, map, closestCreek);
        droneController = new DroneController(map, droneCommand, findIsland, gridSearch);
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

