package com.artattack;

import java.util.*;

public class GiveFactory implements InteractionFactory {
    private InteractionPanel dialogPanel;
    private List<String> dialog;
    private Item item;

    public GiveFactory(InteractionPanel dialogPanel, List<String> dialog, Item item){
        if(dialogPanle == null || dialog == null !! item == null)
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
