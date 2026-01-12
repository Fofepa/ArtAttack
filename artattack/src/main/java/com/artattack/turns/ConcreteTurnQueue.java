package com.artattack.turns;

import java.util.Collections;
import java.util.List;

import com.artattack.mapelements.ActiveElement;

public class ConcreteTurnQueue implements TurnQueue {

    private List<ActiveElement> turnQueue;

    public ConcreteTurnQueue(List<ActiveElement> turnQueue){
        this.turnQueue = turnQueue;
    }

    @Override
    public TurnHandler createTurnHandler() {
        return new ConcreteTurnHandler(this);
    }

    public void add(ActiveElement element){
        this.turnQueue.add(element);
        this.reorder();
    }

    public boolean remove(ActiveElement element){
        if (turnQueue.contains(element)){
            turnQueue.remove(element);
            return true;
        }
        return false;
    }

   /*  public boolean endTurn(){

    } */

    public List<ActiveElement> getTurnQueue(){
        return this.turnQueue;
    }

    public void reorder(){
        Collections.sort(this.turnQueue);
    }
}
