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

        if(this.unlocked){
            Maps currMap = gameContext.getMapManager().getLevels().get(gameContext.getMapManager().getCurrMap());
            Maps next = gameContext.getMapManager().getLevels().get(this.nextMap);
            if (next == null) {
                return;
            }
            List<ActiveElement> list = new LinkedList<ActiveElement>();
            list.add(player);
            player.setCoordinates(next.getP1Spawn());
            next.getDict().put(next.getP1Spawn(), player);
            
            if(player.equals(currMap.getPlayerOne())){
                next.setPlayerOne(player);
                currMap.getPlayerTwo().setCoordinates(next.getP2Spawn());
                next.setPlayerTwo(currMap.getPlayerTwo());
                next.setTurnQueue(player, next.getPlayerTwo());
                next.getDict().put(next.getP2Spawn(), next.getPlayerTwo());
                currMap.setCell(currMap.getPlayerTwo().getCoordinates(), '.');
                gameContext.getMapManager().getLevels().get(nextMap).setCell(currMap.getPlayerTwo().getCoordinates(), currMap.getPlayerTwo().getMapSymbol());
                currMap.remove(currMap.getPlayerTwo());
            } else{
                next.setPlayerTwo(player);
                currMap.getPlayerOne().setCoordinates(next.getP2Spawn());
                next.setPlayerOne(currMap.getPlayerOne());
                next.setTurnQueue(player, next.getPlayerOne());
                next.getDict().put(next.getP2Spawn(), next.getPlayerOne());
                currMap.setCell(currMap.getPlayerOne().getCoordinates(), '.');
                gameContext.getMapManager().getLevels().get(nextMap).setCell(currMap.getPlayerOne().getCoordinates(), currMap.getPlayerOne().getMapSymbol());
                currMap.remove(currMap.getPlayerOne());
            }
            //next.setDict();
            //next.startMap();
            currMap.remove(player);
            currMap.setCell(player.getCoordinates(), '.');
            gameContext.getMapManager().getLevels().get(nextMap).setCell(player.getCoordinates(), player.getMapSymbol());
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
