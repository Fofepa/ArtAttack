package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public class Give extends Interaction {

    private List<String> dialog;
    private List<Item> item;

    public Give(List<String> dialog, List<Item> item){
        //super(mainFrame);
        this.dialog = dialog;
        this.item = item;
    }

    @Override
    public void doInteraction(GameContext gameContext, Player player){
        player.addItems(this.item);;
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().showDialog(dialog);
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