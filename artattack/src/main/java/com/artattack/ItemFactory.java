package com.artattack;

public interface ItemFactory{
    Item createItem(String name, String description, int amount);
}