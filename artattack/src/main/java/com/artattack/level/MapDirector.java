package com.artattack.level;

import java.util.List;

import com.artattack.interactions.GiveFactory;
import com.artattack.interactions.TalkFactory;
import com.artattack.items.Cure;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.TriggerGroup;

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
                AreaBuilder ab_t = new AreaBuilder();
                ab_t.addShape("base");
                List<Coordinates> moveArea_t = ab_t.getResult();
                ab_t.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_t = ab_t.getResult();

                //Creating Enemies
                Enemy e = new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea_t, enemyVisionArea_t, null, null, 0);

                //Creating InteractableElements
                InteractableElement chest_t = new InteractableElement(0, 'O', "Chest", new Coordinates(1, 23), List.of(
                    new GiveFactory(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), new Cure("Cure", "Heals 5 HP.", 5)).createInteraction()), null, null, null);
                InteractableElement npc_t = new InteractableElement(0, 'T', "John Belushi", new Coordinates(29, 22), List.of(new TalkFactory(
                    List.of("You need me to explain again?",
                            "Aw, man... I was hopin' you wouldn't... Anyways, here goes.",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
                            "Instead, if you see something that interests you, you can position your CURSOR on it and press the E key to interact with it. You can also talk to people this way!",
                            "Keep in mind that if you want to interact with something, you have to move next to it first!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Understood? Alright, go and beat up that fella over there.")).createInteraction()), null, null, null);

                //Creating TriggerGroups
                TriggerGroup chestDialogue = new TriggerGroup(new TalkFactory(
                    List.of("Whoa, is that a CHEST?", 
                            "I thought everything was stored in the cloud these days!",
                            "There might be something valuable inside! Go next to it, position your cursor on top of it then press E!")).createInteraction());
                TriggerGroup visionAreaDialogue = new TriggerGroup(new TalkFactory(
                    List.of("Alright, now that you're approaching the enemy, I need you to listen up!",
                            "You see that red area around him? That's their line of sight! Or VISION AREA, if you'd like!",
                            "If you enter that area, they will notice you and become hostile!",
                            "So it's important that you get the jump on them by using a RANGED MOVE from your MOVE INVENTORY!",
                            "Press F to browse your MOVE INVENTORY. Then, after choosing a MOVE, press ENTER to use it!")).createInteraction());
                TriggerGroup turnQueueDialogue = new TriggerGroup(new TalkFactory(
                    List.of("Okay, now they're mad! As you can see, the enemy has now entered the TURN QUEUE! It tells you what the order of turns is!",
                            "As you can see, the QUEUE is numbered, and the slot belonging to the person who's turn it is will be highlighted.",
                            "When all your ACTION POINTS are depleted, your turn will pass and it'll be the next person in the queue's turn!",
                            "Instead, if it's your turn and you don't want to do anything else, press SPACE to pass your turn! We don't have all day, you know!")).createInteraction());

                //Building Map
                this.builder.setDimension(32, 26);
                this.builder.setEnemies(List.of(e));
                this.builder.setInteractableElements(List.of(npc_t, chest_t));
                this.builder.addTriggerGroup(chestDialogue, new Coordinates(1, 21), 12, 4);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(1, 6), 12, 9);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(1, 1), 3, 5);
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(9, 1), 4, 5);
                this.builder.addTriggerGroup(turnQueueDialogue, new Coordinates(4, 1), 5, 5);
                this.builder.buildBorder();
                this.builder.buildWall(new Coordinates(19, 21), 1, 4, '#');
                this.builder.buildWall(new Coordinates(22, 20), 9, 1, '#');
                this.builder.buildWall(new Coordinates(1, 19), 12, 2, '#');
                this.builder.buildWall(new Coordinates(1, 15), 10, 1, '#');
                this.builder.buildWall(new Coordinates(13, 1), 2, 3, '#');
                this.builder.buildWall(new Coordinates(14, 12), 3, 2, '#');
                this.builder.buildWall(new Coordinates(24, 11), 7, 1, '#');
                break;
            case "Test":
                this.builder_ = new TestMapBuilder();
                builder_.setDimension(20, 20);
                builder_.setEnemies(List.of());
                break;
            case "1":
                //Creating Areas
                AreaBuilder ab_1 = new AreaBuilder();
                ab_1.addShape("base");
                List<Coordinates> moveArea_1 = ab_1.getResult();
                ab_1.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_1 = ab_1.getResult();

                Enemy e0_1 = new Enemy(0, 'E', "Employee", new Coordinates(28, 13), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea_1, enemyVisionArea_1, null, null, 0);
                Enemy e1_1 = new Enemy(0, 'E', "Employee", new Coordinates(20, 21), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea_1, enemyVisionArea_1, null, null, 0);
                Enemy e2_1 = new Enemy(0, 'E', "Employee", new Coordinates(9, 21), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea_1, enemyVisionArea_1, null, null, 0);
                Enemy e3_1 = new Enemy(0, 'E', "Employee", new Coordinates(2, 11), EnemyType.EMPLOYEE, 0, 0, 0, null, 0, 0, moveArea_1, enemyVisionArea_1, null, null, 0);

                InteractableElement chest0_1 = new InteractableElement(0, '$', "Chest", new Coordinates(29, 29), List.of(
                new GiveFactory(List.of("You found a Cure!"), new Cure("Cure", "Heals 5 HP.", 5)).createInteraction()), null, null, null);
                InteractableElement chest1_1 = new InteractableElement(0, '$', "Chest", new Coordinates(9, 11), List.of(
                new GiveFactory(List.of("You found a Cure!"), new Cure("Cure", "Heals 5 HP.", 5)).createInteraction()), null, null, null);

                this.builder.setDimension(32, 32);
                this.builder.setEnemies(List.of(e0_1, e1_1, e2_1, e3_1));
                this.builder.setInteractableElements(List.of(chest0_1, chest1_1));
                this.builder.buildBorder();
                this.builder.buildWall(new Coordinates(11, 1), 8, 25, '#');
                this.builder.buildWall(new Coordinates(14, 28), 1, 3, '#');
                this.builder.buildWall(new Coordinates(1, 14), 4, 1, '#');
                this.builder.buildWall(new Coordinates(8, 8), 3, 1, '#');
                this.builder.buildWall(new Coordinates(11, 26), 1, 2, '#');
                this.builder.buildWall(new Coordinates(18, 26), 1, 2, '#');
                this.builder.buildWall(new Coordinates(23, 11), 8, 1, '#');
                this.builder.buildWall(new Coordinates(19, 19), 7, 1, '#');
                this.builder.buildWall(new Coordinates(2, 19), 9, 1, '#');
        }
    }
}
