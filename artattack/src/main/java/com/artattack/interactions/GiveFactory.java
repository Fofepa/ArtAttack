package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.view.InteractionPanel;

public class GiveFactory implements InteractionFactory {
    private InteractionPanel dialogPanel;
    private List<String> dialog;
    private Item item;

    public GiveFactory(InteractionPanel dialogPanel, List<String> dialog, Item item){
        if(dialogPanel == null || dialog == null || item == null)
            throw new IllegalArgumentException();
        this.dialogPanel = dialogPanel;
        this.dialog = dialog;
        this.item = item;
    }

    @Override
    public Interaction createInteraction(){
        return new Give(this.dialogPanel, this.dialog, this.item);
    }
}
