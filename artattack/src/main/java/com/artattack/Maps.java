package com.artattack;

import java.util.*;

public class Maps {
    private Map<Coordinates, ? extends MapElement> dictionaire;
    private char[][] mapMatrix;
    
   /* public Maps(Map<Coordinates, ? extends MapElement> dictionaire, char[][] mapMatrix){
        this.dictionaire = dictionaire;
        this.mapMatrix = mapMatrix;
    }*/

    public char[][] getMapMatrix(){
        return this.mapMatrix;
    } 

    public Map<Coordinates, ? extends MapElement> getDict(){
        return this.dictionaire;
    }

    public boolean updateMovement(Coordinates curr_pos, Coordinates new_pos){
        return false;
    }
}