package com.artattack.turns;

import java.util.*;
import com.artattack.mapelements.ActiveElement;

public class TurnManager {
    private List<TurnListener> listeners;

    public TurnManager(){
        this.listeners = new ArrayList<>();
    }

    public void addListener(TurnListener turnListener){
        this.listeners.add(turnListener);
    }

    public void notifyTurn(ActiveElement activeElement){
        
    }
}
