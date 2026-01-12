package com.artattack.items;

public interface ItemFactory{
    Item createItem(String name, String description, int amount);
}