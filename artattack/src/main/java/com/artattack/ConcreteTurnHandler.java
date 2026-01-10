package com.artattack;

public class ConcreteTurnHandler implements TurnHandler {

    private ConcreteTurnQueue turnQueue;
    private int index = 0;

    public ConcreteTurnHandler(ConcreteTurnQueue turnQueue){
        this.turnQueue = turnQueue;
    }


    @Override
    public boolean hasNext(){
        return (index < turnQueue.getTurnQueue().size());
    }

    @Override
    public ActiveElement next(){    // add the isActive control
        this.current().resetActionPoints(); 
        if(hasNext()){ 
            return turnQueue.getTurnQueue().get(index++);
        }
        else{
            resetIndex();
            return turnQueue.getTurnQueue().get(index++);
        }
    }

    @Override
    public ActiveElement current(){
        if(index-1 < 0){
            return turnQueue.getTurnQueue().get(turnQueue.getTurnQueue().size() -1);
        }
        else{
            return turnQueue.getTurnQueue().get(index-1);
        }
    }

    public void resetIndex(){
        this.index = 0;
    }

    public int getIndex(){
        return this.index;
    }

    public ConcreteTurnQueue getConcreteTurnQueue(){
        return this.turnQueue;
    }
}