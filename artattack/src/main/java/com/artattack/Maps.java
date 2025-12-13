package com.artattack;

import java.util.Map;

public class Maps {
    private Map<Coordinates, ? extends MapElement> dictionaire; // for now we leave it here
    private char[][] mapMatrix;
    private int rows;
    private int columns;
    
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

    public int getRows(){
        return this.rows;
    }

    public int getcolumns(){
        return this.columns;
    }

    public void setCell(int row, int column, char character){
        if(row >= 0 && row < rows && column >= 0 && column < columns)
            this.mapMatrix[row][column] = character;
    }

    public char getCell(int row, int column){
        if(row >= 0 && row < rows && column >= 0 && column < columns)
            return this.mapMatrix[row][column];
        else
            return ' ';
    }
   /*  public boolean updateMovement(Coordinates curr_pos, Coordinates new_pos){ Should be inside the execute() of the MovementStrategy

        char symbol = this.mapMatrix[curr_pos.getX()][curr_pos.getY()];

        if (this.mapMatrix[new_pos.getX()][new_pos.getY()] == ' '){
            this.mapMatrix[new_pos.getX()][new_pos.getY()] = symbol;
            return true;
        }
        else
            return false;
    } */

    
}