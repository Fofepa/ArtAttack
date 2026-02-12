package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.CheckPoint;
import com.artattack.interactions.Give;
import com.artattack.interactions.SwitchMap;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.level.Coordinates;

public class InteractableElementDirector {
    public void createChest(InteractableElementBuilder builder, List<Item> item, Coordinates coordinates){
        builder.setMapSymbol('$');
        builder.setName("Chest");
        builder.setCoordinates(coordinates);
        builder.setInteractions(
            List.of(new Give(List.of("You've found new items!"), item), new Talk(List.of("The chest is empty"))
        ));
    }

    public void createCheckPoint(InteractableElementBuilder builder, Coordinates coordinates){
        builder.setMapSymbol('A');
        builder.setName("Aretha Franklin");
        builder.setCoordinates(coordinates);
        builder.setSpritePath("/images/aretha.jpg/");
        builder.setInteractions(
            List.of(new CheckPoint(List.of("If anything goes bad you'll come here guys!", "Don't get yourselves killed!")))
        );

    }
    
    public void createDoor(InteractableElementBuilder builder, int nextMap, Coordinates coordinates){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setCoordinates(coordinates);
        builder.setInteractions(
            List.of(new SwitchMap(nextMap))
        );
        
    }

    public void createDoor(InteractableElementBuilder builder, int nextMap, boolean isLevelFinish, Coordinates coordinates){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setCoordinates(coordinates);
        builder.setInteractions(
            List.of(new SwitchMap(nextMap, isLevelFinish))
        );
        
    }

    public void createDoor(InteractableElementBuilder builder, int nextMap, int key, Coordinates coordinates){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setCoordinates(coordinates);
        builder.setInteractions(
            List.of(new SwitchMap(key, nextMap))
        );
        
    }

     public void createDoor(InteractableElementBuilder builder, int nextMap, boolean isLevelFinish, int key, Coordinates coordinates){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setCoordinates(coordinates);
        builder.setInteractions(
            List.of(new SwitchMap(key, nextMap, isLevelFinish))
        );
        
    }

    public void createDoor(InteractableElementBuilder builder, int nextMap, Coordinates coordinates, Coordinates p1Overwrite, Coordinates p2Overwrite) {
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setCoordinates(coordinates);
        SwitchMap sm = new SwitchMap(nextMap);
        sm.setOverwrite(p1Overwrite, p2Overwrite);
        System.out.println(sm.getP1Overwrite() + "\n" + sm.getP2Overwrite());
        builder.setInteractions(
            List.of(sm)
        );
    }
}
