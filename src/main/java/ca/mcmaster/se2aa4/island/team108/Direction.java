package ca.mcmaster.se2aa4.island.team108;

/**
 * Enum of the direction the drone is currently facing
 * turnRight() and turnLeft() return the new direction after turning
 */
public enum Direction {
    N, S, E, W;


    public Direction turnRight() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }

    public Direction turnLeft() {
        return switch (this) {
            case N -> W;
            case W -> S;
            case S -> E;
            case E -> N;
        };
    }

}