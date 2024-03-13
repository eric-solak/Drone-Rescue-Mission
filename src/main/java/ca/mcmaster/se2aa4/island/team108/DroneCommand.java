package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;
/*
    To use:
    instantiate a DroneCommand object in your class
    Create a JSONObject to assign to the result of the command
    Call the method on your DroneCommand object and assign result to your JSONObject
 */

public interface DroneCommand {
    JSONObject dronefly() throws Exception; // OutOfRange Exception
    JSONObject droneScan() throws Exception; // BatteryLevel Exception
    JSONObject droneEcho(Direction heading) throws IllegalStateException;
    JSONObject droneTurn(Direction heading) throws IllegalStateException;
    JSONObject droneStop();

}
