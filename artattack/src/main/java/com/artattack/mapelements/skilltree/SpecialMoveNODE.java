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
        this.getPlayer().setMaxWeapons();
        Weapon specialWeapon = new Weapon("Special", "You got it with tears", 0);
        specialWeapon.addMove(specialMove);
        this.getPlayer().getWeapons().add(specialWeapon);
        this.setSpent();
    }
}