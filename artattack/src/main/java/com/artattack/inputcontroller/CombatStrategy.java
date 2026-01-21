package com.artattack.inputcontroller;

import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class CombatStrategy implements PlayerStrategy{
    private Maps map;
    private Player player;    
    private boolean isSelected = false;
    private int weaponIndex = 0;
    private int moveIndex = 0;
    private MainFrame mainFrame;

    public CombatStrategy(Maps map, Player player){
        this.map = map;
        this.player = player;
    }

    @Override
    public void execute(int dx, int dy){    // in this case dx is the weapon index and dy is the move index
        
        if ((dx == 1 || dx == -1) && dy == 0){ // selection of the weapon, dy must be 0
            moveWeaponIndex(dx);
        }
        else if ((dx == 1 || dx == -1) && dy == 1){ // selection of the move associated to the dx-weapon dy is 1
            moveMoveIndex(dx);
        }
    }     

    private void moveWeaponIndex(int index){
        this.weaponIndex = (this.weaponIndex + index)  % (player.getWeapons().size());
    }

    private void moveMoveIndex(int index){
        this.moveIndex = (this.moveIndex + index) % player.getWeapons().get(weaponIndex).getMoves().size();
    }

    public int acceptMove(){
        int value = player.getWeapons().get(weaponIndex).getMoves().get(moveIndex).useMove(player, map);
        
        if(value != 0){
            this.mainFrame.showDialog(List.of(player.getName() + " has done damage " + value));
            isSelected = false;
            moveIndex = 0;
            weaponIndex = 0;
        } 
        else{
            this.mainFrame.showDialog(List.of("Not enough AP or no one to be hit"));
        }

        if(player.getActionPoints() == 0){
            mainFrame.getMap().getConcreteTurnHandler().next();
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

    public void setMainFrame(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setIsSelected(boolean condition){
        this.isSelected = condition;
    }

    public void setMap(Maps map){
        this.map = map;
    }

    public void setMoveIndex(int moveIndex) {
        this.moveIndex = moveIndex;
    }

    @Override
    public int getType(){
        return 1;
    }

    
}
