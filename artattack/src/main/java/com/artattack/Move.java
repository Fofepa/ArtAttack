package com.artattack;

import java.util.ArrayList;
import java.util.List;

public class Move {
    //Attributes
    private String name;
    private String description;
    private int power;
    private int actionPoints_temp;
    private List<Coordinates> attackArea;

    //Constructors
    public Move() {

    }

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

    public int getActionPoints() {
        return this.actionPoints_temp;
    }

    public List<Coordinates> getAttackArea() {
        return this.attackArea;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints_temp = actionPoints;
    }

    public void setAttackArea(List<Coordinates> attackArea) {
        this.attackArea = attackArea;
    }

    //Methods
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Move m) {
            if (!m.getName().equals(this.getName())) {
                return false;
            }
            if (!m.getDescription().equals(this.getName())) {
                return false;
            }
            if (m.getPower() != this.getPower()) {
                return false;
            }
            if (m.getActionPoints() != this.getActionPoints()) {
                return false;
            }
            return m.getAttackArea() == this.getAttackArea();
        }
        return false;
    }

    public int attack(MapElement attacker, Maps map) {
        List<MapElement> victims = new ArrayList<>();
        for (Coordinates attackCell : this.attackArea) {
            MapElement check = map.getDict().get(Coordinates.sum(attacker.getCoordinates(), attackCell));
            if (attacker instanceof Player){
                if (check instanceof Enemy enemy)
                    victims.add(enemy);
            }
            else if (attacker instanceof Enemy)
                if (check instanceof Player player)
                    victims.add(player);
        }
        for (MapElement element : victims) {
            switch (element) {
                case Enemy e -> {
                    e.updateHP(- this.power /*-variabile globale*/);
                }
                case Player p -> {  
                    p.updateHP(- this.power /*-variabile globale*/);
                }
                default -> {
                }
            }
        }
        return this.power;
    }
}
