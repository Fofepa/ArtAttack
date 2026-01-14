package com.artattack.interactions;

import java.util.List;

import com.artattack.items.Item;

public class GiveFactory implements InteractionFactory {
    private List<String> dialog;
    private Item item;

    public GiveFactory(List<String> dialog, Item item){
        if(dialog == null || item == null)
            throw new IllegalArgumentException();
        this.dialog = dialog;
        this.item = item;
    }

    @Override
    public Interaction createInteraction(){
        return new Give(null, this.dialog, this.item);
    }
}