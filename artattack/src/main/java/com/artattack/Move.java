package com.artattack;

import java.util.ArrayList;
import java.util.List;

public class Move {
    //Attributes
    private String name;
    private String description;
    private int power;
    private int healAmount;
    private int actionPoints;
    private List<Coordinates> attackArea;
    private List<Coordinates> healArea;

    //Constructors
    public Move() {

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

    public int getHealAmount() {
        return this.healAmount;
    }

    public int getActionPoints() {
        return this.actionPoints;
    }

    public List<Coordinates> getAttackArea() {
        return this.attackArea;
    }

    public List<Coordinates> getHealArea() {
        return this.healArea;
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

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }

    public void setAttackArea(List<Coordinates> attackArea) {
        this.attackArea = attackArea;
    }

    public void setHealArea(List<Coordinates> healArea) {
        this.healArea = healArea;
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

    public int useMove(ActiveElement user, Maps map) {
        if (user.getActionPoints() < this.getActionPoints()) {
            return 0;
        }
        //Damage Logic
        if (this.power != 0 && !this.attackArea.isEmpty()) {
            List<ActiveElement> victims = new ArrayList<>();
            for (Coordinates attackCell : this.attackArea) {
                ActiveElement check = (ActiveElement)map.getDict().get(Coordinates.sum(user.getCoordinates(), attackCell));
                if (user instanceof Player){
                    if (check instanceof Enemy enemy)
                        victims.add(enemy);
                }
                else if (user instanceof Enemy)
                    if (check instanceof Player player)
                        victims.add(player);
            }
            for (ActiveElement element : victims) {
                switch (element) {
                    case Enemy e -> {
                        e.updateHP(- this.power /*-variabile globale*/);
                        if(user.equals(map.getPlayerOne()))
                            e.updatePlayerOneDemage(this.power);
                        else e.updatePlayerTwoDemage(this.power);
                        if(!e.isAlive()){
                            e.dropXP(map.getPlayerOne(), map.getPlayerTwo());
                            if(e.getDrops() != null && !e.getDrops().isEmpty()){
                                List<Item> drops = e.getDrops();
                                Player p = (Player) user;
                                for(Item item : drops)
                                    p.addItem(item);
                            }
                            if(e.getKeys() != null && !e.getKeys().isEmpty()){
                                List<Key> keys = e.getKeys();
                                Player p = (Player) user;
                                for(Key key : keys)
                                    p.addKey(key);
                            }
                            e.remove(map);
                        }
                    }
                    case Player p -> {  
                        p.updateHP(- this.power /*-variabile globale*/);
                        //Need to add GameOver logic
                    }
                    default -> {
                    }
                }
            }
        }
        //Healing Logic
        if (this.healAmount != 0 && !this.healArea.isEmpty()) {
            List<ActiveElement> benefactors = new ArrayList<>();
            for (Coordinates healCell : this.healArea) {
                ActiveElement check = (ActiveElement)map.getDict().get(Coordinates.sum(user.getCoordinates(), healCell));
                if (user instanceof Player){
                    if (check instanceof Player player)
                        benefactors.add(player);
                }
                else if (user instanceof Enemy)
                    if (check instanceof Enemy enemy)
                        benefactors.add(enemy);
            }
            for (ActiveElement element : benefactors) {
                element.updateHP(this.healAmount);
            }
        }
        user.setActionPoints(user.getActionPoints() - this.getActionPoints());
        return this.power;
    }
}
