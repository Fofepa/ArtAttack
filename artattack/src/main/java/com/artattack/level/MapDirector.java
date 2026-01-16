package com.artattack.level;

import java.util.ArrayList;
import java.util.List;

import com.artattack.interactions.Talk;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.Weapon;
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
                AreaBuilder areaBuilder = new AreaBuilder();
                areaBuilder.addShape("base");
                List<Coordinates> moveArea = areaBuilder.getResult();
                this.builder.setDimension(32, 26);
                this.builder.setEnemies(
                    List.of(new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, null, null, null, null, 0)));
                TriggerGroup firstDialogue = new TriggerGroup(new Talk(new InteractionPanel(), 
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
                            "Go teach him a lesson! I'll teach you how!")));
                TriggerGroup chestDialogue = new TriggerGroup(new Talk(new InteractionPanel(),
                    List.of("Test")));
                this.builder.addTriggerGroup(chestDialogue, new Coordinates(1, 21), 12, 4);
                this.builder.setInteractableElements(new ArrayList<>());
                this.builder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(29, 23), List.of(new Weapon("Hoe", "", 0)), 5,5, moveArea, 20, 20, 0, 20, 1, 5, 2, new ArrayList<>(), null, null));
                this.builder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(26, 23),List.of(new Weapon("Hoe", "", 0)), 5, 5 , moveArea, 20, 20, 0, 20, 1, 5, 2, new ArrayList<>(), null, null));
                this.builder.setDict();
                this.builder.setTurnQueue();
                this.builder.startMap();
                break;
        }
    }
}
