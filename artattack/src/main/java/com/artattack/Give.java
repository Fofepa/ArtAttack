package com.artattack;

import java.util.List;

public class Give extends Interaction {

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
        player.getInventory().add(this.item);
    }

    public Item item(){
        return this.item;
    }

    public List<String> getDialog(){
        return this.dialog;
    }
}
