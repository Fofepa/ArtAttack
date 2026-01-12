package com.artattack.moves;

import java.util.ArrayList;
import java.util.List;

public class Weapon{
    //Attributes
    private final String name;
    private final String description;
    private List<Move> moves;
    private int compatibility;

    //Constructors
    public Weapon(String name, String description, int compatibility) {
        this.name = name;
        this.description = description;
        this.moves = new ArrayList<>();
        this.compatibility = compatibility;
    }

    public Weapon(String name, String description, List<Move> moves, int compatibility) {
        this.name = name;
        this.description = description;
        this.moves = moves;
        this.compatibility = compatibility;
    }

    //Getters
    public List<Move> getMoves() {
        return this.moves;
    }

    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }

    public int getCompatibility() {
        return this.compatibility;
    }

    //Methods
    public boolean addMove(Move move) {
        if (this.moves.contains(move)) {
            return false;
        }
        moves.add(move);
        return true;
    }
}
