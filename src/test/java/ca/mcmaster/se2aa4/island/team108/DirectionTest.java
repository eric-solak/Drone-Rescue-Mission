package ca.mcmaster.se2aa4.island.team108;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

    private Direction direction;

    @Test
    public void turnRightFromNorth() {
        Direction input = Direction.N;
        Direction expected = Direction.E;

        input = input.turnRight();
        assertEquals(expected, input);
    }

    @Test
    public void turnLeftFromNorth() {
        Direction input = Direction.N;
        Direction expected = Direction.W;

        input = input.turnLeft();
        assertEquals(expected, input);
    }

    @Test
    public void turnRightFromEast() {
        Direction input = Direction.E;
        Direction expected = Direction.S;

        input = input.turnRight();
        assertEquals(expected, input);
    }

    @Test
    public void turnLeftFromEast() {
        Direction input = Direction.E;
        Direction expected = Direction.N;

        input = input.turnLeft();
        assertEquals(expected, input);
    }

    @Test
    public void turnRightFromSouth() {
        Direction input = Direction.S;
        Direction expected = Direction.W;

        input = input.turnRight();
        assertEquals(expected, input);
    }

    @Test
    public void turnLeftFromSouth() {
        Direction input = Direction.S;
        Direction expected = Direction.E;

        input = input.turnLeft();
        assertEquals(expected, input);
    }

    @Test
    public void turnRightFromWest() {
        Direction input = Direction.W;
        Direction expected = Direction.N;

        input = input.turnRight();
        assertEquals(expected, input);
    }

    @Test
    public void turnLeftFromWest() {
        Direction input = Direction.W;
        Direction expected = Direction.S;

        input = input.turnLeft();
        assertEquals(expected, input);
    }
}
