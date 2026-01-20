package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxHP
public class  HPNODE extends Node{
    int newHP;
    
    public HPNODE(Player player, int newHP){
        super(player);
        this.newHP = newHP;
        
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxHP(this.getPlayer().getMaxHP()+ newHP);
        this.setSpent();
    }
}
