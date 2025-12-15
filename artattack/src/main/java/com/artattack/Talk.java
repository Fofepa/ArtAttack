package com.artattack;

import java.util.List;


public class Talk extends Interaction {

    private InteractionPanel dialogPanel;
    private List<String> dialog;
    
    public Talk(InteractionPanel dialogPanle, List<String> dialog){
        this.dialogPanel = dialogPanel;
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(Player player){
        this.dialogPanel.showDialog(dialog);
    }
}
