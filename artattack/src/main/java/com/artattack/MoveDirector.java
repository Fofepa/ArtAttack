package com.artattack;

import java.util.List;

public class MoveDirector {
    //Attributes
    private MoveBuilder builder;

    //Constructor
    public MoveDirector(MoveBuilder builder) {
        this.builder = builder;
    }

    //Methods
    public void setBuilder(MoveBuilder builder) {
        this.builder = builder;
    }

    public void makeTestMove(MoveBuilder builder) {
        this.builder.setName("TestMove");
        this.builder.setDescription("TestDescription");
        this.builder.setPower(1);
        this.builder.setActionPoints(1);
        this.builder.setAttackArea(List.of(new Coordinates(0, 1)));
    }
}
