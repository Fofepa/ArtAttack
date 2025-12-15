package com.artattack;

public class SpeedBuff extends Item {

    //Constructor
    public SpeedBuff(String name, String description, int amount) {
        super(name, description, amount);
    }

    //Methods
    @Override
    public int use(Player player) {
        player.setSpeed(player.getSpeed() + this.getAmount());
        return this.getAmount();
    }
}