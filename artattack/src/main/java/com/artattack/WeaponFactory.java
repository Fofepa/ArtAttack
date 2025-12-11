package com.artattack;

public class WeaponFactory implements ItemFactory {
    @Override
    public Item createItem() {
        return new Weapon();
    }
}