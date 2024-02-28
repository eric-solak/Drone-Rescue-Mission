package ca.mcmaster.se2aa4.island.team108;
import org.json.JSONArray;
import org.json.JSONObject;

public class DroneController {
     private enum State {FindIsland, MoveToIsland, Creek, EmergencySite}
     private State currentState = State.FindIsland;
     private FindIsland findIsland;
     private GridSearch gridSearch;

     public DroneController(){
          this.findIsland = new FindIsland();
          this.gridSearch = new GridSearch();
     }

     public JSONObject getNextMove(JSONObject extraInfo, JSONObject prevAction, Direction heading) {
          JSONObject move = new JSONObject();

          // STATE: No Island Found
          if (currentState.equals(State.FindIsland)) {
               if (!extraInfo.has("found")) {
                    move = findIsland.noLandDetected(prevAction, heading);
               } else {
                    if (extraInfo.getString("found").equals("GROUND")) {
                         JSONObject parameters = prevAction.getJSONObject("parameters");
                         Direction echoDirection = (Direction) parameters.get("direction");

                         JSONObject direction = new JSONObject();
                         if (echoDirection == heading.turnLeft()) {
                              direction.put("direction", heading.turnLeft());
                         } else {
                              direction.put("direction", heading.turnRight());
                         }
                         move.put("parameters", direction);
                         move.put("action", "heading");
                         currentState = State.MoveToIsland;
                    } else {
                         move = findIsland.noLandDetected(prevAction, heading);
                    }
               }
          }

          // STATE: Island is found, fly to it
          else if (currentState.equals(State.MoveToIsland)) {
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

          // STATE: Look for creeks (Perimeter Search)
          else if (currentState.equals(State.Creek)) {
               move.put("action", "stop");
          }

          // STATE: Look for emergency site (Grid Search)
          else {
               move = gridSearch.nextMove(extraInfo, prevAction, heading);
          }
          return move;
     }

}
