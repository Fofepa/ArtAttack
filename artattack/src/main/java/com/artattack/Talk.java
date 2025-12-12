package com.artattack;

import com.googlecode.lanterna.gui2.TextBox;

public class Talk extends Interaction {

    //Attribute
    private TextBox dialogBox;
    private String dialog;
    
    //Constructor
    public Talk(TextBox dialogBox, String dialog){
        super(dialogBox, dialog);
    }

    //Primitive method
    @Override
    public void doAction(Player player){
        this.dialogBox.addLine(dialog);
    }

    //Setter
    public void setDialog(String dialog){
        this.dialog = dialog;
    }
}
