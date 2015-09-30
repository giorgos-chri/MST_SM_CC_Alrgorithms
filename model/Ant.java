/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Giorgos
 */
public abstract class Ant implements Comparable<Ant>{
   private final int id;
   private final double x;
   private final double y;
   
   public Ant(int id, double x, double y){
       this.id = id;
       this.x = x;
       this.y = y;
   }
   
/**
Returns the id the ant.
*/
   public int getID(){
       return this.id;
   }
/**
Return the X coordinate of the ant
*/   

   public double getX(){
       return this.x;
   }
   
/**
Return the Y coordinate of the ant
*/
   public double getY(){
       return this.y;
   }
   
   public double getDistanceFrom(Ant other){
       double new_y = Math.abs(this.y - other.getY());
       double new_x = Math.abs(this.x - other.getX());
       return Math.sqrt(Math.pow(new_y, 2.0) + Math.pow(new_x, 2.0));
   }
   
   @Override
   public boolean equals(Object o){
       return ((Ant)o).getID() == this.id && ((Ant)o).getX() == this.x && ((Ant)o).getY() == this.y;
   }
   
   @Override
   public int hashCode(){
       int hash = 7;
       hash = 23 * hash + this.id;
       hash = 23 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
       hash = 23 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
       return hash;
   }
   
   @Override
   public int compareTo(Ant o){
       return Integer.compare(this.id, o.getID());
   }
   
}
