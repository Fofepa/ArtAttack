package com.artattack.mapelements;

import java.util.List;
import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.view.GameContext;

public class NPC extends MapElement implements Interactable{
    private int maxInteractions;
    private int currInteraction;
    private List<List<String>> dialogs;
    private List<List<Item>> items;
    private List<List<Key>> keys;
    private String spritePath;

    public NPC(int ID, char mapSymbol, String name, Coordinates coordinates, List<List<String>> dialogs){
        super(ID, mapSymbol, name, coordinates);
        this.dialogs = dialogs;
        this.maxInteractions = dialogs.size();
        this.currInteraction = 0;
    }

    public NPC(int ID, char mapSymbol, String name, Coordinates coordinates, List<List<String>> dialogs, List<List<Item>> items, String spritePath){
        this(ID, mapSymbol, name, coordinates, dialogs);
        this.items = items;
        this.keys = null;
        this.spritePath = spritePath;
    }

    public NPC(int ID, char mapSymbol, String name, Coordinates coordinates, List<List<String>> dialogs, List<List<Key>> keys){
        this(ID, mapSymbol, name, coordinates, dialogs);
        this.items = null;
        this.keys = keys;
    }

    public NPC(int ID, char mapSymbol, String name, Coordinates coordinates, List<List<String>> dialogs, List<List<Item>> items, List<List<Key>> keys, String spritePath){
        this(ID, mapSymbol, name, coordinates, dialogs);
        this.items = items;
        this.keys = keys;
        this.spritePath = spritePath;
    }

    public List<List<String>> getDialogs() {
        return dialogs;
    }

    public List<List<Item>> getItems() {
        return items;
    }

    public List<List<Key>> getKeys() {
        return keys;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public int getMaxInteractions() {
        return maxInteractions;
    }

    public int getCurrInteraction() {
        return currInteraction;
    }

    

    @Override
    public void interact(GameContext gameContext, Player player) {
        gameContext.getUiManager().loadSprite(this.spritePath);
        if(this.currInteraction >= this.maxInteractions)
            gameContext.getUiManager().showDialog(List.of("Good talk!"));
        else{
            gameContext.getUiManager().showDialog(this.dialogs.get(this.currInteraction));
            if(this.items != null && !this.items.isEmpty()){
                List<Item> currItem = this.items.get(this.currInteraction);
                if(currItem != null && !currItem.isEmpty())
                    player.addItems(currItem);
            }
            if(this.keys != null && !this.keys.isEmpty()){
                List<Key> currKey = this.keys.get(this.currInteraction);
                if(currKey != null && !currKey.isEmpty())
                    player.addKeys(currKey);
            }
            this.currInteraction++;
        }
    }
}

