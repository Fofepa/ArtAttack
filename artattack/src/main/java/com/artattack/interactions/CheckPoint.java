package com.artattack.interactions;

import com.artattack.view.GameContext;

import java.io.IOException;

import com.artattack.mapelements.Player;

public class CheckPoint extends Interaction {

    public CheckPoint(){
        super(InteractionType.CHECKPOINT);
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player){
        try{
            gameContext.getSaveManager().save(gameContext.getMapManager());
        } catch(IOException e){
            
        }
    }
    
}
