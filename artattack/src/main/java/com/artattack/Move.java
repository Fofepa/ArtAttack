package com.artattack;

import java.util.ArrayList;
import java.util.List;

public class Move{
    //Attributes
    private String name;
    private String description;
    private int power;
    private int healAmount;
    private int actionPoints;
    private List<Coordinates> attackArea;
    private List<Coordinates> healArea;
    private boolean areaAttack;
    private boolean areaHeal;
    private boolean range; //Ranged := true; Melee := false

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

    public boolean getAreaAttack() {
        return this.areaAttack;
    }

    public boolean getAreaHeal() {
        return this.areaHeal;
    }

    public boolean getRange() {
        this.range = !(this.attackArea.contains(new Coordinates(-1, 1)) || 
                        this.attackArea.contains(new Coordinates(0, 1)) ||
                        this.attackArea.contains(new Coordinates(1, 1)) ||
                        this.attackArea.contains(new Coordinates(-1, 0)) ||
                        this.attackArea.contains(new Coordinates(1, 0)) ||
                        this.attackArea.contains(new Coordinates(-1, -1)) ||
                        this.attackArea.contains(new Coordinates(0, -1)) ||
                        this.attackArea.contains(new Coordinates(1, -1)));
        return this.range;
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

    public void setAreaAttack(boolean areaAttack) {
        this.areaAttack = areaAttack;
    }

    public void setAreaHeal(boolean areaHeal) {
        this.areaHeal = areaHeal;
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
            if (!m.getDescription().equals(this.getDescription())) {
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

    public List<ActiveElement> getAttackTargets(ActiveElement user, Maps map) {
        List<ActiveElement> targets = new ArrayList<>();
        for (Coordinates attackCell : this.attackArea) {
            ActiveElement check = (ActiveElement)map.getDict().get(Coordinates.sum(user.getCoordinates(), attackCell));
            //If user is Player, attack Enemies; If user is Enemy, attack Players
            if (user instanceof Player) {
                if (check instanceof Enemy enemy) {
                    targets.add(enemy);
                }
            }
            else if (user instanceof Enemy) {
                if (check instanceof Player player) {
                    targets.add(player);
                }
            }
        }
        return (targets.isEmpty()) ? null : targets;
    }

    public List<ActiveElement> getHealTargets(ActiveElement user, Maps map) {
        List<ActiveElement> targets = new ArrayList<>();
        for (Coordinates healCell : this.healArea) {
            ActiveElement check = (ActiveElement)map.getDict().get(Coordinates.sum(user.getCoordinates(), healCell));
            //If user is Player, heal Players; If user is Enemy, heal Enemies
            if (user instanceof Player) {
                if (check instanceof Player player) {
                    targets.add(player);
                }
            }
            else if (user instanceof Enemy) {
                if (check instanceof Enemy enemy) {
                    targets.add(enemy);
                }
            }
        }
        return (targets.isEmpty()) ? null : targets;
    }

    public int useMove(ActiveElement user, Maps map) {
        //Checks if user has enough ActionPoints
        if (user.getActionPoints() < this.getActionPoints()) {
            return 0;
        }
        //Damage Logic
        if (this.power != 0 && !this.attackArea.isEmpty()) {
            for (ActiveElement element : this.getAttackTargets(user, map)) {
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
                if (!this.areaAttack) {
                    break;
                }
            }
        }
        //Healing Logic
        if (this.healAmount != 0 && !this.healArea.isEmpty()) {
            for (ActiveElement element : this.getHealTargets(user, map)) {
                element.updateHP(this.healAmount);
                if (!this.areaHeal) {
                    break;
                }
            }
        }
        user.setActionPoints(user.getActionPoints() - this.getActionPoints());
        return this.power;
    }

}
