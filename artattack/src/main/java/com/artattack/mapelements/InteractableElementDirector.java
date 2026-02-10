package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Give;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;

public class InteractableElementDirector {
    public void createChest(InteractableElementBuilder builder, List<Item> item){
        builder.setMapSymbol('$');
        builder.setName("Chest");
        builder.setInteractions(
            List.of(new Give(List.of("You've found new items!"), item), new Talk(List.of("The chest is empty"))
        ));
    }

    public void createCheckPoint(InteractableElementBuilder builder){
        

    }
    /*public void createDoor(InteractableElementBuilder builder, int nextMap){
        builder.setMapSymbol('\u2339');
        builder.setName("Door");
        builder.setInteractions(
            List.of(new SwitchMap(,))
        );
        
    }*/
}
