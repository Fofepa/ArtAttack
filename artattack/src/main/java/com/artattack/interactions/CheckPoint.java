package com.artattack.interactions;

import com.artattack.view.GameContext;

import java.io.IOException;
import java.util.List;

import com.artattack.mapelements.Player;

public class CheckPoint extends Interaction {
    List<String> dialog;

    public CheckPoint(List<String> dialog){
        super(InteractionType.CHECKPOINT);
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player){
        gameContext.getUiManager().showDialog(this.dialog);
        try{
            gameContext.getSaveManager().save(gameContext.getMapManager());
        } catch(IOException e){

        }
    }
    
}
