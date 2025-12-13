package com.artattack;

import com.googlecode.lanterna.gui2.TextBox;

public class TalkFactory implements InteractionFactory {
    //Primitive method
    @Override
    public Interaction createInteraction(Object... args){
        //Need to add check before casting
        TextBox dialogBox = (TextBox) args[0];
        String dialog = (String) args[1];
        return new Talk(dialogBox, dialog);
    }
}
