package com.artattack;

import java.util.List;

import com.artattack.view.MainFrame;

public class DumbAttackStrategy extends DecisionStrategy {

    public DumbAttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseWeighted(this.getMoves());
        
        int damage = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
        this.getMainFrame().showDialog(List.of(enemy.getName() + " has done " + damage + " dmg to the player"));
    }
    
    private Move chooseWeighted(List<Move> moves) { // function that defines the pb distribution of the usable moves
        if (moves.isEmpty()) return null;
        
        // partitioning the usable moves with a decreasing factor
        double factor = 0.7;
        double[] weights = new double[moves.size()];
        weights[0] = 1.0;

        for (int i = 1; i < moves.size(); i++)
            weights[i] = weights[i-1] * factor;

        double total = 0;
        for (double w : weights) total += w;

        // extraction of the move
        double r = Math.random() * total; 
        double cumulative = 0;
        for (int i = 0; i < moves.size(); i++) {
            cumulative += weights[i];
            if (r < cumulative) return moves.get(i);
        }

        return moves.get(moves.size() - 1);
    }
}