package com.artattack;

public class SpeedBuffFactory implements ItemFactory {
    @Override
    public Item createItem(String name, String description, int amount) {
        return new SpeedBuff(name,description,amount);
    }
}