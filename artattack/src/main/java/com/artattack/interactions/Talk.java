package com.artattack.interactions;

import java.util.List;

import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public class Talk extends Interaction {

    private List<String> dialog;
    
    public Talk(List<String> dialog){
        super(InteractionType.TALK);
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player, String spritePath){
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().showDialog(dialog, spritePath);
        }
    }

    public List<String> getDialog() {
        return dialog;
    }
}