package com.artattack.interactions;

import java.util.List;

import com.artattack.view.InteractionPanel;

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
        return new Talk(this.dialogPanel, this.dialog);
    }
}
