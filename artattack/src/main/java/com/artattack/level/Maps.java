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
    private  int ID;
    private Player p1;
    private Player p2;
    private List<Enemy> enemies;
    private List<Trigger> triggers;
    private List<InteractableElement> interactableElements;
    private transient Map<Coordinates,MapElement> dictionaire; // for now we leave it here
    private char[][] mapMatrix;
    private int width;
    private int height;
    private transient ConcreteTurnHandler turnHandler;
    private Coordinates p1spawn;
    private Coordinates p2spawn;

    
    public Maps(){
        
    } // now Maps is an empty builder because of the Builder design pattern

    public void setID(int ID){
        this.ID = ID;
    }

    public void setPlayerOne(Player player){
        this.p1 = player;
        if (this.p1spawn != null) {
            this.p1.setCoordinates(this.p1spawn);
        }
    }

    public void setPlayerTwo(Player player){
        this.p2 = player;
        if (this.p2spawn != null) {
            this.p2.setCoordinates(this.p2spawn);
        }
    }

    public void setEnemies(List<Enemy> enemies){
        this.enemies = enemies;
    }

    public void setSpawn(Coordinates p1spawn, Coordinates p2spawn) {
        this.p1spawn = p1spawn;
        this.p2spawn = p2spawn;
    }

    public Coordinates getP1Spawn() {
        return this.p1spawn;
    }

    public Coordinates getP2Spawn() {
        return this.p2spawn;
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
        if (this.p1 != null && this.p1.getCoordinates() != null) {
            this.dictionaire.put(this.p1.getCoordinates(), this.p1);
        }

        if (this.p2 != null && this.p2.getCoordinates() != null) {
            this.dictionaire.put(this.p2.getCoordinates(), this.p2);
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
        list.add(this.p1);
        list.add(this.p2);
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

    public void setTurnQueue(List<ActiveElement> elements, int index){
        ConcreteTurnQueue turnQueue = new ConcreteTurnQueue(elements);
        this.turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
        this.turnHandler.getConcreteTurnQueue().reorder();
        this.turnHandler.setIndex(index);
    }

    public int getID(){
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
        return this.p1;
    }

    public Player getPlayerTwo(){
        return this.p2;
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
    
    public List<Enemy> checkAggro(Coordinates coordinates){
        List<Enemy> aggroed = new ArrayList<>();
        for(Enemy enemy : enemies){
            if(Coordinates.sum(enemy.getVisionArea(), enemy.getCoordinates()).contains(coordinates)){
                /* enemy.activate(); */
                aggroed.add(enemy);
            }
        }
        return aggroed.isEmpty() ? null : aggroed;
    }

    public void checkPlayerEscape(){
        List<Enemy> toRemove = new ArrayList<>();
        for(ActiveElement element : this.turnHandler.getConcreteTurnQueue().getTurnQueue()){
            if(element instanceof Enemy e){
                if(!Coordinates.sum(e.getVisionArea(), e.getCoordinates()).contains(p1.getCoordinates()) && !Coordinates.sum(e.getVisionArea(), e.getCoordinates()).contains(p2.getCoordinates())){
                    toRemove.add(e);
                }
            }
        }
        for(Enemy e : toRemove){
            if(!Coordinates.sum(e.getVisionArea(), e.getCoordinates()).contains(p1.getCoordinates()) && !Coordinates.sum(e.getVisionArea(), e.getCoordinates()).contains(p2.getCoordinates())) {
                // if both players escaped from the enemy
                System.out.println("The party has escaped from " + e.getName() + " and it has been removed from the queue");
                this.turnHandler.getConcreteTurnQueue().remove(e);
            }
        }
    }

    public void remove(ActiveElement element){
        if(this.dictionaire.containsValue(element)){
            this.turnHandler.getConcreteTurnQueue().remove(element);
            this.mapMatrix[element.getCoordinates().getX()][element.getCoordinates().getY()] = '.';
            if(element instanceof Player){
                if(element.equals(this.p1))
                    this.p1 = null;
                else this.p2 = null;
            } //else this.enemies.remove(this.enemies.indexOf(element)) doesn't work if List<Enemy> is unmodifiable (like List.of())
            this.dictionaire.remove(element.getCoordinates());
        }
    }
}