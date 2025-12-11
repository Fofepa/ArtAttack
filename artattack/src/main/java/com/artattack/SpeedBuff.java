package com.artattack;

public class SpeedBuff extends Item {
    //Attributes
    private int ID;
    private int buffAmount;

    //Getters
    public int getID() {
        return ID;
    }

    public int getBuffAmount() {
        return buffAmount;
    }

    //Methods
    @Override
    public int use() {
        return 0;
    }
}