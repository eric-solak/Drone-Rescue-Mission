package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*
public class FindIslandTest {
    private FindIsland findIsland;
    private DroneCommand droneCommand;
    private Direction direction;

    @BeforeEach
    public void initialize() {
        findIsland = new FindIsland(new Map());
        direction = Direction.E;
        droneCommand = new MoveDrone(new Map());
    }

    @Test
    public void prevActionFly() {
        JSONObject prev = new JSONObject();
        prev.put("action", "fly");

        JSONObject expected = new JSONObject();
        expected.put("action", "scan");

        JSONObject output = findIsland.noLandDetected(prev, direction, droneCommand);

        assertEquals(expected.toString(),output.toString());

    }

    @Test
    public void prevActionScan() {
        JSONObject prev = new JSONObject();
        prev.put("action", "scan");

        JSONObject expected = new JSONObject();
        JSONObject parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction.turnLeft());
        expected.put("parameters", parameters);

        JSONObject output = findIsland.noLandDetected(prev, direction, droneCommand);

        assertEquals(expected.toString(),output.toString());

    }

    @Test
    public void prevActionEchoLeft() {
        JSONObject prev = new JSONObject();
        prev.put("action", "echo");
        JSONObject prevParameters = new JSONObject();
        prevParameters.put("direction", direction.turnLeft());
        prev.put("parameters", prevParameters);

        JSONObject expected = new JSONObject();
        JSONObject parameters = new JSONObject();
        expected.put("action", "echo");
        parameters.put("direction", direction.turnRight());
        expected.put("parameters", parameters);

        JSONObject output = findIsland.noLandDetected(prev, direction, droneCommand);

        assertEquals(expected.toString(),output.toString());
    }

    @Test
    public void prevActionEchoRight() {
        JSONObject prev = new JSONObject();
        prev.put("action", "echo");
        JSONObject prevParameters = new JSONObject();
        prevParameters.put("direction", direction.turnRight());
        prev.put("parameters", prevParameters);

        JSONObject expected = new JSONObject();
        expected.put("action", "fly");

        JSONObject output = findIsland.noLandDetected(prev, direction, droneCommand);

        assertEquals(expected.toString(),output.toString());
    }
}
 */
