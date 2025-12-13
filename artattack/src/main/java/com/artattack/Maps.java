package com.artattack;

import java.util.Map;

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

        char symbol = this.mapMatrix[curr_pos.getX()][curr_pos.getY()];

        if (this.mapMatrix[new_pos.getX()][new_pos.getY()] == ' '){
            this.mapMatrix[new_pos.getX()][new_pos.getY()] = symbol;
            return true;
        }
        else
            return false;
    }
}