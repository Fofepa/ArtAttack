package com.artattack;

import com.googlecode.lanterna.gui2.TextBox;

public class Give extends Interaction {

    //Attributes
    private TextBox dialogBox;
    private String dialog;
    private Item item;

    //Constructor
    public Give(TextBox dialogBox, String dialog, Item item){
        this.dialogBox = dialogBox;
        this.dialog = dialog;
        this.item = item;
    }

    //Primitive method
    @Override
    public void doAction(Player player){
        /*this.dialogBox.addLine(dialog);
        player.getInventory().add(this.item);*/ //usare List<? super Item> in player e fare il cast in un metodo useItem del player
    }

    //Getter
    public Item item(){
        return this.item;
    }

    public String getDialog(){
        return this.dialog;
    }
}
