package com.artattack.turns;

import com.artattack.mapelements.ActiveElement;

public class ConcreteTurnHandler implements TurnHandler {

    private ConcreteTurnQueue turnQueue;
    private TurnManager turnManager;
    private int index = 0;
    private boolean started = false;

    public ConcreteTurnHandler(ConcreteTurnQueue turnQueue){
        this.turnQueue = turnQueue;
        this.turnManager = new TurnManager();
    }

    /**
     * Starts the turn system and notifies the first element
     */
    public void start() {
        if (!started && !turnQueue.getTurnQueue().isEmpty()) {
            started = true;
            index = 0;
            // Notify the first element in the queue
            this.turnManager.notifyTurn(turnQueue.getTurnQueue().get(0));
        }
    }

    public void start(int index){
        if(!started){
            this.started = true;
            this.index = index;
        }
    }

    public void updateTurn(){
        if(turnQueue.getTurnQueue().isEmpty()) 
            return;
        this.turnManager.notifyTurn(current());
}


    /**
     * Adds a turn listener to be notified of turn changes
     * Delegates to TurnManager
     */
    public void addTurnListener(TurnListener listener) {
        this.turnManager.addListener(listener);
    }

    @Override
    public boolean hasNext(){
        return (index < turnQueue.getTurnQueue().size());
    }

    @Override
    public ActiveElement next(){
        // Reset action points for current element before moving to next
        this.current().resetActionPoints(); 
        index++;
        
        if(hasNext()){ 
            // Get the element BEFORE reordering
            ActiveElement nextElement = this.getConcreteTurnQueue().getTurnQueue().get(index);
            this.turnManager.notifyTurn(nextElement);
            this.getConcreteTurnQueue().reorder();
            // After reorder, find where the element moved to and update index
            index = this.getConcreteTurnQueue().getTurnQueue().indexOf(nextElement);
            return nextElement;
        }
        else{
            // Loop back to beginning
            resetIndex();
            ActiveElement nextElement = this.getConcreteTurnQueue().getTurnQueue().get(index);
            this.turnManager.notifyTurn(nextElement);
            this.getConcreteTurnQueue().reorder();
            // After reorder, find where the element moved to and update index
            index = this.getConcreteTurnQueue().getTurnQueue().indexOf(nextElement);
            return nextElement;
        }
    }

    @Override
    public ActiveElement current(){
        if(turnQueue.getTurnQueue().isEmpty()){
            return null;
        }
        // When start() is called, index = 0, so we want element at index 0
        // After next() is called, index increments, so we want element at current index
        if(index >= turnQueue.getTurnQueue().size()){
            return turnQueue.getTurnQueue().get(0); // Wrapped around
        }
        return turnQueue.getTurnQueue().get(index);
    }

    public void resetIndex(){
        this.index = 0;
    }

    public int getIndex(){
        return this.index;
    }

    /**
     * Updates the index to point to the given element after queue reordering
     * This is crucial when elements are added mid-turn and the queue is reordered
     */
    public void updateIndexForElement(ActiveElement element) {
        if (element != null && turnQueue.getTurnQueue().contains(element)) {
            index = turnQueue.getTurnQueue().indexOf(element);
        }
    }

    public ConcreteTurnQueue getConcreteTurnQueue(){
        return this.turnQueue;
    }

    public void setIndex(int index){
        this.index = index;
    }

}