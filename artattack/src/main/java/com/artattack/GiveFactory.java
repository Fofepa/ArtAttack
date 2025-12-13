package com.artattack;

import com.googlecode.lanterna.gui2.TextBox;

public class GiveFactory implements InteractionFactory {
    //Primitive method
    @Override
    public Interaction createInteraction(Object... args){
        //Need to add check before casting
        TextBox dialogBox = (TextBox) args[0];
        String dialog = (String) args[1];
        Item item = (Item) args[2];
        return new Give(dialogBox, dialog, item);
    }
}
