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
    private boolean empty = false;

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
        if(!empty){ 
            if (this.item != null && !this.item.isEmpty()) {
                this.empty = true;
                player.addItems(this.item);
            }
            if (this.wpn != null) {
                if(wpn.getCompatibility() == player.getType()){
                    player.getWeapons().add(this.wpn);
                    this.empty = true;
                    gameContext.getUiManager().repaintWeaponsPanel();
                }
                else{
                    gameContext.getUiManager().showDialog(List.of(player.getName() + " couldn't find nothing useful"),spritePath);
                    return;
                }
                
            }
            if (this.key != null) {
                player.getKeys().add(this.key);
                this.empty = true;
            }
            if (gameContext.getUiManager() != null) {
                gameContext.getUiManager().showDialog(dialog, spritePath);
                gameContext.getUiManager().repaintInventoryPanel();
            }
        }
        else{
            gameContext.getUiManager().showDialog(List.of("This chest is empty"),spritePath);
        }
    }

    public List<Item> item(){
        return this.item;
    }

    public List<String> getDialog(){
        return this.dialog;
    }
}