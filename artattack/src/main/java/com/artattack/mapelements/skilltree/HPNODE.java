package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxHP
public class  HPNODE extends Node{
    int newHP;
    
    public HPNODE(Player player, int newHP){
        super(player, NodeType.HP);
        this.newHP = newHP;
        
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                this.getPlayer().setMaxHP(this.getPlayer().getMaxHP()+ newHP);
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
