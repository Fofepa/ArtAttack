package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

public class RootNode extends Node{
    public RootNode(Player player){
        super(player);
    }

    @Override
    public void setSkill(){
        System.out.println("Root created");
    }
}
