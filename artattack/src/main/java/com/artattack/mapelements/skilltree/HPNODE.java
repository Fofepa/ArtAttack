package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxHP
public class  HPNODE extends Node{
    int newHP;
    
    public HPNODE(int newHP){
        super(NodeType.HP);
        this.newHP = newHP;
        
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.setMaxHP(player.getMaxHP()+ newHP);
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }

    public int getNewHP() {
        return newHP;
    }

    
}
