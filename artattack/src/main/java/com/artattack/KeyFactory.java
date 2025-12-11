package com.artattack;

public class KeyFactory implements ItemFactory {
    @Override
    public Item createItem() {
        return new Key();
    }
}