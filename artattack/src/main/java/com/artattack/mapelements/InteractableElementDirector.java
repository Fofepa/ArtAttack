package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.CheckPoint;
import com.artattack.interactions.Give;
import com.artattack.interactions.SwitchMap;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.level.Coordinates;

public class InteractableElementDirector {
    public void createChest(InteractableElementBuilder builder, List<Item> item){
        builder.setMapSymbol('$');
        builder.setName("Chest");
        builder.setInteractions(
            List.of(new Give(List.of("You've found new items!"), item), new Talk(List.of("The chest is empty"))
        ));
    }

    public void createCheckPoint(InteractableElementBuilder builder){
        builder.setMapSymbol('C');
        builder.setName("Checkpoint");
        builder.setInteractions(
            List.of(new CheckPoint(List.of("Your progress has been saved")))
        );

    }
    
    public void createDoor(InteractableElementBuilder builder, int nextMap, List<Coordinates> nexCoordinates){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setInteractions(
            List.of(new SwitchMap(nextMap, nexCoordinates))
        );
        
    }

    public void createDoor(InteractableElementBuilder builder, int nextMap, List<Coordinates> nexCoordinates, boolean isLevelFinish){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setInteractions(
            List.of(new SwitchMap(nextMap, nexCoordinates, isLevelFinish))
        );
        
    }

    public void createDoor(InteractableElementBuilder builder, int nextMap, List<Coordinates> nexCoordinates, int key){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setInteractions(
            List.of(new SwitchMap(key, nextMap, nexCoordinates))
        );
        
    }

     public void createDoor(InteractableElementBuilder builder, int nextMap, List<Coordinates> nexCoordinates, boolean isLevelFinish, int key){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setInteractions(
            List.of(new SwitchMap(key, nextMap, nexCoordinates, isLevelFinish))
        );
        
    }
}
