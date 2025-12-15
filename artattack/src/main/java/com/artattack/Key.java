package com.artattack;

public class Key{
    //Attributes
    private final String name;
    private final String description;
    private final int ID;

    //Constructor
    public Key(String name, String description, int ID) {
        this.name = name;
        this.description = description;
        this.ID = ID;
    }

    //Getters
    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

     public String getDescription() {
        return this.description;
    }
}