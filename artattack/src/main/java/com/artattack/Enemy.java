package com.artattack;

public class Enemy extends MapElement {
    //Attributes
    private int maxHP;
    private int currHP;

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, int maxHP, int currHP) {
        super(ID, mapSymbol, name, coordinates);
        //TODO Auto-generated constructor stub
        this.maxHP = maxHP;
        this.currHP = currHP;
    }
    
    public int getCurrHP() {
        return this.currHP;
    }

    public void updateHP(int amount){
        if(this.currHP + amount > this.maxHP)
            this.currHP = this.maxHP;
        else
            this.currHP += amount;
    }
}
