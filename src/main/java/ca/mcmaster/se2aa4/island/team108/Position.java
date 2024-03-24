package ca.mcmaster.se2aa4.island.team108;

/**
 * Position keeps track of the current position of the drone
 * i.e. its coordinates
 */
public class Position {


    public int x;
    public int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }


    public int[] getCoords() {
        return new int[]{x, y};
    }

}

