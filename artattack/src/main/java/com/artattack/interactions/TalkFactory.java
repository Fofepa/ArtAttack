package com.artattack.interactions;

import java.util.List;

public class TalkFactory implements InteractionFactory {

    private List<String> dialog;

    public TalkFactory(List<String> dialog){
        if(dialog == null)
            throw new IllegalArgumentException();
        this.dialog = dialog;
    }
    
    @Override
    public Interaction createInteraction(){
        return new Talk(null, this.dialog);
    }
}