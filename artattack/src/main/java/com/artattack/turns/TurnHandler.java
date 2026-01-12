package com.artattack.turns;

import com.artattack.mapelements.ActiveElement;

public interface  TurnHandler {
    public boolean hasNext();
    public ActiveElement next();
    public ActiveElement current();
}
