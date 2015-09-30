/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Giorgos
 */
public class BlackAnt extends Ant{
    private final int[] objects;
    
    public BlackAnt(int id, double x, double y, int[] objects){
        super(id, x, y);
        this.objects = objects;
    }
    
    public int[] getObjects(){
        return this.objects;
    }
    
    @Override
    public String toString(){
        return "Black Ant: " + this.getID();
    }
}
