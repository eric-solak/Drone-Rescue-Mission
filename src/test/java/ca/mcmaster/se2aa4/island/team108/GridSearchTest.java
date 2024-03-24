package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridSearchTest {
    private GridSearch gridSearch;
    private Position position;
    private DroneCommand droneCommand;
    private Direction direction;
    private AreaMap map;
    private JSONObject extraInfo;

    @BeforeEach
    public void initialize() {
        map = new AreaMap();
        droneCommand = new MoveDrone(map);
        ClosestCreek closestCreek = new ClosestCreek(map.siteMap, map.creekMap);
        gridSearch = new GridSearch(droneCommand, map,closestCreek);
        direction = Direction.E;
        position = new Position(0, 0);
        extraInfo = new JSONObject();

    }

    @Test
    void AfterEachFlyAction(){
        var prevAction = new JSONObject();
        prevAction.put("action", "fly");

        var expected = new JSONObject();
        expected.put("action", "scan");

        var result = gridSearch.nextMove(extraInfo, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void afterEachScanActionAboveGround(){
        var prevAction = new JSONObject();
        prevAction.put("action", "scan");
        var extras = new JSONObject();
        extras.put("creeks", new JSONArray());
        extras.put("biomes", new JSONArray().put("SHRUBLAND"));
        extras.put("sites", new JSONArray());
        droneCommand.setHeading(Direction.E);

        var expected = new JSONObject();
        expected.put("action", "fly");

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void afterEachScanActionAboveOcean(){
        var prevAction = new JSONObject();
        prevAction.put("action", "scan");
        var extras = new JSONObject();
        extras.put("creeks", new JSONArray());
        extras.put("biomes", new JSONArray().put("OCEAN"));
        extras.put("sites", new JSONArray());
        droneCommand.setHeading(Direction.E);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction);
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void findingNextSectionOfLand(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "GROUND");
        extras.put("range", 10);


        var expected = new JSONObject();
        expected.put("action", "fly");

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void noLandAheadMakeUTurnLeftWithRoomToEdge(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnRight());
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void noLandAheadMakeUTurnLeftWithoutRoomToEdge(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 2);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnLeft());
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void noLandAheadMakeUTurnRightWithRoomToEdge(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);
        gridSearch.getFirstTurn(1);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnLeft());
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void noLandAheadMakeUTurnRightWithoutRoomToEdge(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);
        gridSearch.getFirstTurn(1);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 2);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnRight());
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void afterEchoLandIsDirectlyBelow(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("action", "echo");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "GROUND");
        extras.put("range", 0);


        var expected = new JSONObject();
        expected.put("action", "scan");

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void noActionRecieved(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("fly", "forward");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        expected.put("action", "stop");

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }
    /*
    @Test
    void afterScanningACreek() {
        var prevAction = new JSONObject();
        prevAction.put("action", "scan");
        var extras = new JSONObject();
        extras.put("creeks", new JSONArray().put("CREEK_1234"));
        extras.put("biomes", new JSONArray().put("OCEAN").put("BEACH"));
        extras.put("sites", new JSONArray());
        droneCommand.setHeading(Direction.E);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction);
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());
        assertEquals("CreekCoordinates:\nCreekID: CREEK_1234, Position: [0, 0]\n",map.getCreekCoordinatesAsString());
    }

    @Test
    void afterScanningASite(){
        var prevAction = new JSONObject();
        prevAction.put("action", "scan");
        var extras = new JSONObject();
        extras.put("creeks", new JSONArray());
        extras.put("biomes", new JSONArray().put("OCEAN").put("BEACH"));
        extras.put("sites", new JSONArray().put("SITE_1234"));
        droneCommand.setHeading(Direction.E);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction);
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());
        assertEquals("SiteCoordinates:\nSiteID: SITE_1234, Position: [0, 0]\n",map.getSiteCoordinatesAsString());
    }

     */

    @Test
    void afterCompletingUTurn(){
        var prevAction = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();
        droneCommand.setHeading(Direction.E);

        prevAction.put("action", "heading");
        prevParameters.put("direction", direction);
        prevAction.put("parameters", prevParameters);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction);
        expected.put("parameters", parameters);

        var result = gridSearch.nextMove(extras, prevAction, direction, droneCommand, position,map);
        assertEquals(expected.toString(), result.toString());

    }


}

