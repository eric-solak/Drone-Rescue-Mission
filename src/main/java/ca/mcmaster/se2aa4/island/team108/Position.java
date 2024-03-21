package ca.mcmaster.se2aa4.island.team108;

public class Position {

    //implement position tracking
    public int x;
    public int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position getPosition() {
        return this;
    }
    public int[] getCoords() {
        return new int[]{x, y};
    }

}