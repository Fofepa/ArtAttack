package com.artattack;

import java.util.List;

public class MoveBuilder1 implements MoveBuilder {
    private Move move;

    public MoveBuilder1() {
        this.move = new Move();
    }

    public void reset() {
        this.move = new Move();
    }

    @Override
    public void setName(String name) {
        this.move.setName(name);
    }

    @Override
    public void setDescription(String description) {
        this.move.setDescription(description);
    }

    @Override
    public void setPower(int power) {
        this.move.setPower(power);
    }

    @Override
    public void setActionPoints(int actionPoints) {
        this.move.setActionPoints(actionPoints);
    }

    @Override
    public void setAttackArea(List<Coordinates> attackArea) {
        this.move.setAttackArea(attackArea);
    }
    
    public Move getResult() {
        Move product = this.move;
        this.reset();
        return product;
    }
}
