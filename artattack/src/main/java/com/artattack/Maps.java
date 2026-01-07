package com.artattack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Maps {
    private Player playerOne;
    private Player playerTwo;
    private List<Enemy> enemies;
    private List<InteractableElement> interactableElements;
    private Map<Coordinates,MapElement> dictionaire; // for now we leave it here
    private char[][] mapMatrix;
    private int rows;
    private int columns;
    private ConcreteTurnHandler turnHandler;

    
    public Maps(){} // now Maps is an empty builder because of the Builder design pattern

    public void setPlayerOne(Player player){
        this.playerOne = player;
    }

    public void setPlayerTwo(Player player){
        this.playerTwo = player;
    }

    public void setEnemies(List<Enemy> enemies){
        this.enemies = enemies;
    }

    public void setInteractableElements(List<InteractableElement> interactableElements){
        this.interactableElements = interactableElements;
    }

    public void setDimension(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
    }


    public void setDict(){
         this.dictionaire = new HashMap<>();
        if (this.playerOne != null && this.playerOne.getCoordinates() != null) {
            this.dictionaire.put(this.playerOne.getCoordinates(), this.playerOne);
        }

        if (this.playerTwo != null && this.playerTwo.getCoordinates() != null) {
            this.dictionaire.put(this.playerTwo.getCoordinates(), this.playerTwo);
        }
        if (this.interactableElements != null) {
            for (InteractableElement i : this.interactableElements) {
                if (i.getCoordinates() != null)
                    this.dictionaire.put(i.getCoordinates(), i);
            }
        }
        if (this.enemies != null) {
            for (Enemy e : this.enemies) {
                if (e.getCoordinates() != null)
                    this.dictionaire.put(e.getCoordinates(), e);
            }
        }
    }

    public void setTurnQueue(){
        List<ActiveElement>list = new LinkedList<ActiveElement>();
        list.add(this.playerOne);
        list.add(this.playerTwo);
        ConcreteTurnQueue turnQueue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(list));
        this.turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
        
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

    public Player getPlayerOne(){
        return this.playerOne;
    }

    public Player getPlayerTwo(){
        return this.playerTwo;
    }

    public List<Enemy> getEnemies(){
        return this.enemies;
    }

    public ConcreteTurnHandler getConcreteTurnHandler(){
        return this.turnHandler;
    }

    public void setCell(Coordinates coord, char character){
        if(coord.getX() >= 0 && coord.getX() < rows && coord.getY() >= 0 && coord.getY() < columns)
            this.mapMatrix[coord.getX() ][coord.getY() ] = character;
    }

    public char getCell(Coordinates coord){
        if(coord.getX() >= 0 && coord.getX() < rows && coord.getY() >= 0 && coord.getY() < columns)
            return this.mapMatrix[coord.getX()][coord.getY()];
        else
            return ' ';
    }

    public void startMap(char[][] mapMatrix){
        this.mapMatrix = mapMatrix;
    }
    
    public Enemy checkAggro(Coordinates coordinates){
        for(Enemy enemy : enemies){
            if(enemy.getVisionArea().contains(coordinates)){
                return enemy;
            }
        }
        return null;
    }
}