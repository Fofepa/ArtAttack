package com.artattack;

public class CombatStrategy{
    private Maps map;
    private Player player;    
    private boolean isSelected = false;
    private int weaponIndex = 0;
    private int moveIndex = 0;

    public CombatStrategy(MapBuilder mapBuilder){
        this.map = mapBuilder.getResult();
    }

    public void execute(int dx, int dy){    // in this case dx is the weapon index and dy is the move index
        
        if (dx >=0 && dx <= player.getWeapons().size()-1 && dy == 0){ // selection of the weapon, dy must be 0
            moveWeaponIndex(dx);
        }
        else if (dx >=0 && dx <= player.getWeapons().size()-1 && dy >= 1 && dy <= player.getWeapons().get(dx).getMoves().size()){ // selection of the move associated to the dx-weapon
            moveMoveIndex(dy);
        }
    }     

    private void moveWeaponIndex(int index){
        this.weaponIndex = index % (player.getWeapons().size());
    }

    private void moveMoveIndex(int index){
        this.moveIndex = (index - 1) % player.getWeapons().get(weaponIndex).getMoves().size();
    }

    public int acceptMove(){
        int value = player.getWeapons().get(weaponIndex).getMoves().get(moveIndex).useMove(player, map);
        
        if(value != 0){
            isSelected = false;
            moveIndex = 0;
            weaponIndex = 0;
        } 
        return value;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setCurrentPlayer(Player player){
        this.player = player;
    }

    public Maps getMaps(){
        return this.map;
    }

    public int getWeaponIndex(){
        return this.weaponIndex;
    }

    public int getMoveIndex(){
        return this.moveIndex;
    }

    public boolean isSelected(){
        return this.isSelected;
    }
}
