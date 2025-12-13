package com.artattack;

public class AttackBuffFactory implements ItemFactory {
    @Override
    public Item createItem() {
        return new AttackBuff();
    }
}