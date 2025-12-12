package com.artattack;

public abstract class Item {
    private String name;
    private String description;
    private int healAmount;

    public int use(){
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHealAmount() {
        return healAmount;
    }

}
