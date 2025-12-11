package com.artattack;

public class TalkFactory implements InteractionFactory {
    //Primitive method
    @Override
    public Interaction createInteraction(){
        return new Talk();
    }
}
