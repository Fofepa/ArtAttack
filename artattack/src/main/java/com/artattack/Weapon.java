package com.artattack;

import java.util.List;

public class Weapon extends Item {
    //Attributes
    private List<Move> moves;
    private Characters characterClass;

    //Getters
    public List<Move> getMoves() {
        return moves;
    }

    public Characters getCharacterClass() {
        return characterClass;
    }

    //Methods
    @Override
    public int use() {
        return 0;
    }
}