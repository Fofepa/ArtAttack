package com.artattack;

import java.util.List;

import com.artattack.view.MainFrame;

public class SmartAttackStrategy extends DecisionStrategy {

public SmartAttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseWeighted(this.getMoves());

         enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
    }

    private Move chooseWeighted(List<Move> moves) {
        if (moves.isEmpty()) return null;

        // we have to check if the positive factor works as intended
        double factor = 1.6;
        double[] weights = new double[moves.size()];
        weights[0] = 1.0;

        for (int i = 1; i < moves.size(); i++)
            weights[i] = weights[i-1] * factor;

        double total = 0;
        
        for (double w : weights) total += w;

        double r = Math.random() * total;
        double cumulative = 0;

        for (int i = 0; i < moves.size(); i++){
            cumulative += weights[i];
            if (r< cumulative) return moves.get(i);
        }

        return moves.get(moves.size() - 1);
    }
}