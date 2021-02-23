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
public class Vector {
    private int x;
    private int y;

    Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector add(Vector addend){
        return new Vector(x + addend.x, y + addend.y);
    }
    
    public Vector clone(){
        return new Vector(this.x, this.y);
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public String toString(){
        return String.format("(%d,%d)", x, y);
    }
    
    public static Vector[] makeCartesianProduct(int[] intArr){
        Vector[] cartesProduct = new Vector[intArr.length*intArr.length];
        int topOfStack = 0;
        for (int i : intArr){
            for (int j : intArr){
                cartesProduct[topOfStack++] = new Vector(i,j);
            }
        }
        return cartesProduct;
    }
}
