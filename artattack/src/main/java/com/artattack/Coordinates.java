package com.artattack;

import java.util.*;

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

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
