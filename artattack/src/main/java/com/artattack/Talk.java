package com.artattack;

public class Talk implements Interaction {

    //Attribute
    private String dialog;
    
    //Constructor
    public Talk(){

    }

    //Primitive method
    @Override
    public void doAction(Player p){

    }

    //Getter
    public String getDialog(){
        return this.dialog;
    }

    //Setter
    public void setDialog(String dialog){
        this.dialog = dialog;
    }
}
