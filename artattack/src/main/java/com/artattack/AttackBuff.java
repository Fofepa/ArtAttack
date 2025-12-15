package com.artattack;

public class AttackBuff extends Item {
    //Attributes
    private int buffAmount;

    //Constructor
    public AttackBuff(String name, String description, int buffAmount) {
        super(name, description);
        this.buffAmount = buffAmount;
    }

    public int getBuffAmount() {
        return this.buffAmount;
    }

    //Methods
    @Override
    public int use() {
        return 0;
    }
}