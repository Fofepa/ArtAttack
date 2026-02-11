package com.artattack.enemystrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;

public class SmartAttackStrategy extends DecisionStrategy {

public SmartAttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseWeighted(this.getMoves());

        int damage = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
        this.getMainFrame().showDialog(List.of(enemy.getName() + " used " + move.getName(), enemy.getName() + " has done " + damage + " dmg to the player"));
        for(ActiveElement element : move.getAttackTargets(enemy, map)){
            if(!element.isAlive()){
               this.getMainFrame().showDialog(List.of(element.getName() + " has been defeated!"));
            }
        }
        //this.getMainFrame().gameOver();
    }

    private Move chooseWeighted(Map<Move, Integer> moves) {
        if (moves.isEmpty()) return null;

        double factor = 6.53;    // factor for the distribution if > 1 the elements on top of the list are more likely to appear 

        List<Move> orderedMoves = new ArrayList<>(moves.keySet());

        // distribution of the weights inside the list
        int n = orderedMoves.size();
        double[] weights = new double[n];

        weights[0] = 1.0;

    
        for (int i = 1; i < n; i++) {
            weights[i] = weights[i - 1] * factor;
        }

        double total = 0;
        for (double w : weights) total += w;

    
        // extraction of the element
        double r = Math.random() * total;
        double cumulative = 0;

        for (int i = 0; i < n; i++) {
            cumulative += weights[i];
            if (r < cumulative) {
                return orderedMoves.get(i);   // 🔥 a little fire to keep us warm
            }
        }
        return orderedMoves.get(n - 1);
    }
}