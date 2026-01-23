package com.artattack.mapelements;

import com.artattack.level.Coordinates;
import com.artattack.items.Key;
import com.artattack.level.Maps;
import com.artattack.view.GameContext;

import java.util.LinkedList;
import java.util.List;

public class Door extends MapElement implements Interactable{
    private boolean unloked;
    private int key;
    private int nextMap;
    private List<Coordinates> nextCoordinates;
    private String spritePath;

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID, mapSymbol, name, coordinates);
    }

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, int key, int nextMap, List<Coordinates> nextCoordinates, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = false;
        this.key = key;
        this.nextMap = nextMap;
        this.nextCoordinates = nextCoordinates;
        this.spritePath = spritePath;
    }



    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, int nextMap, List<Coordinates> nextCoordinates, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = true;
        this.nextMap = nextMap;
        this.nextCoordinates = nextCoordinates;
        this.spritePath = spritePath;
    }

    public boolean isUnloked() {
        return unloked;
    }

    public int getKey() {
        return key;
    }

    public int getNextMap() {
        return nextMap;
    }

    public String getSpritePath(){
        return this.spritePath;
    }

    public List<Coordinates> getNexCoordinates(){
       return  this.nextCoordinates;
    }

    @Override
    public void interact(GameContext gameContext, Player player) {
        if(!this.unloked){
            if(player.getKeys() != null && !player.getKeys().isEmpty()){
                for(Key k : player.getKeys()){
                    if(k.getID() == key){
                        this.unloked = true;
                        break;
                    }
                }
            }
        }
        if(this.unloked){
            Maps currMap = gameContext.getMapManager().getLevels().get(gameContext.getMapManager().getCurrMap());
            Maps next = gameContext.getMapManager().getLevels().get(this.nextMap);
            List<ActiveElement> list = new LinkedList<ActiveElement>();
            list.add(player);
            player.setCoordinates(this.nextCoordinates.get(0));
            if(player.equals(currMap.getPlayerOne())){
                next.setPlayerOne(player);
                currMap.getPlayerTwo().setCoordinates(this.nextCoordinates.get(1));
                next.setPlayerTwo(currMap.getPlayerTwo());
                next.setTurnQueue(player, next.getPlayerTwo());
            } else{
                next.setPlayerTwo(player);
                currMap.getPlayerOne().setCoordinates(this.nextCoordinates.get(1));
                next.setPlayerOne(currMap.getPlayerOne());
                next.setTurnQueue(player, next.getPlayerOne());
            }
            //next.setDict();
            //next.startMap(null);
            gameContext.getUiManager().switchMap(next);
            gameContext.getMapManager().setCurrMap(this.nextMap);
        } else {
            gameContext.getUiManager().showDialog(List.of("Door is locked. You need a key."));
        }
        
    }

    
    
}
