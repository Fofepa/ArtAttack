package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.level.Coordinates;

public interface InteractableElementBuilder {
    public abstract void setID(int ID);
    public abstract void setMapSymbol(char mapSymbol);
    public abstract void setName(String name);
    public abstract void setCoordinates(Coordinates coordinates);
    public abstract void setSpritePath(String spritePath);
    public abstract void setInteractions(List<Interaction> interactions);
    
}
