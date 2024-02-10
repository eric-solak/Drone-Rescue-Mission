package ca.mcmaster.se2aa4.island.team108;
import java.util.List;

public class DroneController {
     private DroneMovement droneMovement;
     //make decisions based on pathfinder

     public DroneController(){
          this.droneMovement = new DroneMovement();
     }

     public void executeInstruction(Position current, Position destination){
          List<Direction> path = droneMovement.calculatePath(current, destination);

     }
}
