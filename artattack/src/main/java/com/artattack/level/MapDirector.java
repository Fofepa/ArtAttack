package com.artattack.level;

import java.util.List;

import com.artattack.interactions.Talk;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.view.InteractionPanel;

public class MapDirector {
    private MapBuilder builder;

    public MapDirector(MapBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(MapBuilder builder) {
        this.builder = builder;
    }

    public void make(String type) {
        switch (type) {
            case "Tutorial":
                this.builder.setDimension(26, 32);
                this.builder.setEnemies(
                    List.of(new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, null, null, null, null, 0)));
                TriggerGroup chestDialogue = new TriggerGroup(new Talk(new InteractionPanel(), 
                    List.of("Hey, over here! Mr. Zappa! Mr. Lynch!", 
                            "What a mess! Look at what they've done to our peaceful resting place! It's now a soul-harvesting data center! You need to do something about this, or else we'll never be able to return to our well-earned slumber!",
                            "Me? Oh, I'm nowhere near as powerful as you guys, I wouldn't last a second against these guys.",
                            "You guys don't look so good... What happened to you?",
                            "Oh! Right! You two were dead, just like me! Hahahaha!",
                            "What's that? You don't remember how to do things? Yeah, makes sense considering you were six feet deep just a few moments ago. You must be rusty on what it takes to walk the earth.",
                            "But it's fine, I can teach you everything you need to know.",
                            "Each one of you has a cursor. It has many uses, like moving!",
                            "You can position your cursor using WASD or the Arrow Keys. Then, use Enter to move where the cursor is!",
                            "Or, you can position your cursor on something that interests you and interact with it using the E button. You can also talk to people this way!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Now, go! Get to the top floor and defeat these power-hungry nerds!",
                            "Wait... Look! That red guy over there! It's an employee! You have to defeat him to get to the next floor!",
                            "Go teach him a lesson!")));
                this.builder.setInteractableElements(
                List.of(new InteractableElement(0, '$', "Chest", new Coordinates(1, 30), null, null, null, null)));
                break;
        }
    }
}
