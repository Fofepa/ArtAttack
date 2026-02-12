package com.artattack.moves;

import java.util.ArrayList;
import java.util.List;

import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;

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
        this.name = "";
        this.description = "";
        this.power = 0;
        this.healAmount = 0;
        this.actionPoints = 0;
        this.attackArea = new ArrayList<>();
        this.healArea = new ArrayList<>();
        this.areaAttack = false;
        this.areaHeal = false;
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
        AreaBuilder ab = new AreaBuilder();
        ab.addShape("8");
        this.range = !(this.attackArea.containsAll(ab.getResult()));
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
        if (this.name.equals("Little Umbrellas")) {
            List<ActiveElement> l = new ArrayList<>();
            l.addAll(map.getEnemies());
            return l;
        }
        if (this.attackArea == null || this.attackArea.isEmpty()) {
            System.out.println(this.getName() + " has no attackArea");
            return null;
        }
        if (!this.areaAttack) {
            ActiveElement temp = null;
            for (Coordinates attackCell : this.attackArea) {
                Object obj = map.getDict().get(Coordinates.sum(user.getCoordinates(), attackCell));
                if (obj instanceof ActiveElement e) {
                    if ((e.isAlive() && (temp == null || 
                        Math.abs(temp.getMaxHP() - temp.getCurrHP()) < Math.abs(e.getMaxHP() - e.getCurrHP())))) {
                        if (user.getClass() != e.getClass()) {
                            temp = e;
                        }
                    }
                }
            }
            if (temp == null) {
                System.out.println(this.getName() + ": temp is null");
            }
            return (temp == null) ? null : List.of(temp);
        }
        List<ActiveElement> targets = new ArrayList<>();
        for (Coordinates attackCell : Coordinates.sum(this.attackArea, user.getCoordinates())) {
            Object obj = map.getDict().get(attackCell);
            //System.out.println(map.getDict().containsValue(map.getPlayerOne())); for debug reasons
            if (obj instanceof ActiveElement check) {
                System.out.println("Checking " + ((ActiveElement) obj).getName());
                // If user is Player, attack Enemies; If user is Enemy, attack Players
                if (user instanceof Player) {
                    if (check instanceof Enemy enemy) {
                        targets.add(enemy);
                    }
                } 
                else if (user instanceof Enemy) {
                    if (check instanceof Player player) {
                        if (player.isAlive()) {
                            targets.add(player);
                        }
                    }
                }   
            }
            else { /* System.out.println("The object was not a ActiveElement"); */ }    // for debug reasons
        }
        if (targets.isEmpty()) {
            System.out.println(this.getName() + ": no targets found");
        }
        return targets.isEmpty() ? null : targets;
    }


    public List<ActiveElement> getHealTargets(ActiveElement user, Maps map) {
        if (this.healArea == null || this.healArea.isEmpty()) {
            return null;
        }
        if (!this.areaHeal){
            ActiveElement temp = null;
            for(Coordinates areaCell : Coordinates.sum(this.healArea, user.getCoordinates())){
                Object obj = map.getDict().get(areaCell);
                if (obj instanceof ActiveElement e) {
                    if ((temp == null || Math.abs(temp.getMaxHP() - temp.getCurrHP()) < Math.abs(e.getMaxHP() - e.getCurrHP()))) {
                        if (user.getClass() == e.getClass()) {
                            temp = e;
                        }
                    }
                }
            }
            return (temp == null) ? null : List.of(temp);
        }
        List<ActiveElement> targets = new ArrayList<>();
        for (Coordinates healCell : this.healArea) {
            Object obj = (ActiveElement)map.getDict().get(Coordinates.sum(user.getCoordinates(), healCell));
            if (obj instanceof ActiveElement check) {
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
        }
        return (targets.isEmpty()) ? null : targets;
    }

    public int useMove(ActiveElement user, Maps map) {
        //Checks if user has enough ActionPoints
        if (user.getActionPoints() < this.getActionPoints()) {
            return 0;
        }
        int total = 0;
        boolean works = false;
        //Damage Logic
        if ((this.power != 0 && !this.attackArea.isEmpty() && this.getAttackTargets(user, map) != null) || (this.name.equals("Little Umbrellas") && this.power != 0 && this.getAttackTargets(user, map) != null)) {
            for (ActiveElement element : this.getAttackTargets(user, map)) {
                if (element instanceof Enemy e) {
                    if (user.equals(map.getPlayerOne())) { e.updatePlayerOneDamage(this.power); }
                    else { e.updatePlayerTwoDamage(this.power); }
                    if((this.name.equals("ERASERHEAD") || this.name.equals("Flash")) && !e.isStunned()){
                        e.setIsStunned(true);
                        System.out.println("Enemy is stunned? " + e.isStunned()); //for debug
                    }
                    if(this.name.equals("Wild at Heart")){
                        e.setEnemyType(EnemyType.DUMMY);
                    }
                    
                }
                element.updateHP(- this.power);
                total += this.power;
                if(this.name.equals("Fragile Swing") && this.power != 0){
                    this.setPower((int) Math.floor(this.power/2));
                }
                if (!element.isAlive()) {
                    element.onDeath(map, user);
                }
                if (!works) {
                    works = true;
                }
                if (!this.areaAttack) {
                    break;
                }
            }
        }
        //Healing Logic
        if (this.healAmount != 0 && !this.healArea.isEmpty() && this.getHealTargets(user, map) != null) {
            for (ActiveElement element : this.getHealTargets(user, map)) {
                boolean wasDead = element.getCurrHP() <= 0;
                element.updateHP(this.healAmount);
                total += this.healAmount;


                if (element.getCurrHP() > 0 && wasDead) { 
                    
                    ((Player)element).revive(map);
                    System.out.println(element.getName() + " is revived and back in the turn queue!");
                }

                if (!works) {
                    works = true;
                }

                if (!this.areaHeal) {
                    break;
                }
            }
            if (!works) {
                works = true;
            }
        }
        if (works) {
            user.setActionPoints(user.getActionPoints() - this.getActionPoints());
        }
        return total;
    }

}
