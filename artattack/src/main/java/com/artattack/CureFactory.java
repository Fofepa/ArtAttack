package com.artattack;

public class CureFactory implements ItemFactory {
    @Override
    public Item createItem() {
        return new Cure();
    }
}