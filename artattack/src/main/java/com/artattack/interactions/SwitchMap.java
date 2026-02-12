package com.artattack.interactions;

import java.util.LinkedList;
import java.util.List;

import com.artattack.items.Key;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public class SwitchMap extends Interaction {

    private boolean unlocked;
    private int key;
    private int nextMap;
    private boolean isLevelFinish;

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
            List<ActiveElement> list = new LinkedList<ActiveElement>();
            list.add(player);

            Player tmp1 = currMap.getPlayerOne();
            Player tmp2 = currMap.getPlayerTwo();

            currMap.remove(tmp1);
            currMap.remove(tmp2);

            next.setPlayerOne(tmp1);
            next.setPlayerTwo(tmp2);
            next.setTurnQueue(player, (player.equals(next.getPlayerOne()) ? next.getPlayerTwo() : next.getPlayerOne()));
            next.getDict().put(tmp1.getCoordinates(), tmp1);
            next.getDict().put(tmp2.getCoordinates(), tmp2);
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
