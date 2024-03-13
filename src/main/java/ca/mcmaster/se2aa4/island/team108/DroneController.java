package ca.mcmaster.se2aa4.island.team108;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class DroneController {
     private final Logger logger = LogManager.getLogger();
     private enum State {FindIsland, MoveToIsland, Creek, EmergencySite}
     private State currentState = State.FindIsland;
     private FindIsland findIsland;
     private GridSearch gridSearch;
     private Map map;
     private PerimeterSearch perimeterSearch;

     public DroneController(){
          this.map = new Map();
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
                              direction.put("direction", heading.turnLeft());
                         } else { // If the ECHO commands direction was right, turn right
                              direction.put("direction", heading.turnRight());
                         }
                         move.put("parameters", direction);
                         move.put("action", "heading");
                         logger.info("MOVETOISLAND:" + move);
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
          /*
          // Look for creeks (Perimeter Search)
          else if (currentState.equals(State.Creek)) {
               move.put("action","stop");
               //move = perimeterSearch.nextMove(extraInfo, prevAction, heading);
          }

          // Look for emergency site (Grid Search)
           */
          else if (currentState.equals(State.EmergencySite)) {
               logger.info("Moving To Grid Search");
               //move.put("action","stop");
               move = gridSearch.nextMove(extraInfo, prevAction, heading);
               logger.info("printing current hashmap");
               map.printSiteCoordinates();
               logger.info("done");
          }
          return move;
     }

}
