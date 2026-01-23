package com.artattack.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MapElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.Trigger;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;


public class Maps {
    private static int nextID = 0;
    private final int ID;
    private Player playerOne;
    private Player playerTwo;
    private List<Enemy> enemies;
    private List<Trigger> triggers;
    private List<InteractableElement> interactableElements;
    private transient Map<Coordinates,MapElement> dictionaire; // for now we leave it here
    private char[][] mapMatrix;
    private int width;
    private int height;
    private ConcreteTurnHandler turnHandler;

    
    public Maps(){
        this.ID = nextID++;
    } // now Maps is an empty builder because of the Builder design pattern

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

    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height) {
        if (this.triggers == null) {
            this.triggers = new ArrayList<>();
        }
        for (int i = 0; i < width; i++) {
            this.triggers.add(new Trigger(0, '.', "Trigger", new Coordinates(i + offset.getX(), offset.getY()), triggerGroup));
            this.triggers.add(new Trigger(0, '.', "Trigger", new Coordinates(i + offset.getX(), height - 1 + offset.getY()), triggerGroup));
        }
        for (int i = 1; i < height; i++) {
            this.triggers.add(new Trigger(0, '.', "Trigger", new Coordinates(offset.getX(), i + offset.getY()), triggerGroup));
            this.triggers.add(new Trigger(0, '.', "Trigger", new Coordinates(width - 1 + offset.getX(), i + offset.getY()), triggerGroup));
        }
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                this.triggers.add(new Trigger(0, '.', "Trigger", new Coordinates(i + offset.getX(), j + offset.getY()), triggerGroup));
            }
        }
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setTurnHandler(ConcreteTurnHandler turnHandler){
        this.turnHandler = turnHandler;
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
        if (this.triggers != null) {
            for (Trigger t : this.triggers) {
                if (t.getCoordinates() != null && this.dictionaire.get(t.getCoordinates()) == null) {
                    this.dictionaire.put(t.getCoordinates(), t);
                }
            }
        }
    }

    public void updateDict(Coordinates oldCoordinates, Coordinates newCoordinates){
        if(oldCoordinates == null || newCoordinates == null){
            return;
        }

        MapElement element = this.dictionaire.remove(oldCoordinates);
        if(element != null){
            this.dictionaire.put(newCoordinates,element);
        }
    }

    public void setTurnQueue(){
        List<ActiveElement>list = new LinkedList<ActiveElement>();
        list.add(this.playerOne);
        list.add(this.playerTwo);
        ConcreteTurnQueue turnQueue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(list));
        this.turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
        
    }

    public void setTurnQueue(Player currPlayer, Player otherPlayer){
        List<ActiveElement>list = new LinkedList<ActiveElement>();
        list.add(currPlayer);
        list.add(otherPlayer);
        ConcreteTurnQueue turnQueue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(list));
        this.turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
    }

    private int getID(){
        return this.ID;
    }


    public char[][] getMapMatrix(){
        return this.mapMatrix;
    } 

    public Map<Coordinates,MapElement> getDict(){
        return this.dictionaire;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
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
        if(coord.getX() >= 0 && coord.getX() < height && coord.getY() >= 0 && coord.getY() < width)
            this.mapMatrix[coord.getX() ][coord.getY() ] = character;
    }

    public char getCell(Coordinates coord){
        if(coord.getX() >= 0 && coord.getX() < height && coord.getY() >= 0 && coord.getY() < width)
            return this.mapMatrix[coord.getX()][coord.getY()];
        else
            return ' ';
    }

    public void startMap(char[][] mapMatrix){
        this.mapMatrix = mapMatrix;
    }
    
    public Enemy checkAggro(Coordinates coordinates){
        for(Enemy enemy : enemies){
            if(Coordinates.sum(enemy.getVisionArea(), enemy.getCoordinates()).contains(coordinates)){
                return enemy;
            }
        }
        return null;
    }

    public void remove(ActiveElement element){
        if(this.dictionaire.containsValue(element)){
            this.turnHandler.getConcreteTurnQueue().remove(element);
            this.mapMatrix[element.getCoordinates().getX()][element.getCoordinates().getY()] = '.';
            if(element instanceof Player){
                if(element.equals(this.playerOne))
                    this.playerOne = null;
                else this.playerTwo = null;
            } //else this.enemies.remove(this.enemies.indexOf(element)) doesn't work if List<Enemy> is unmodifiable (like List.of())
            this.dictionaire.remove(element.getCoordinates());
        }
    }
}