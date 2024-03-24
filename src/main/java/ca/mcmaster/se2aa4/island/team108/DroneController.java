package ca.mcmaster.se2aa4.island.team108;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Arrays;

public class DroneController {
     private final Logger logger = LogManager.getLogger();
     private enum State {FindIsland, MoveToIsland, SearchIsland}
     private State currentState = State.FindIsland;
     private Position position;
     private DroneCommand droneCommand;
     private FindIsland findIsland;
     private GridSearch gridSearch;

     public DroneController(AreaMap map) {
          this.droneCommand = new MoveDrone(map);
          this.findIsland = new FindIsland();
          this.gridSearch = new GridSearch(this.droneCommand, map, new ClosestCreek(map.siteMap, map.creekMap));
          this.position = new Position(0, 0);
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
     public JSONObject getNextMove(JSONObject extraInfo, JSONObject prevAction, Direction heading, AreaMap map ) {
          JSONObject move = new JSONObject();
          droneCommand.setHeading(heading);
          position = droneCommand.getPosition();
          try {
               if (currentState.equals(droneControllerState.FindIsland)) {
                    if (prevAction.has("action")) {
                         String prev = prevAction.getString("action");
                         if (Objects.equals(prev, "heading")) {
                              currentState = droneControllerState.MoveToIsland;
                              move = droneCommand.droneEcho(heading);
                         } else {
                              move = findIsland.noLandDetected(prevAction, heading, droneCommand, extraInfo);
                         }
                    } else {
                         move.put("action", "fly");
                    }
               } else if (currentState.equals(droneControllerState.MoveToIsland)) {
                    String prev = prevAction.getString("action");
                    if (Objects.equals(prev, "scan")) {
                         currentState = droneControllerState.SearchIsland;
                         move = droneCommand.dronefly();
                         gridSearch.getFirstTurn(findIsland.getFirstTurnDirection());
                    } else {
                         move = findIsland.landDetected(prevAction, heading, droneCommand, extraInfo);
                    }
               } else if (currentState.equals(droneControllerState.SearchIsland)) {
                    move = gridSearch.nextMove(extraInfo, prevAction, heading, droneCommand, position, map);
                    logger.info("Reaching Site Check");
                    if (prevAction.has("action")){
                         String prev = prevAction.getString("action");
                         if (Objects.equals(prev, "scan")) {
                              JSONArray creekArray = extraInfo.optJSONArray("creeks");
                              boolean containsCreek = creekArray != null && !creekArray.isEmpty();
                              JSONArray siteArray = extraInfo.optJSONArray("sites");
                              boolean containsSite = creekArray != null && !siteArray.isEmpty();
                              if (containsSite) {
                                   logger.info("Site found");
                                   for (int i = 0; i < siteArray.length(); i++) {
                                        String siteID = siteArray.getString(i);
                                        logger.info("Adding site: " + siteID + " at position: " + Arrays.toString(position.getCoords()));
                                        map.addSite(siteID, position.getCoords());
                                   }
                              } else if (containsCreek) {
                                   logger.info("Creek found");
                                   for (int i = 0; i < creekArray.length(); i++) {
                                        String creekID = creekArray.getString(i);

                                        map.addCreek(creekID, position.getCoords());
                                   }
                              }
                         }
                    }
               }
          } catch (Exception e){
               logger.error("Exception: " + e);
               return droneCommand.droneStop();
          }
          logger.info("Action sent to explorer: " + move.toString());
          return move;
     }
}