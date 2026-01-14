package com.artattack.mapelements;

import com.artattack.level.Coordinates;

public class Trigger extends MapElement {
    private TriggerGroup triggerGroup;

    public Trigger(int ID, char mapSymbol, String name, Coordinates coordinates, TriggerGroup triggerGroup) {
        super(ID, mapSymbol, name, coordinates);
        this.triggerGroup = triggerGroup;
    }

    public void OnTrigger(Player player) {
        triggerGroup.OnTrigger(player);
    }
}
