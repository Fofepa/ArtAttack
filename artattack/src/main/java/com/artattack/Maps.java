package com.artattack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maps {
    private Map<Coordinates,MapElement> dictionaire; // for now we leave it here
    private char[][] mapMatrix;
    private int rows;
    private int columns;
    
    public Maps(Player playerOne, Player playerTwo, List<InteractableElement> interactableElements, List<Enemy> enemies ) {
        //this.dictionaire = dictionaire;
        this.rows = 36;
        this.columns = 150;
        this.dictionaire = new HashMap<>();
        if (playerOne.getCoordinates() != null && playerOne != null)
            dictionaire.put(playerOne.getCoordinates(), playerOne);

        if (playerTwo.getCoordinates() != null && playerTwo != null)
            dictionaire.put(playerTwo.getCoordinates(), playerTwo);
        if (interactableElements != null){
            for (InteractableElement i : interactableElements) {
                if (i.getCoordinates() != null)
                    dictionaire.put(i.getCoordinates(), i);
        }
}
        if(enemies != null){
            for (Enemy e : enemies) {
                if (e.getCoordinates() != null)
                    dictionaire.put(e.getCoordinates(), e);
            }
        }
           this.mapMatrix = startMap();
    }

    public char[][] getMapMatrix(){
        return this.mapMatrix;
    } 

    public Map<Coordinates,MapElement> getDict(){
        return this.dictionaire;
    }

    public int getRows(){
        return this.rows;
    }

    public int getColumns(){
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

    private char[][] startMap(){
        char[][] charMatrix = new char[this.rows][this.columns];
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                charMatrix[i][j] = '.';
            }
        }
        
        for (int i = 0; i < this.columns; i++) {
            charMatrix[0][i] = '#';
            charMatrix[this.rows - 1][i] = '#';
        }
        for (int i = 0; i < this.rows; i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][this.columns - 1] = '#';
        }
        
        for (int i = 5; i < 15; i++) {
            charMatrix[10][i] = '#';
        }
        
        for (Coordinates key : dictionaire.keySet()){
            MapElement element = dictionaire.get(key);
            charMatrix[key.getY()][key.getX()] = element.getMapSymbol();
        }
        

        return charMatrix;
    }

    
}