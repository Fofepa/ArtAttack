package com.artattack;

public class CureFactory implements ItemFactory {
    @Override
    public Item createItem(String name, String description, int amount) {
        return new Cure(name,description,amount);
    }
}