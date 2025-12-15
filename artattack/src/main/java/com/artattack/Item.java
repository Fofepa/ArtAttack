package com.artattack;

public abstract class Item {
    //Attributes
    private String name;
    private String description;
    private int amount;

    //Constructor
    public Item(String name, String description, int amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getAmount() {
        return this.amount;
    }

    //Methods
    public int use(Player player){
        return 0;
    }
}