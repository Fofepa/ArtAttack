package com.artattack.mapelements;

import java.util.List;
import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.view.GameContext;

public class Chest extends MapElement implements Interactable{
    private List<String> dialog;
    private List<Item> items;
    private List<Key> keys;
    private String spritePath;
    private boolean opened;

    public Chest(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID, mapSymbol, name, coordinates);
    }

    public Chest(int ID, char mapSymbol, String name, Coordinates coordinates, List<String> dialog, List<Item> items, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.dialog = dialog;
        this.items = items;
        this.keys = null;
        this.spritePath = spritePath;
        this.opened = false;
    }

    public Chest(int ID, char mapSymbol, String name, Coordinates coordinates, List<String> dialog, List<Key> keys){
        this(ID, mapSymbol, name, coordinates);
        this.dialog = dialog;
        this.items = null;
        this.keys = keys;
        this.opened = false;
    }

    public Chest(int ID, char mapSymbol, String name, Coordinates coordinates, List<String> dialog, List<Item> items, List<Key> keys, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.dialog = dialog;
        this.items = items;
        this.keys = keys;
        this.spritePath = spritePath;
        this.opened = false;
    }

    public List<String> getDialog(){
        return this.dialog;
    }

    public List<Item> getItems(){
        return this.items;
    }

    public List<Key> getKeys(){
        return this.keys;
    }

    public String getSpritePath(){
        return this.spritePath;
    }

    private boolean isOpened(){
        return this.opened;
    }

    @Override
    public void interact(GameContext gameContext, Player player) {
        if(opened)
            gameContext.getUiManager().showDialog(List.of("Chest has already been opened."));
        else{
            gameContext.getUiManager().showDialog(this.dialog);
            player.addItems(this.items);
            player.addKeys(this.keys);
            this.opened = true;
        }        
    }

    
}
