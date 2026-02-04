package com.artattack.interactions;

import java.util.List;

import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public class Talk extends Interaction {

    private List<String> dialog;
    
    public Talk(List<String> dialog){
        super(InteractionType.TALK);
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player){
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().showDialog(dialog);
        }
    }

    public List<String> getDialog() {
        return dialog;
    }
}