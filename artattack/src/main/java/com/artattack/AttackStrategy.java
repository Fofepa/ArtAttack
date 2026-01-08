package com.artattack;

import java.util.List;

import com.artattack.view.MainFrame;

public class AttackStrategy extends DecisionStrategy {

    public AttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseUniform(this.getMoves());

         int damage = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
         this.getMainFrame().showDialog(List.of(enemy.getName() + " has done " + damage + " dmg to the player"));

    }

    private Move chooseUniform(List<Move> moves){   // just selects a move accordingly to the uniform distribution
        if (moves.isEmpty()) return null;
        return moves.get((int)(Math.random() * (moves.size())));
    }
}