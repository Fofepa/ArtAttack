package com.artattack.interactions;

import java.util.LinkedList;
import java.util.List;

import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public class SwitchMap extends Interaction {

    private boolean unlocked;
    private int key;
    private int nextMap;
    private List<Coordinates> nextCoordinates;
    private boolean isLevelFinish;

    public SwitchMap(int key, int nextMap, List<Coordinates> nextCoordinates){
        this(key, nextMap, nextCoordinates, false);
    }

    public SwitchMap(int key, int nextMap, List<Coordinates> nextCoordinates, boolean isLevelFinish){
        super(InteractionType.SWITCH_MAP);
        this.unlocked = false;
        this.key = key;
        this.nextMap = nextMap;
        this.nextCoordinates = nextCoordinates;
        this.isLevelFinish = isLevelFinish;
    }

    public SwitchMap(int nextMap, List<Coordinates> nextCoordinates){
        this(nextMap, nextCoordinates, false);
    }



    public SwitchMap(int nextMap, List<Coordinates> nextCoordinates, boolean isLevelFinish){
        super(InteractionType.SWITCH_MAP);
        this.unlocked = true;
        this.nextMap = nextMap;
        this.nextCoordinates = nextCoordinates;
        this.isLevelFinish = isLevelFinish;
    }

   /* @Override
    public void doInteraction(GameContext gameContext, Player player){
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().showDialog(dialog);
        }

        Maps next = setNextMap(gameContext, player);

        getMainFrame().switchMap(this.builder.getResult());
    }*/

   /* public MapBuilder getBuilder(){
        return this.builder;
    }

    public List<String> getDialog(){
        return this.dialog;
    }*/

    /*private void setNextMap(Player player){
        List<ActiveElement> list = new LinkedList<ActiveElement>();
        list.add(player);
        player.setCoordinates(this.nextCoordinateses.get(0));
        if(player.equals(this.playerOne)){
            this.builder.setPlayerOne(player);
            this.playerTwo.setCoordinates(this.nextCoordinateses.get(1));
            this.builder.setPlayerTwo(this.playerTwo);
            list.add(this.playerTwo);
            this.builder.setTurnQueue(player, this.playerTwo);
        } else {
            this.builder.setPlayerTwo(player);
            this.playerOne.setCoordinates(this.nextCoordinateses.get(1));
            this.builder.setPlayerOne(this.playerOne);
            list.add(this.playerOne);
            this.builder.setTurnQueue(player, this.playerOne);
        }
        this.builder.setDict();
        this.builder.startMap(); 
    }*/

    @Override
    public void doInteraction(GameContext gameContext, Player player) {
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
            
            List<ActiveElement> list = new LinkedList<ActiveElement>();
            list.add(player);
            player.setCoordinates(this.nextCoordinates.get(0));
            next.getDict().put(this.nextCoordinates.get(0), player);
            
            if(player.equals(currMap.getPlayerOne())){
                next.setPlayerOne(player);
                currMap.getPlayerTwo().setCoordinates(this.nextCoordinates.get(1));
                next.setPlayerTwo(currMap.getPlayerTwo());
                next.setTurnQueue(player, next.getPlayerTwo());
                next.getDict().put(this.nextCoordinates.get(1), next.getPlayerTwo());
                currMap.setCell(currMap.getPlayerTwo().getCoordinates(), '.');
                gameContext.getMapManager().getLevels().get(nextMap).setCell(currMap.getPlayerTwo().getCoordinates(), currMap.getPlayerTwo().getMapSymbol());
                currMap.remove(currMap.getPlayerTwo());
            } else{
                next.setPlayerTwo(player);
                currMap.getPlayerOne().setCoordinates(this.nextCoordinates.get(1));
                next.setPlayerOne(currMap.getPlayerOne());
                next.setTurnQueue(player, next.getPlayerOne());
                next.getDict().put(this.nextCoordinates.get(1), next.getPlayerOne());
                currMap.setCell(currMap.getPlayerOne().getCoordinates(), '.');
                gameContext.getMapManager().getLevels().get(nextMap).setCell(currMap.getPlayerOne().getCoordinates(), currMap.getPlayerOne().getMapSymbol());
                currMap.remove(currMap.getPlayerOne());
            }
            //next.setDict();
            //next.startMap();
            currMap.remove(player);
            currMap.setCell(player.getCoordinates(), '.');
            gameContext.getMapManager().getLevels().get(nextMap).setCell(player.getCoordinates(), player.getMapSymbol());
            
            gameContext.getMapManager().setCurrMap(this.nextMap);

            if (this.isLevelFinish) {
                gameContext.getUiManager().showLevelComplete(next);
            } else {
                gameContext.getUiManager().switchMap(next);
            }
        } else {
            gameContext.getUiManager().showDialog(List.of("Door is locked. You need a key."));
        }
        
    }
}
