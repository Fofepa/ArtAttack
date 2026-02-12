package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public class SwitchMap extends Interaction {

    private boolean unlocked;
    private int key;
    private int nextMap;
    private boolean isLevelFinish;
    private Coordinates p1Overwrite, p2Overwrite;

    public SwitchMap(int key, int nextMap){
        this(key, nextMap, false);
    }

    public SwitchMap(int key, int nextMap, boolean isLevelFinish){
        super(InteractionType.SWITCH_MAP);
        this.unlocked = false;
        this.key = key;
        this.nextMap = nextMap;
        this.isLevelFinish = isLevelFinish;
    }

    public SwitchMap(int nextMap){
        this(nextMap, false);
    }

    public SwitchMap(int nextMap, boolean isLevelFinish){
        super(InteractionType.SWITCH_MAP);
        this.unlocked = true;
        this.nextMap = nextMap;
        this.isLevelFinish = isLevelFinish;
    }

    public void setOverwrite(Coordinates p1Overwrite, Coordinates p2Overwrite) {
        this.p1Overwrite = p1Overwrite;
        this.p2Overwrite = p2Overwrite;
    }

    public Coordinates getP1Overwrite() {
        return this.p1Overwrite;
    }

    public Coordinates getP2Overwrite() {
        return this.p2Overwrite;
    }
    
    @Override
    public void doInteraction(GameContext gameContext, Player player, String spritePath) {
        if(!this.unlocked){
            if(player.getKeys() != null && !player.getKeys().isEmpty()){
                for(Key k : player.getKeys()){
                    if(k.getID() == key){
                        this.unlocked = true;
                        break;
                    }
                }
            }
        }

        if(this.unlocked) {
            System.out.println("SwitchMap.doInteraction called");
            Maps currMap = gameContext.getMapManager().getLevels().get(gameContext.getMapManager().getCurrMap());
            Maps next = gameContext.getMapManager().getLevels().get(this.nextMap);
            if (next == null) {
                return;
            }
            Player tmp1 = currMap.getPlayerOne();
            Player tmp2 = currMap.getPlayerTwo();
            currMap.remove(tmp1);
            currMap.remove(tmp2);
            next.setPlayerOne(tmp1);
            next.setPlayerTwo(tmp2);
            if (this.p1Overwrite != null) {
                next.getPlayerOne().setCoordinates(this.p1Overwrite);
            }
            if (this.p2Overwrite != null) {
                next.getPlayerTwo().setCoordinates(this.p2Overwrite);
            }
            next.setDict();
            next.setTurnQueue(player, (player.equals(next.getPlayerOne()) ? next.getPlayerTwo() : next.getPlayerOne()));
            next.startMap();
            gameContext.getUiManager().switchMap(next);
            gameContext.getMapManager().setCurrMap(this.nextMap);
            if (this.isLevelFinish) {
                gameContext.getUiManager().showLevelComplete(next);
            } else {
                gameContext.getUiManager().switchMap(next);
            }
        } else {
            gameContext.getUiManager().showDialog(List.of("Door is locked. You need a key."), spritePath);
        }
        
    }

}
