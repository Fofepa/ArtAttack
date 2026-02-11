package com.artattack.inputcontroller;

import java.util.ArrayList;
import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
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
    public void execute(int dx, int dy){    
        
        if ((dx == 1 || dx == -1) && dy == 0){ 
            moveWeaponIndex(dx);
        }
        else if ((dx == 1 || dx == -1) && dy == 1){
            moveMoveIndex(dx);
        }
    }     

    private void moveWeaponIndex(int index){
        this.weaponIndex = (this.weaponIndex + index + player.getWeapons().size())  % (player.getWeapons().size());
    }

    private void moveMoveIndex(int index){
        this.moveIndex = (this.moveIndex + index + player.getWeapons().get(weaponIndex).getInitMoves()) % player.getWeapons().get(weaponIndex).getInitMoves();
    }

    public int acceptMove(){
        Move move = player.getWeapons().get(weaponIndex).getMoves().get(moveIndex);
        List<ActiveElement> targets = move.getAttackTargets(player, map); 

        List<String> allMessages = new ArrayList<>();

        int value = move.useMove(player, map);

        if(value != 0){
            if("Wild at Heart".equals(move.getName())){
                 if (targets != null && !targets.isEmpty()) {
                    allMessages.add(targets.get(0).getName() + ": What do you want fa***t?!");
                 }
                 allMessages.add("*A group of offended thugs goes to the enemy and beats it really bad!");
            }

            allMessages.add(player.getName()+ " used " + move.getName());

            String damageMsg = player.getName() + ": has done damage " + value;
            if (targets != null) {
                damageMsg += " to " + targets.size() + " enemies!";
            }
            allMessages.add(damageMsg);

            isSelected = false;
            moveIndex = 0;
            weaponIndex = 0;

            if (targets != null) {
                boolean repainted = false;
                for(ActiveElement element : targets){
                    if(!element.isAlive()){
                        allMessages.add(element.getName() + ": has been defeated!");

                        if (!repainted) {
                            this.mainFrame.repaintStatsPanel(); 
                            repainted = true;
                        }
                    }
                }
            }
        } 
        else{
            allMessages.add("Not enough AP or no one to be hit");
        }

        if (!allMessages.isEmpty()) {
            this.mainFrame.showDialog(allMessages);
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
