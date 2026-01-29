package com.artattack.moves;

import java.util.ArrayList;
import java.util.List;

import com.artattack.mapelements.PlayerType;

public class Weapon{
    //Attributes
    private final String name;
    private final String description;
    private int maxMoves;
    private List<Move> moves;
    private PlayerType compatibility;

    //Constructors
    public Weapon(String name, String description, int maxMoves, PlayerType compatibility) {
        this.name = name;
        this.description = description;
        this.maxMoves = maxMoves;
        this.moves = new ArrayList<>();
        this.compatibility = compatibility;
    }

    public Weapon(String name, String description, int maxMoves, List<Move> moves, PlayerType compatibility) {
        this.name = name;
        this.description = description;
        this.maxMoves = maxMoves;
        if (moves.size() <= this.maxMoves) {
            this.moves = moves;
        }
        this.compatibility = compatibility;
    }

    //Getters
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }

    public int getMaxMoves() {
        return this.maxMoves;
    }
    
    public List<Move> getMoves() {
        return this.moves;
    }

    public PlayerType getCompatibility() {
        return this.compatibility;
    }

    //Setters
    public void setMaxMoves() {
        this.maxMoves++;
    }

    //Methods
    public boolean addMove(Move move) {
        if (this.moves.contains(move) || this.moves.size() >= this.maxMoves) {
            return false;
        }
        moves.add(move);
        return true;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Weapon weapon = (Weapon) obj;
        return this.getName().equals(weapon.getName());
    }
}
