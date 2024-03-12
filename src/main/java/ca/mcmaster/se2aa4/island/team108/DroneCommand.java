package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;

public interface DroneCommand {
    JSONObject dronefly() throws Exception; // OutOfRange Exception
    JSONObject droneScan() throws Exception; // BatteryLevel Exception
    JSONObject droneEcho(Direction heading) throws IllegalStateException;
    JSONObject droneTurn(Direction heading) throws IllegalStateException;
    JSONObject droneStop();

}
