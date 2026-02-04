package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;
import com.artattack.moves.Weapon;

// type of nodes that increases the maxWeapons
public class MAXMVNODE extends Node{
    
    public MAXMVNODE(){
        super(NodeType.MAXMV);
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                for(Weapon weapons : player.getWeapons()){
                    weapons.setMaxMoves();
                }
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
