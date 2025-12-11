package com.artattack;

public class Key extends Item {
    //Attributes
    private int ID;

    //Constructor
    public Key(String name, String description, int ID) {
        super(name, description);
        this.ID = ID;
    }

    //Getters
    public int getID() {
        return this.ID;
    }
}