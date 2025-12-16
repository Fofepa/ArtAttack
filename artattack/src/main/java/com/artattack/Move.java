package com.artattack;

import java.util.ArrayList;
import java.util.List;

public class Move {
    //Attributes
    private String name;
    private String description;
    private int power;
    private List<Coordinates> attackArea;

    //Constructor
    public Move(String name, String description, int power, List<Coordinates> attackArea) {
        this.name = name;
        this.description = description;
        this.power = power;
        this.attackArea = attackArea;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPower() {
        return this.power;
    }

    public List<Coordinates> getAttackArea() {
        return this.attackArea;
    }

    //Methods
    public int attack(MapElement attacker, Maps map) {
        List<MapElement> victims = new ArrayList<MapElement>();
        for (Coordinates attackCell : this.attackArea) {
            MapElement check = map.getDict().get(Coordinates.sum(attacker.getCoordinates(), attackCell));
            if (attacker instanceof Player)
                if (check instanceof Enemy enemy)
                    victims.add(enemy);
            else if (attacker instanceof Enemy)
                if (check instanceof Player player)
                    victims.add(player);
        }
        for (MapElement element : victims) {
            switch (element) {
                case Enemy e -> {
                    e.updateHP(- power /*-variabile globale*/);
                }
                case Player p -> {  
                    p.updateHP(- power /*-variabile globale*/);
                }
                default -> {
                }
            }
        }
        return this.power;
    }
}
