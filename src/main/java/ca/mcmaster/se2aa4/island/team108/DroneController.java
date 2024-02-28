package ca.mcmaster.se2aa4.island.team108;
import org.json.JSONArray;
import org.json.JSONObject;

public class DroneController {
     private enum State {FindIsland, MoveToIsland, Creek, EmergencySite}
     private State currentState = State.FindIsland;
     private FindIsland findIsland;

     public DroneController(){
          this.findIsland = new FindIsland();
     }

     public JSONObject getNextMove(JSONObject extraInfo, JSONObject prevAction) {
          JSONObject move = new JSONObject();

          if (currentState.equals(State.FindIsland)) {
               if (!extraInfo.has("found")) {
                    move = findIsland.noLandDetected(prevAction);
               } else {
                    JSONArray biomesArray = extraInfo.getJSONArray("biomes");
                    boolean containsGround = biomesArray.toList().contains("GROUND");
                    if (containsGround) { // If the island is found using "echo", change direction
                         JSONObject direction = new JSONObject();
                         direction.put("direction", "S");
                         move.put("parameters", direction);
                         move.put("action", "heading");
                         currentState = State.MoveToIsland; // switch state
                    } else {
                         move = findIsland.noLandDetected(prevAction);
                    }

               }

          }

          else if (currentState.equals(State.MoveToIsland)) { // Moves forward until "scan" returns "BEACH"
               if (extraInfo.has("biomes")) {
                    JSONArray biomesArray = extraInfo.getJSONArray("biomes");
                    boolean containsBeach = biomesArray.toList().contains("BEACH");
                    if (containsBeach) {
                         currentState = State.Creek;
                    } else {
                         move = findIsland.landDetected(prevAction);
                    }
               } else {
                    move = findIsland.landDetected(prevAction);
               }

          }

          else if (currentState.equals(State.Creek)) { // No implementation yet
               move.put("action", "stop");
          }

          else {
               move.put("action", "stop");
          }
          return move;
     }

}
