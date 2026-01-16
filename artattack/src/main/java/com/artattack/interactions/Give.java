package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class Give extends Interaction {

    private List<String> dialog;
    private Item item;

    public Give(MainFrame mainFrame, List<String> dialog, Item item){
        super(mainFrame);
        this.dialog = dialog;
        this.item = item;
    }

    @Override
    public void doInteraction(Player player){
        if (getMainFrame() != null) {
            getMainFrame().showDialog(dialog);
        }
        player.addItem(item);
    }

    public Item item(){
        return this.item;
    }

    public List<String> getDialog(){
        return this.dialog;
    }
}