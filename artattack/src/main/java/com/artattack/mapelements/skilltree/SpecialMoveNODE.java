package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

// type of nodes that creates a new SpecialMove
public class SpecialMoveNODE extends Node{
    private Move specialMove;

    public SpecialMoveNODE(Player player, Move specialMove){
        super(player);
        this.specialMove = specialMove;
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                Weapon specialWeapon = new Weapon("Special", "You got it with tears", 0);
                if(!this.getPlayer().getWeapons().contains(specialWeapon)){
                    specialWeapon.addMove(specialMove);
                    this.getPlayer().setMaxWeapons();
                    this.getPlayer().getWeapons().add(specialWeapon);
                    this.setSpent();
                }
                else{
                    this.getPlayer().getWeapons().get(this.getPlayer().getWeapons().indexOf(specialWeapon)).addMove(specialMove);
                }
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}