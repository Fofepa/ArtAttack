package com.artattack;

import java.util.List;

public class Move {
    //Attributes
    private String name;
    private String description;
    private int power;
    private Coordinates[] attackArea;

    //Constructor
    public Move(String name, String description, int power, Coordinates[] attackArea) {
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

    public Coordinates[] getAttackArea() {
        return this.attackArea;
    }

    //Methods
    public int attack(Player player, Maps map) {
        List<Enemy> victims = null;
        for (Coordinates attackCell : this.attackArea) {
            MapElement check = map.getDict().get(Coordinates.sum(player.getCoordinates(), attackCell));
            if (check instanceof Enemy enemy)
                victims.add(enemy);
        }
        for (Enemy enemy : victims) {
            int realDamage = (enemy.getCurrHP() - this.power /*-variabile globale*/ < 0) ? enemy.getCurrHP() : this.power;
            enemy.updateHP(- power /*-variabile globale*/);
        }
        return power;
    }
}
