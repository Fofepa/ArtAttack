package com.artattack;

public class GiveFactory implements InteractionFactory {
    //Primitive method
    @Override
    public Interaction createInteraction(){
        return new Give();
    }
}
