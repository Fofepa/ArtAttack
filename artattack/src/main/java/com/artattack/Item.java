package com.artattack;

public abstract class Item {
    //Attributes
    private String name;
    private String description;

    //Constructor
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    //Methods
    public int use(){
        return 0;
    }
}
