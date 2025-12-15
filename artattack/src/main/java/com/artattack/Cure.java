package com.artattack;

public class Cure extends Item {

    //Constructor
    public Cure(String name, String description, int amount) {
        super(name, description, amount);
    }

    //Methods
    @Override
    public int use(Player player) {
        int realCure = (player.getCurrHP() + this.getAmount() > player.getMaxHP()) ? player.getMaxHP() - player.getCurrHP() : this.getAmount();
        player.updateHP(this.getAmount());
        return realCure;
    }  
}