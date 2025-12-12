package com.artattack;

import com.googlecode.lanterna.gui2.TextBox;

public abstract class Interaction {
    private TextBox dialogBox;
    private String dialog;

    public Interaction(TextBox dialogBox, String dialog){
        this.dialogBox = dialogBox;
        this.dialog = dialog;
    }

    public abstract void doAction(Player player);


    //getter
    public TextBox getDialogBox(){
        return this.dialogBox;
    }

    public String getDialog(){
        return this.dialog;
    }
}
