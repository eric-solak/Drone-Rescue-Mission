package ca.mcmaster.se2aa4.island.team108;
import java.util.List;


interface PathFinder {
    List<Direction> calculatePath(Position current, Position destination);
}