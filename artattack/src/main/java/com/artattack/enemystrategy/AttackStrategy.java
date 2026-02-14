package com.artattack.enemystrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;

public class AttackStrategy extends DecisionStrategy {

    public AttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseUniform(this.getMoves());
        List<ActiveElement> targets = move.getAttackTargets(enemy, map); 

         int damage = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
         this.getMainFrame().showDialog(List.of(enemy.getName() + " used " + move.getName(), enemy.getName() + " has done " + damage + " dmg to the player"), enemy.getSpritePath());
         /* for(ActiveElement element : move.getAttackTargets(enemy, map)){
            if(!element.isAlive()){
               this.getMainFrame().showDialog(List.of(element.getName() + " has been defeated!"));
               element.setMapSymbol('\u2620');
               this.getMainFrame().repaintMapPanel();
               this.getMainFrame().repaintTurnOrderPanel();
            }
        } */
        if(targets.size() == 1 && !targets.get(0).isAlive()){
            this.getMainFrame().showDialog(List.of(targets.get(0).getName() + " has been defeated!"), enemy.getSpritePath());
        }
        else if(targets.size() == 2){
            List<String> support = new ArrayList<>();
            if(!targets.get(0).isAlive()){
                support.add(targets.get(0).getName() + " has been defeated!");
            }
            if(!targets.get(1).isAlive()){
                support.add(targets.get(1).getName() + " has been defeated!");
            }

            this.getMainFrame().showDialog(support, enemy.getSpritePath());
        }
        this.getMainFrame().repaintMapPanel();
        this.getMainFrame().repaintTurnOrderPanel();
        //this.getMainFrame().gameOver();
    }

    private Move chooseUniform(Map<Move, Integer> moves){
        if (moves.isEmpty()) return null;
        List<Move> moveList = new ArrayList<>(moves.keySet());
        return moveList.get((int)(Math.random() * (moveList.size() - 1)));
    }
}