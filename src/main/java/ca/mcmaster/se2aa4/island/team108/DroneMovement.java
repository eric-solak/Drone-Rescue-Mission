package ca.mcmaster.se2aa4.island.team108;
import java.util.List;

public class DroneMovement implements PathFinder {


    @Override
    public List<Direction> calculatePath(Position current, Position destination){

         return List.of(Direction.EAST, Direction.NORTH); // just an example of what it could look like. implement logic later
    }
}
