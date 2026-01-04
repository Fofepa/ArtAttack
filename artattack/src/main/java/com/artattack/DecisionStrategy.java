package com.artattack;

import java.util.Comparator;
import java.util.List;

import com.artattack.view.MainFrame;

public abstract class DecisionStrategy {
    private MainFrame mainframe;
    private List<Move> moves;

    public DecisionStrategy(MainFrame mainframe){
        this.mainframe = mainframe;
    }

    public abstract  void execute(Enemy enemy,Maps map);

    public MainFrame getMainFrame(){
        return this.mainframe;
    }

    public void setMoves(List<Move> moves){
        moves.sort(Comparator.comparing(Move::getPower).thenComparing(Move::getAreaAttack).thenComparing(m -> m.getAttackArea().size()));
        this.moves = moves;
    }

    public List<Move> getMoves(){
        return  this.moves;
    }
}