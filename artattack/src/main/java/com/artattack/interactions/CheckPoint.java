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
        Player player1 = gameContext.getMapManager().getLevels().get((gameContext.getMapManager().getCurrMap())).getPlayerOne();
        Player player2 = gameContext.getMapManager().getLevels().get((gameContext.getMapManager().getCurrMap())).getPlayerTwo();

        
        
        if((!player1.isAlive() || !player2.isAlive()) 
            && gameContext.getMapManager().getLevels().get((gameContext.getMapManager().getCurrMap())).getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().size() > 1){
                gameContext.getUiManager().showDialog(List.of("I can't help you right now, the enemies are close!"), "/images/aretha.jpg");
        } else{
            if(!player1.isAlive()){
                gameContext.getMapManager().getLevels().get((gameContext.getMapManager().getCurrMap())).getConcreteTurnHandler().getConcreteTurnQueue().add(player1);
                gameContext.getUiManager().repaintTurnOrderPanel();
            }
            if(!player2.isAlive()){
                gameContext.getMapManager().getLevels().get((gameContext.getMapManager().getCurrMap())).getConcreteTurnHandler().getConcreteTurnQueue().add(player2);
                gameContext.getUiManager().repaintTurnOrderPanel();
            }
            
            player1.setCurrHP(player1.getMaxHP());
            player2.setCurrHP(player2.getMaxHP());
        }
        try{
            gameContext.getSaveManager().save(gameContext.getMapManager());
        } catch(IOException e){

        }
    }
    
}
