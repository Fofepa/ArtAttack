package com.artattack;

import java.util.List;

import com.artattack.view.InteractionPanel;

public class Give implements Interaction {

    private InteractionPanel dialogPanel;
    private List<String> dialog;
    private Item item;

    public Give(InteractionPanel dialogPanel, List<String> dialog, Item item){
        this.dialogPanel = dialogPanel;
        this.dialog = dialog;
        this.item = item;
    }

    @Override
    public void doInteraction(Player player){
        this.dialogPanel.showDialog(dialog);
        player.addItem(item);
    }

    public Item item(){
        return this.item;
    }

    public List<String> getDialog(){
        return this.dialog;
    }
}
