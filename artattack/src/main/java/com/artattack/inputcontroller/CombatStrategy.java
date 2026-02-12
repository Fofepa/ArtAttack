package com.artattack.inputcontroller;

import java.util.ArrayList;
import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
        boolean moveType = (move.getHealAmount() == 0 || (move.getHealArea() == null || move.getHealArea().isEmpty())); //true: dmg | false: heal
        List<ActiveElement> targets = moveType ? move.getAttackTargets(player, map) : move.getHealTargets(player, map);

        List<String> allMessages = new ArrayList<>();

        int value = move.useMove(player, map);

        if(value != 0){
            if(move.getName().equals("Wild at Heart")){
                 if (targets != null && !targets.isEmpty()) {
                    allMessages.add(targets.get(0).getName() + ": What do you want fa***t?!");
                 }
                 allMessages.add("*A group of offended thugs goes to the enemy and beats it really bad!");
            }
             if(move.getName().equals("Button Press")){
                mainFrame.showDialog(List.of(player.getName()+ " used " + move.getName(), "But nothing happened..."));
                return 0;
             }

            allMessages.add(player.getName()+ " used " + move.getName());

            String damageMsg = moveType  ?  player.getName() + ": has done damage " + value : player.getName() + ": healed "  + value;
            if (targets != null) {
                damageMsg += " to " + targets.size() + (moveType ? " enemies!" : " allies!");
            }
            allMessages.add(damageMsg);

            isSelected = false;
            moveIndex = 0;
            weaponIndex = 0;

            if (targets != null) {
                boolean repainted = false;
                boolean isBossDefeated = false;
                for(ActiveElement element : targets){
                    if(!element.isAlive()){
                        allMessages.add(element.getName() + ": has been defeated!");

                        if ("Sam Altman".equals(element.getName())) {
                            allMessages.add("It's over... The system is shutting down...");
                            allMessages.add("You have saved the digital world!");
                            isBossDefeated = true;
                        }

                        if (!repainted) {
                            this.mainFrame.repaintStatsPanel(); 
                            repainted = true;
                        }
                    }
                }
                
                if (!allMessages.isEmpty()) {
                    this.mainFrame.showDialog(allMessages);
                    
                    if (isBossDefeated) {
                        waitForDialogAndEndGame();
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

    private void waitForDialogAndEndGame() {
        Timer timer = new Timer(100, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mainFrame.getDialogActive()) {
                    timer.stop(); 
                    if (mainFrame.getMainGUIFacade() != null) {
                        mainFrame.getMainGUIFacade().showGameVictory();
                    }
                }
            }
        });
        timer.start();
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
