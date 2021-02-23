/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

/**
 *
 * @author riclimcoo
 */
public class Direction extends Vector {
    public static final Direction E = new Direction(0,1, "East");
    public static final Direction SE = new Direction(1,1, "Southeast");
    public static final Direction S = new Direction(1,0, "South");
    public static final Direction SW = new Direction(1, -1, "Southwest");
    public static final Direction W = new Direction(0,-1, "West");
    public static final Direction NW = new Direction(-1,-1, "Northwest");
    public static final Direction N = new Direction(-1,0, "North");
    public static final Direction NE = new Direction(-1,1, "Northeast");
    public static final Direction[] EIGHTDIRECTIONS = {E, SE, S, 
                                  SW, W, NW, N, NE};
    
    private final String name;

    Direction(int x, int y, String name) {
        super(x,y);
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
