package com.artattack;

public class Give implements Interaction {

    //Attributes
    private String dialog;
    private Item item;

    //Constructor
    public Give(String dialog, Item item){

    }

    //Primitive method
    @Override
    public void doActiion(Player p){

    }

    //Getter
    public Item item(){
        return this.item;
    }

    public String getDialog(){
        return this.dialog;
    }

    //Setter
    public void setDialog(String dialog){
        this.dialog = dialog;
    }
    
    public void setItem(Item item){
        this.item = item;
    }
}
