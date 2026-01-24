package com.artattack.items;

import com.artattack.mapelements.Player;

public class Item {
    //Attributes
    private ItemType type;
    private String name;
    private String description;
    private int amount;

    //Constructor
    public Item(ItemType type, String name, String description, int amount) {
        this.type = type;
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
        switch(this.type){
            case ATTACK_BUFF: //toDO; break;
            case CURE: player.updateHP(this.getAmount()); break;
            case SPEED_BUFF: player.setSpeed(player.getSpeed() + this.getAmount()); break;
        }
        return this.amount;
    }
}