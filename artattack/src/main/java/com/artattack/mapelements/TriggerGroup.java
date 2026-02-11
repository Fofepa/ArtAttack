package com.artattack.mapelements;

import com.artattack.interactions.Interaction;
import com.artattack.view.GameContext;

public class TriggerGroup {
    private Interaction interaction;
    private boolean consumed;

    public TriggerGroup(Interaction interaction) {
        this.interaction = interaction;
        this.consumed = false;
    }

    public void OnTrigger(GameContext gameContext, Player player, String spritePath) {
        if (!this.consumed) {
            this.interaction.doInteraction(gameContext, player, spritePath);
            this.consumed = true;
        }
    }

    public Interaction getInteraction() {
        return this.interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public void toggle(boolean b) {
        this.consumed = b;
    }
}
