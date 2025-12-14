package com.artattack;

import java.util.List;

public class TalkFactory implements InteractionFactory {

    @Override
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog, Item item){
        if(dialogPanel == null || dialog == null ||  item != null)
            throw new IllegalArgumentException();
        return new Talk(dialogPanel, dialog);
    }
    
    @Override
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog){
        return this.createInteraction(dialogPanel, dialog, null);
    }
}
