package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxAP
public class  APNODE extends Node{
    int newAP;
    
    public APNODE(Player player, int newAP){
        super(player, NodeType.AP);
        this.newAP = newAP;
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                this.getPlayer().setMaxActionPoints(this.getPlayer().getMaxActionPoints()+ newAP);
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
