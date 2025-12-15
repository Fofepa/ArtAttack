package com.artattack;

public class SpeedBuff extends Item {
    //Attributes
    private int buffAmount;

    //Constructor
    public SpeedBuff(String name, String description, int buffAmount) {
        super(name, description);
        this.buffAmount = buffAmount;
    }
    //Getters

    public int getBuffAmount() {
        return this.buffAmount;
    }

    //Methods
    @Override
    public int use() {
        return 0;
    }
}