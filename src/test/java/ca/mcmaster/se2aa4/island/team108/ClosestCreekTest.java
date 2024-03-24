package ca.mcmaster.se2aa4.island.team108;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClosestCreekTest {
    private ClosestCreek closestCreek;

    @BeforeEach
    void setUp() {
        Map<String, int[]> emergencySiteMap = new HashMap<>();
        emergencySiteMap.put("siteA", new int[]{10, 15});

        Map<String, int[]> creekMap = new HashMap<>();
        creekMap.put("creekA", new int[]{10, 16});
        creekMap.put("creeB", new int[]{434, 43});

        closestCreek = new ClosestCreek(emergencySiteMap, creekMap);
    }

    //standard test case
    @Test
    void findClosestCreekTestStandard() {

        assertEquals("creekA", closestCreek.findClosestCreek());
    }


    @Test
    void findClosestCreekWithEmptyMapsEmptyMaps() {

        Map<String, int[]> emergencySiteMap = new HashMap<>();
        Map<String, int[]> creekMap = new HashMap<>();
        ClosestCreek closestCreek = new ClosestCreek(emergencySiteMap, creekMap);

        assertNull(closestCreek.findClosestCreek());
    }

    @Test
    void findClosestCreekWithEmptyMapsBoundary() {

        Map<String, int[]> emergencySiteMap = new HashMap<>();
        emergencySiteMap.put("siteA", new int[]{0, 0});
        Map<String, int[]> creekMap = new HashMap<>();
        creekMap.put("creekA", new int[]{0, 2});
        creekMap.put("creekB", new int[]{0, 1});
        ClosestCreek closestCreek = new ClosestCreek(emergencySiteMap, creekMap);


        assertEquals("creekB", closestCreek.findClosestCreek());
    }

    @Test
    void findClosestCreekWithEmptyMapsNoCreeksOnlySite() {

        Map<String, int[]> emergencySiteMap = new HashMap<>();
        emergencySiteMap.put("siteA", new int[]{0, 0});
        Map<String, int[]> creekMap = new HashMap<>();
        ClosestCreek closestCreek = new ClosestCreek(emergencySiteMap, creekMap);


        assertNull(closestCreek.findClosestCreek());
    }
}
