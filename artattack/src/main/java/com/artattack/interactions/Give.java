package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.mapelements.Player;
import com.artattack.moves.Weapon;
import com.artattack.view.GameContext;

public class Give extends Interaction {

    private List<String> dialog;
    private List<Item> item;
    private Weapon wpn;
    private Key key;

    public Give(List<String> dialog, List<Item> item){
        super(InteractionType.GIVE);
        this.dialog = dialog;
        this.item = item;
    }

    public Give(List<String> dialog, Weapon wpn) {
        super(InteractionType.GIVE);
        this.dialog = dialog;
        this.wpn = wpn;
    }

    public Give(List<String> dialog, Key key) {
        super(InteractionType.GIVE);
        this.dialog = dialog;
        this.key = key;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player, String spritePath){
        if (this.item != null && !this.item.isEmpty()) {
            player.addItems(this.item);
        }
        if (this.wpn != null) {
            if(wpn.getCompatibility() == player.getType()){
                player.getWeapons().add(this.wpn);
            }
            else{
                gameContext.getUiManager().showDialog(List.of(player.getName() + "can't take this!"),spritePath);
            }
            
        }
        if (this.key != null) {
            player.getKeys().add(this.key);
        }
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().showDialog(dialog, spritePath);
            gameContext.getUiManager().repaintInventoryPanel();
        }
    }

    public List<Item> item(){
        return this.item;
    }

    public List<String> getDialog(){
        return this.dialog;
    }
}