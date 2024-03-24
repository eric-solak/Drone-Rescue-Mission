package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FindIslandTest {
    private FindIsland findIsland;
    private DroneCommand droneCommand;
    private Direction direction;
    private JSONObject extraInfo;

    @BeforeEach
    public void initialize() {
        findIsland = new FindIsland(new Map());
        direction = Direction.E;
        droneCommand = new MoveDrone(new Map());
    }

    @Test
    void islandNotFoundAndCommandQueueEmpty() throws Exception {
        var prevAction = new JSONObject();
        prevAction.put("action", "fly");

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction);
        expected.put("parameters", parameters);

        var result = findIsland.noLandDetected(prevAction, direction, droneCommand, extraInfo);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void islandNotFoundAndEchoCompleted() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction);
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.CheckEcho);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 10);

        var expected = new JSONObject();
        expected.put("action", "fly");

        var result = findIsland.noLandDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void islandFoundOnEchoRight() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction.turnRight());
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.CheckEcho);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnRight());
        expected.put("parameters", parameters);

        var result = findIsland.noLandDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void islandFoundOnEchoForward() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction);
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.CheckEcho);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnRight());
        expected.put("parameters", parameters);

        var result = findIsland.noLandDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void islandFoundOnEchoLeft() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction.turnLeft());
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.CheckEcho);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnLeft());
        expected.put("parameters", parameters);

        var result = findIsland.noLandDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());

    }

    @Test
    void atIslandEdge() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction);
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.MoveToIsland);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 0);

        var expected = new JSONObject();
        expected.put("action", "scan");

        var result = findIsland.landDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void awayFromIslandEdge() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction);
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.MoveToIsland);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        expected.put("action", "fly");

        var result = findIsland.landDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void PerpendicularAndNotAtEdge() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction.turnLeft());
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.Perpendicular);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "GROUND");
        extras.put("range", 10);

        var expected = new JSONObject();
        expected.put("action", "fly");

        var result = findIsland.landDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void PerpendicularAndPastEdge() throws Exception {
        var prev = new JSONObject();
        var prevParameters = new JSONObject();
        var extras = new JSONObject();

        prev.put("action", "echo");
        prevParameters.put("direction", direction.turnLeft());
        prev.put("parameters", prevParameters);
        findIsland.setCurrentState(FindIslandState.Perpendicular);
        droneCommand.setHeading(Direction.E);
        extras.put("found", "OUT_OF_RANGE");
        extras.put("range", 10);

        var expected = new JSONObject();
        var parameters = new JSONObject();
        expected.put("action", "heading");
        parameters.put("direction", direction.turnLeft());
        expected.put("parameters", parameters);

        var result = findIsland.landDetected(prev, direction, droneCommand, extras);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void defaultFirstTurnDirection(){
        assertEquals(0,findIsland.getFirstTurnDirection());
    }

    @Test
    void invalidActionArguement() throws Exception {
        var prevAction = new JSONObject();
        prevAction.put("notAnAction", "land");

        var expected = new JSONObject();
        expected.put("action", "stop");

        var result = findIsland.noLandDetected(prevAction, direction, droneCommand, extraInfo);
        assertEquals(expected.toString(), result.toString());
    }

}