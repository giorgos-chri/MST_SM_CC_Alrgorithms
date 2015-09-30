/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Comparator;

/**
 *
 * @author Giorgos
 */
public class Edge implements Comparator{
    private int from;
    private int to;
    private double distance;
    
    public Edge(int f, int t, double c){
        this.from = f;
        this.to = t;
        this.distance = c;
    }
    
    public Edge(){
        
    }
    
    public int getFrom(){
        return this.from;
    }
    
    public void setFrom(int from){
        this.from = from;
    }
    
    public int getTo(){
        return this.to;
    }
    
    public void setTo(int to){
        this.to = to;
    }
    
    public double getDistance(){
        return this.distance;
    }
    
    public void setCost(double cost){
        this.distance = cost;
    }
    
    public int compare(Object o1, Object o2){
        double cost1 = ((Edge)o1).distance;
        double cost2 = ((Edge)o2).distance;
        int from1 = ((Edge)o1).from;
        int from2 = ((Edge)o2).from;
        int to1 = ((Edge)o1).to;
        int to2 = ((Edge)o2).to;
        if(cost1 < cost2){
            return -1;
        }
        if (cost1 == cost2 && from1 == from2 && to1 == to2){
            return -1;
        }
        if(cost1 == cost2){
            return -1;
        }
        if(cost1 > cost2){
            return 1;
        }
        return 0;        
    }
    
    @Override
    public boolean equals(Object obj){
        Edge e = (Edge)obj;
        return this.distance == e.distance && this.from == e.from && this.to == e.to;
    }    
}