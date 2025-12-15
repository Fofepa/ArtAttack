package com.artattack;

import java.util.List;

public class TalkFactory implements InteractionFactory {

    private InteractionPanel dialogPanel;
    private List<String> dialog;

    public TalkFactory(InteractionPanel dialogPanel, List<String> dialog){
        if(dialogPanel == null || dialog == null)
            throw new IllegalArgumentException();
        this.dialogPanel = dialogPanel;
        this.dialog = dialog;
    }
    
    @Override
    public Interaction createInteraction(){
        return this.createInteraction(this.dialogPanel, this.dialog);
    }
}
