package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;
import com.artattack.moves.Weapon;

// type of nodes that increases the maxWeapons
public class MAXMVNODE extends Node{
    
    public MAXMVNODE(Player player){
        super(player);
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                for(Weapon weapons : this.getPlayer().getWeapons()){
                    weapons.setMaxMoves();
                }
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
