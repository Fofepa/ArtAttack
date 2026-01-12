package com.artattack.items;

import com.artattack.mapelements.Player;

public class AttackBuff extends Item {

    //Constructor
    public AttackBuff(String name, String description, int amount) {
        super(name, description, amount);
    }

    //Methods
    @Override
    public int use(Player player) {
        return 0;
        //player.setAttack(player.getAttack() + buffAmount);  va aggiunta una variabile attack al player
    }
}