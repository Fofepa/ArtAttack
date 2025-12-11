package com.artattack;

public class SpeedBuffFactory implements ItemFactory {
    @Override
    public Item createItem() {
        return new SpeedBuff();
    }
}