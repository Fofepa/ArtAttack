package com.artattack;

public class Enemy extends MapElement {

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates) {
        super(ID, mapSymbol, name, coordinates);
        //TODO Auto-generated constructor stub
    }
    
    public int getCurrHP() {
        return 0;
    }

    public void updateHP(int delta) {
    }
}
