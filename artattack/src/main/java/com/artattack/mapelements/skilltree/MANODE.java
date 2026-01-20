package com.artattack.mapelements.skilltree;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.mapelements.Player;

// type of nodes that creates a new movementArea
public class  MANODE extends Node{
    private List<Coordinates> shape;    // piece of area to add
    
    public MANODE(Player player, List<Coordinates> shape){
        super(player);
        this.shape = shape;
    }

    @Override
    public void setSkill(){
        this.getPlayer().getMoveArea().addAll(shape);
        this.setSpent();
    }
}