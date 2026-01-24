package com.artattack.level;

import java.util.ArrayList;
import java.util.List;

import com.artattack.interactions.Give;
import com.artattack.interactions.SwitchMap;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.Weapon;

public class MapDirector {
    private MapBuilder builder;
    private MapBuilder builder_;

    public MapDirector(MapBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(MapBuilder builder) {
        this.builder = builder;
    }

    public void make(String type) {
        switch (type) {
            case "Tutorial":
                //Creating Areas
                AreaBuilder areaBuilder = new AreaBuilder();
                areaBuilder.addShape("base");
                List<Coordinates> moveArea = areaBuilder.getResult();
                areaBuilder.addShape("square", 10, true);
                List<Coordinates> bigArea = areaBuilder.getResult();
                areaBuilder.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea = areaBuilder.getResult();



                //Creating Players
                Player p1 = new Player(1, '@', "Zappa", new Coordinates(29, 23), List.of(new Weapon("Hoe", "", 0)), 5,5, bigArea, 20, 20, PlayerType.MUSICIAN, 0, 20, 1, 5, 2, new ArrayList<>(), null, null);
                Player p2 = new Player(0, '@', "Lynch", new Coordinates(26, 23),List.of(new Weapon("Hoe", "", 0)), 5, 5 , bigArea, 20, 20, PlayerType.MOVIE_DIRECTOR, 0, 20, 1, 5, 2, new ArrayList<>(), null, null);

                //Creating Enemies
                Enemy e = new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea, enemyVisionArea, null, null, 0);

                //Creating InteractableElements
                InteractableElement chest = new InteractableElement(0, 'O', "Chest", new Coordinates(1, 23), List.of(
                    new Give(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("ALready opened."))),"");
                InteractableElement npc = new InteractableElement(0, 'T', "John Belushi", new Coordinates(22, 21), List.of(new Talk(
                    List.of("You need me to explain again?",
                            "Aw, man... I was hopin' you wouldn't... Anyways, here goes.",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
                            "Or, you can position your CURSOR on something that interests you and interact with it using the E button. You can also talk to people this way!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Understood? Alright, go and beat up that fella over there."))), "");
                make("Test");
                InteractableElement door = new InteractableElement(0, 'H', "Door", new Coordinates(0, 2), 
                List.of(new SwitchMap(1, List.of(new Coordinates(1, 1), new Coordinates(1, 2)))),"");

                //Creating TriggerGroups
                TriggerGroup firstDialogue = new TriggerGroup(new Talk( 
                    List.of("Hey, over here! Mr. Zappa! Mr. Lynch!", 
                            "What a mess! Look at what they've done to our peaceful resting place! It's now a soul-harvesting data center! You need to do something about this, or else we'll never be able to return to our well-earned slumber!",
                            "Me? Oh, I'm nowhere near as powerful as you guys, I wouldn't last a second against these guys.",
                            "You guys don't look so good... What happened to you?",
                            "Oh! Right! You two were dead, just like me! Hahahaha!",
                            "What's that? You don't remember how to do things? Oh yeah, I guess it makes sense considering you were six feet deep just a few moments ago... You must be rusty on what it takes to walk the earth.",
                            "But it's fine, I can teach you everything you need to know.",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
                            "Or, you can position your CURSOR on something that interests you and interact with it using the E button. You can also talk to people this way!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Now, go! Get to the top floor and defeat these power-hungry nerds!",
                            "Wait... Look! That red guy over there! It's an employee! You have to defeat him to get to the next floor!",
                            "Go teach him a lesson!")));
                TriggerGroup chestDialogue = new TriggerGroup(new Talk(
                    List.of("Whoa, is that a CHEST?", 
                            "I thought everything was stored in the cloud these days!",
                            "There might be something valuable inside! Go next to it, position your cursor on top of it then press E!")));
                TriggerGroup visionAreaDialogue = new TriggerGroup(new Talk(
                    List.of("Alright, now that you're approaching the enemy, I need you to listen up!",
                            "You see that red area around him? That's their line of sight! Or VISION AREA, if you'd like!",
                            "If you enter that area, they will notice you and become hostile!",
                            "So it's important that you get the jump on them by using a RANGED MOVE from your MOVE INVENTORY!",
                            "Press F to browse your MOVE INVENTORY. Then, after choosing a MOVE, press ENTER to use it!")));
                TriggerGroup turnQueueDialogue = new TriggerGroup(new Talk(
                    List.of("Okay, now they're mad! As you can see, the enemy has now entered the TURN QUEUE! It tells you what the order of turns is!",
                            "As you can see, the QUEUE is numbered, and the slot belonging to the person who's turn it is will be highlighted.",
                            "When all your ACTION POINTS are depleted, your turn will pass and it'll be the next person in the queue's turn!",
                            "Instead, if it's your turn and you don't want to do anything else, press SPACE to pass your turn! We don't have all day, you know!")));

                //Building Map
                this.builder.setDimension(32, 26);
                this.builder.setPlayerOne(p1);
                this.builder.setPlayerTwo(p2);
                this.builder.setEnemies(List.of(e));
                this.builder.setInteractableElements(List.of(npc, chest, door));
                this.builder.addTriggerGroup(chestDialogue, new Coordinates(1, 21), 12, 4);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(1, 6), 12, 9);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(1, 1), 3, 5);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(9, 1), 4, 5);
                this.builder.addTriggerGroup(turnQueueDialogue, new Coordinates(4, 1), 5, 5);
                this.builder.setDict();
                this.builder.setTurnQueue();
                this.builder.startMap();
                break;
            case "Test":
                this.builder_ = new TestMapBuilder();
                builder_.setDimension(20, 20);
                builder_.setEnemies(List.of());
                builder_.setDict();
                builder_.startMap();
                break;
        }
    }
}
