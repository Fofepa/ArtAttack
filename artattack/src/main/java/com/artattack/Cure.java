package com.artattack;

public class Cure extends Item {
    //Attributes
    private int healAmount;

    //Constructor
    public SpeedBuff(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = buffAmount;
    }

    //Getters
    public int getHealAmount() {
        return this.healAmount;
    }

    //Methods
    @Override
    public int use() {
        return 0;
    }  
}
