package com.artattack.level;

import java.util.ArrayList;
import java.util.List;

import com.artattack.interactions.CheckPoint;
import com.artattack.interactions.Give;
import com.artattack.interactions.SwitchMap;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

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
                //Creating Areas
                AreaBuilder ab_t = new AreaBuilder();
                ab_t.addShape("base");
                List<Coordinates> moveArea_t = ab_t.getResult();
                ab_t.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_t = ab_t.getResult();

                //Creating Enemies
                ab_t.addShape("8");
                List<Coordinates> moveArea = ab_t.getResult();
                ab_t.addShape("4");
                List<Coordinates> area4 = ab_t.getResult();
                Move m1 = new Move(); m1.setName("Kick"); m1.setPower(1); m1.setAttackArea(area4); m1.setActionPoints(3); m1.setAreaAttack(false);
                Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(area4); m2.setActionPoints(4); m2.setAreaAttack(false);
                Move m3 = new Move(); m3.setName("Explode"); m3.setPower(3); m3.setAttackArea(moveArea); m3.setAreaAttack(true); m3.setActionPoints(3);
                Weapon enemyWeapon = new Weapon(" ", " ", 5, List.of(m1,m2, m3), null);
                Enemy e = new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 20, 20, 2, List.of(enemyWeapon), 10, 10, moveArea_t, enemyVisionArea_t, null, null, 20);
                List<Enemy> listE = new ArrayList<>();
                listE.add(e);

                //Creating InteractableElements
                InteractableElement chest_t = new InteractableElement(0, 'O', "Chest", new Coordinates(1, 23), List.of(
                    new Give(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))),"");
                InteractableElement npc_t = new InteractableElement(0, 'T', "John Belushi", new Coordinates(29, 22), List.of(new Talk(
                    List.of("You need me to explain again?",
                            "Aw, man... I was hopin' you wouldn't... Anyways, here goes.",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
                            "Instead, if you see something that interests you, you can position your CURSOR on it and press the E key to interact with it. You can also talk to people this way!",
                            "Keep in mind that if you want to interact with something, you have to move next to it first!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Understood? Alright, go and beat up that fella over there."))), "");
                InteractableElement door_t = new InteractableElement(0, '\u2339', "Door", new Coordinates(0, 2), 
                List.of(new SwitchMap(1, List.of(new Coordinates(28, 2), new Coordinates(28, 4)))),"");

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
                this.builder.setID(0);
                this.builder.setDimension(32, 26);
                this.builder.setEnemies(listE);
                this.builder.setInteractableElements(List.of(npc_t, chest_t, door_t));
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
            case "1":
                //Creating Areas
                AreaBuilder ab_1 = new AreaBuilder();
                ab_1.addShape("base");
                List<Coordinates> moveArea_1 = ab_1.getResult();
                ab_1.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_1 = ab_1.getResult();

                ab_1.addShape("8");
                List<Coordinates> mArea = ab_1.getResult();
                ab_1.addShape("4");
                List<Coordinates> a4 = ab_1.getResult();

                
                Move m11 = new Move(); m11.setName("Kick"); m11.setPower(1); m11.setAttackArea(a4); m11.setActionPoints(3); m11.setAreaAttack(false);
                Move m22 = new Move(); m22.setName("Bump"); m22.setPower(5); m22.setAttackArea(a4); m22.setActionPoints(4); m22.setAreaAttack(false);
                Move m33 = new Move(); m33.setName("Explode"); m33.setPower(3); m33.setAttackArea(mArea); m33.setAreaAttack(true); m33.setActionPoints(3);
                Weapon eWeapon = new Weapon(" ", " ", 5, List.of(m11,m22, m33), null);
                Enemy e0_1 = new Enemy(0, 'E', "Employee", new Coordinates(28, 13), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e1_1 = new Enemy(0, 'E', "Employee", new Coordinates(20, 21), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e2_1 = new Enemy(0, 'E', "Employee", new Coordinates(9, 21), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e3_1 = new Enemy(0, 'E', "Employee", new Coordinates(2, 11), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 40);
                List<Enemy> listEn = new ArrayList<>();
                listEn.addAll(List.of(e0_1,e1_1, e2_1, e3_1));

                InteractableElement chest0_1 = new InteractableElement(0, '$', "Chest", new Coordinates(29, 29), List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))), null);
                InteractableElement chest1_1 = new InteractableElement(0, '$', "Chest", new Coordinates(9, 11), List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))), null);
                InteractableElement checkpoint_1 = new InteractableElement(0, 'C', "checkpoint", new Coordinates(30, 30), List.of(new CheckPoint(List.of("OK"))), "");

                this.builder.setDimension(32, 32);
                this.builder.setEnemies(listEn);
                this.builder.setInteractableElements(List.of(chest0_1, chest1_1, checkpoint_1));
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
