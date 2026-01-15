package com.artattack.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

   
    
    public int getX(){
       return this.x;
    }
    

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }


    public void setY(int y){
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(this.getClass() != obj.getClass()) return false;
        Coordinates other = (Coordinates) obj;
        if(this.getX() == other.getX() && this.getY() == other.getY()) 
            return true;
        return false;
    }

    public static Coordinates sum(Coordinates... args) {
        Coordinates sum = new Coordinates(0, 0);
        for (Coordinates c : args) {
            sum = new Coordinates(sum.getX() + c.getX(), sum.getY() + c.getY());
        }
        return sum;
    }

    public static List<Coordinates> sum(List<Coordinates> area, Coordinates shift){
        List<Coordinates> shifted = new ArrayList<>();
        for(Coordinates coordinates : area){
            shifted.add(Coordinates.sum(coordinates, shift));
        }

        return shifted;
    }

    public static double getDistance(Coordinates c1, Coordinates c2){
        return Math.hypot(c2.getX() - c1.getX(), c2.getY() - c1.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
