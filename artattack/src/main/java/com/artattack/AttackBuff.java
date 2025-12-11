package com.artattack;

public class AttackBuff extends Item {
    //Attributes
    private int ID;
    private int buffAmount;

    //Constructor
    public AttackBuff(String name, String description, int ID, int buffAmount) {
        super(name, description);
        this.ID = ID;
        this.buffAmount = buffAmount;
    }

    //Getters
    public int getID() {
        return this.ID;
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