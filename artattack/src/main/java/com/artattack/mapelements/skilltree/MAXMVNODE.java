package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxWeapons
public class MAXMVNODE extends Node{
    
    public MAXMVNODE(){
        super(NodeType.MAXMV);
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.getWeapons().get(0).setInitMoves();
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
