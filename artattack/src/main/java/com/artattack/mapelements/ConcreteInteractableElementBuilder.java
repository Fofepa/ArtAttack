package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.level.Coordinates;

public class ConcreteInteractableElementBuilder implements InteractableElementBuilder {
    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    private String spritePath;
    private List<Interaction> interactions;

    @Override
    public void setID(int ID){
        this.ID = ID;
    }
    @Override
    public  void setMapSymbol(char mapSymbol){
        this.mapSymbol = mapSymbol;
    }
    @Override
    public  void setName(String name){
        this.name = name;
    }
    @Override
    public  void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }
    @Override
    public  void setSpritePath(String spritePath){
        this.spritePath = spritePath;
    }
    @Override
    public void setInteractions(List<Interaction> interactions){
        this.interactions = interactions;
    }
}
