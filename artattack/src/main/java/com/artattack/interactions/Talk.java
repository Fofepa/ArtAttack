package com.artattack.interactions;

import java.util.List;

import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class Talk extends Interaction {

    private List<String> dialog;
    
    public Talk(MainFrame mainFrame, List<String> dialog){
        super(mainFrame);
        this.dialog = dialog;
    }

    @Override
    public void doInteraction(Player player){
        if (getMainFrame() != null) {
            getMainFrame().showDialog(dialog);
        }
    }

    public List<String> getDialog() {
        return dialog;
    }
}