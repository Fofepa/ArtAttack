package com.artattack.interactions;

import java.io.IOException;
import java.util.List;

import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public class CheckPoint extends Interaction {
    List<String> dialog;

    public CheckPoint(List<String> dialog){
        super(InteractionType.CHECKPOINT);
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player, String spritePath){
        gameContext.getUiManager().showDialog(this.dialog, spritePath);
        try{
            gameContext.getSaveManager().save(gameContext.getMapManager());
        } catch(IOException e){

        }
    }
    
}
