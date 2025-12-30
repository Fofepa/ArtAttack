package com.artattack;

public interface  TurnHandler {
    public boolean hasNext();
    public ActiveElement next();
    public ActiveElement current();
}
