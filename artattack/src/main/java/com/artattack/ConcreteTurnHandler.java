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
    public ActiveElement next(){
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
        return turnQueue.getTurnQueue().get(index-1);
    }

    public void resetIndex(){
        this.index = 0;
    }

    public int getIndex(){
        return this.index;
    }
}
