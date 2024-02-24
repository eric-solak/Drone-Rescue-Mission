package ca.mcmaster.se2aa4.island.team108;

// Direction/Compass Enum
public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    // implementing turnRight and turnLeft
    public Direction turnRight() {

        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                return this; 
        }
    }

    public Direction turnLeft() {

        switch (this) {
            case NORTH:
                return WEST;
            case WEST:
                return SOUTH;
            case SOUTH:
                return EAST;
            case EAST:
                return NORTH;
            default:
                return this; 
        }
    }
}
