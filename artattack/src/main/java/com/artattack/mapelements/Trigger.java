package com.artattack.mapelements;

import com.artattack.level.Coordinates;
import com.artattack.view.GameContext;

public class Trigger extends MapElement {
    private TriggerGroup triggerGroup;

    public Trigger(int ID, char mapSymbol, String name, Coordinates coordinates, TriggerGroup triggerGroup, String spritePath) {
        super(ID, mapSymbol, name, coordinates, spritePath);
        this.triggerGroup = triggerGroup;
    }

    public TriggerGroup getTriggerGroup() {
        return this.triggerGroup;
    }

    public void OnTrigger(GameContext gameContext, Player player, String spritePath) {
        triggerGroup.OnTrigger(gameContext, player, spritePath);
    }
}
