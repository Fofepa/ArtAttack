package com.artattack;

import java.util.*;

public class GiveFactory implements InteractionFactory {


    @Override
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog, Item item){
        if(dialogPanel == null || dialog == null || item == null)
            throw new IllegalArgumentException();
        return new Give(dialogPanel, dialog, item);
    }

    @Override
    public Interaction createInteraction(InteractionPanel dialogPanel, List<String> dialog){
        throw new UnsupportedOperationException();
    }

}
