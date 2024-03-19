package ca.mcmaster.se2aa4.island.team108;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DroneController {
     private final Logger logger = LogManager.getLogger();
     private enum State {FindIsland, MoveToIsland, Creek, EmergencySite}
     private State currentState = State.FindIsland;
     private FindIsland findIsland;
     private GridSearch gridSearch;
     private PerimeterSearch perimeterSearch;

     public DroneController(Map map){
          this.findIsland = new FindIsland();
          this.gridSearch = new GridSearch(map);
          this.perimeterSearch = new PerimeterSearch();
     }

     /**
      * Gets the next move based on the current state of the program
      * STATES: { FindIsland, MoveToIsland, Creek (PerimeterSearch), EmergencySite (GridSearch) }
      * FindIsland -> MoveToIsland -> EmergencySite -> Creek -> Stop
      * @param extraInfo JSONObject that contains the results of the previous action
      * @param prevAction JSONObject of the previous action
      * @param heading Current direction the drone is facing
      * @return JSONObject of the next move
      */
     public JSONObject getNextMove(JSONObject extraInfo, JSONObject prevAction, Direction heading) {
          DroneCommand droneCommand = new MoveDrone();
          JSONObject move = new JSONObject();
          
          // Search for island
          if (currentState.equals(State.FindIsland)) {
               if (!extraInfo.has("found")) {
                    move = findIsland.noLandDetected(prevAction, heading);
               } else {
                    if (extraInfo.getString("found").equals("GROUND")) { // Ground detected in ECHO command
                         JSONObject parameters = prevAction.getJSONObject("parameters");
                         Direction echoDirection = (Direction) parameters.get("direction");

                         JSONObject direction = new JSONObject();
                         if (echoDirection == heading.turnLeft()) { // If the ECHO commands direction was left, turn left
                              move = droneCommand.droneTurn(heading.turnLeft());
                         } else { // If the ECHO commands direction was right, turn right
                              move = droneCommand.droneTurn(heading.turnRight());
                         }
                         currentState = State.MoveToIsland;
                    } else {
                         move = findIsland.noLandDetected(prevAction, heading);
                    }
               }
          }

          // Traverse to island
          else if (currentState.equals(State.MoveToIsland)) {
               if (extraInfo.has("biomes")) {
                    JSONArray biomesArray = extraInfo.getJSONArray("biomes");
                    boolean containsOcean = biomesArray.toList().contains("OCEAN");
                    int numBiomes = biomesArray.length();
                    if (!containsOcean || numBiomes > 1) {
                         //move = perimeterSearch.nextMove(extraInfo, prevAction, heading);
                         currentState = State.EmergencySite;
                         move = findIsland.landDetected(prevAction);
                    } else {
                         move = findIsland.landDetected(prevAction);
                    }
               } else {
                    move = findIsland.landDetected(prevAction);
               }
          }
          // Look for emergency site (Grid Search)
          else if (currentState.equals(State.EmergencySite)) {
               logger.info("MOVING TO GRID SEARCH");
               //move = droneCommand.droneStop();
               move = gridSearch.nextMove(extraInfo, prevAction, heading);
          }
          return move;
     }

}
