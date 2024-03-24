package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveDroneTest {
    private MoveDrone droneCommand;
    private Direction direction;


    @BeforeEach
    public void initialize() {
        AreaMap map = new AreaMap();
        droneCommand = new MoveDrone(map);
        direction = Direction.E;
    }

    @Test
    void OnFlyRequest(){
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.dronefly();
        assertEquals(action.toString(), "{\"action\":\"fly\"}");
    }

    @Test
    void onScanRequest() {
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.droneScan();
        assertEquals(action.toString(), "{\"action\":\"scan\"}");
    }

    @Test
    void onEchoRequest(){
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.droneEcho(direction);
        assertEquals(action.toString(), "{\"action\":\"echo\",\"parameters\":{\"direction\":\"E\"}}");
    }

    @Test
    void onTurnLeftRequest(){
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.droneTurn(direction.turnLeft());
        assertEquals(action.toString(), "{\"action\":\"heading\",\"parameters\":{\"direction\":\"N\"}}");
    }
    @Test
    void onTurnRightRequest(){
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.droneTurn(direction.turnRight());
        assertEquals(action.toString(), "{\"action\":\"heading\",\"parameters\":{\"direction\":\"S\"}}");
    }

    @Test
    void onStopRequest() {
        droneCommand.setHeading(Direction.E);
        JSONObject action = droneCommand.droneStop();
        assertEquals(action.toString(), "{\"action\":\"stop\"}");
    }
}