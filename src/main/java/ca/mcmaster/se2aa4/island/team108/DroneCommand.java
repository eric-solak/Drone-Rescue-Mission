package ca.mcmaster.se2aa4.island.team108;

import org.json.JSONObject;


public interface DroneCommand {
    Position getPosition();
    void setHeading(Direction heading);
    JSONObject dronefly() throws Exception;
    JSONObject droneScan() throws Exception;
    JSONObject droneEcho(Direction heading) throws IllegalStateException;
    JSONObject droneTurn(Direction heading) throws IllegalStateException;
    JSONObject droneStop();



}