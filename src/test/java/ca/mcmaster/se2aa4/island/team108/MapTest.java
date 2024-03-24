package ca.mcmaster.se2aa4.island.team108;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private AreaMap map;

    @BeforeEach

    void startPoint() {
        map = new AreaMap();


        map.position = new Position(0, 0);
    }

    @Test
    void addCreek() {
        map.addCreek("creekA", new int[]{0,0});
        assertTrue(map.creekMap.containsKey("creekA"));
    }

    @Test
    void addSiteTest() {
        map.addSite("siteA", new int[]{2, 3});
        assertTrue(map.siteMap.containsKey("siteA"));
    }

    @Test
    void creekCoordinatesAsStringTest() {
        map.addCreek("creekB", new int[]{});
        map.addCreek("creekC", new int[]{2, 3});
        String expected = "CreekCoordinates:\n" +
                          "CreekID: creekC, Position: [2, 3]\n" +
                          "CreekID: creekB, Position: []\n";
        assertEquals(expected, map.getCreekCoordinatesAsString());
    }

    @Test
    void siteCoordinatesAsStringTest() {
        map.addSite("siteB", new int[]{99, 34});
        map.addSite("siteC", new int[]{2, 23});
        String expected = "SiteCoordinates:\n" +
                          "SiteID: siteC, Position: [2, 23]\n" +
                          "SiteID: siteB, Position: [99, 34]\n";
        assertEquals(expected, map.getSiteCoordinatesAsString());
    }

    @Test
    void updateDronePositionTestHeadingNorth() {
        map.updateDronePosition(Direction.N);
        assertEquals(1, map.position.y);
        assertEquals(0, map.position.x);
    }

    @Test
    void updateDronePositionTestHeadingEast() {
        map.updateDronePosition(Direction.E);
        assertEquals(0, map.position.y);
        assertEquals(1, map.position.x);
    }

    @Test
    void updateDronePositionTestHeadingSouth() {
        map.updateDronePosition(Direction.S);
        assertEquals(-1, map.position.y);
        assertEquals(0, map.position.x);
    }

    @Test
    void updateDronePositionTestHeadingWest() {
        map.updateDronePosition(Direction.W);
        assertEquals(0, map.position.y);
        assertEquals(-1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningNtoE() {
        map.updateDronePositionForTurning(Direction.N, Direction.E);
        assertEquals(1, map.position.y);
        assertEquals(1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningNtoW() {
        map.updateDronePositionForTurning(Direction.N, Direction.W);
        assertEquals(1, map.position.y);
        assertEquals(-1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningEtoN() {
        map.updateDronePositionForTurning(Direction.E, Direction.N);
        assertEquals(1, map.position.y);
        assertEquals(1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningEtoS() {
        map.updateDronePositionForTurning(Direction.E, Direction.S);
        assertEquals(-1, map.position.y);
        assertEquals(1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningStoE() {
        map.updateDronePositionForTurning(Direction.S, Direction.E);
        assertEquals(-1, map.position.y);
        assertEquals(1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningStoW() {
        map.updateDronePositionForTurning(Direction.S, Direction.W);
        assertEquals(-1, map.position.y);
        assertEquals(-1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningWtoN() {
        map.updateDronePositionForTurning(Direction.W, Direction.N);
        assertEquals(1, map.position.y);
        assertEquals(-1, map.position.x);
    }

    @Test
    void testUpdateDronePositionForTurningWtoS() {
        map.updateDronePositionForTurning(Direction.W, Direction.S);
        assertEquals(-1, map.position.y);
        assertEquals(-1, map.position.x);
    }
}
