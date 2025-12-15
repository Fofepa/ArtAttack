package com.artattack;

public class AttackBuffFactory implements ItemFactory {
    @Override
    public Item createItem(String name, String description, int amount) {
        return new AttackBuff(name,description,amount);
    }
}