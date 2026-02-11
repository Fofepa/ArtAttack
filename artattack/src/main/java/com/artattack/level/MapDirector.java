package com.artattack.level;

import java.util.ArrayList;
import java.util.List;

import com.artattack.interactions.CheckPoint;
import com.artattack.interactions.Give;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.mapelements.ConcreteEnemyBuilder;
import com.artattack.mapelements.ConcreteInteractableElementBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyDirector;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.InteractableElementDirector;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.MoveBuilder1;

public class MapDirector {
    private MapBuilder builder;
    private MoveBuilder1 mb1;
    private AreaBuilder ab;
    private ConcreteEnemyBuilder eBuilder;
    private ConcreteInteractableElementBuilder ieBuilder;
    private InteractableElementDirector ieDirector;
    private EnemyDirector eDirector;

    public MapDirector(MapBuilder builder) {
        this.builder = builder;
        this.mb1 = new MoveBuilder1();
        this.ab = new AreaBuilder();
        this.eBuilder = new ConcreteEnemyBuilder();
        this.ieBuilder = new ConcreteInteractableElementBuilder();
        this.ieDirector = new InteractableElementDirector();
        this.eDirector = new EnemyDirector();
    }

    public void changeBuilder(MapBuilder builder) {
        this.builder = builder;
    }

    public void make(String type) {
        switch (type) {
            case "Tutorial":
                //Creating Enemies
                eDirector.create(eBuilder, EnemyType.EMPLOYEE, new Coordinates(6, 3));
                Enemy e = eBuilder.getResult();
                List<Enemy> listE = new ArrayList<>();
                listE.add(e);

                //Creating InteractableElements
                ieBuilder.setID(0); ieBuilder.setMapSymbol('O'); ieBuilder.setName("Chest"); ieBuilder.setCoordinates(new Coordinates(1, 23));
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                InteractableElement chest_t = ieBuilder.getResult();
                ieBuilder.setID(1); ieBuilder.setMapSymbol('M'); ieBuilder.setName("George Melies"); ieBuilder.setCoordinates(new Coordinates(29, 22));
                ieBuilder.setInteractions(List.of(new Talk(
                    List.of("You need me to explain again?",
                            "Aw, man... I was hopin' you wouldn't... Anyways, here goes.",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
                            "Instead, if you see something that interests you, you can position your CURSOR on it and press the E key to interact with it. You can also talk to people this way!",
                            "Keep in mind that if you want to interact with something, you have to move next to it first!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Understood? Alright, go and beat up that fella over there."))));
                InteractableElement npc_t = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 1, new Coordinates(0, 2)); ieBuilder.setID(2);
                InteractableElement door_t = ieBuilder.getResult();

                //Creating TriggerGroups
                TriggerGroup firstDialogue = new TriggerGroup(new Talk( 
                    List.of("Hey, over here! Mr. Zappa! Mr. Lynch!", 
                            "What a mess! Look at what they've done to our peaceful resting place! It's now a soul-harvesting data center! You need to do something about this, or else we'll never be able to return to our well-earned slumber!",
                            "Me? I'm George Melies? You gentlemen surely know who I am! I hate this place, last century the world wasn't nothing like this!", 
                            "I hate this place with all my heart!", "Oh, I'm nowhere near as powerful as you guys, I wouldn't last a second against these guys.",
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
                this.builder.setSpawn(new Coordinates(29, 23), new Coordinates(26, 23));
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
                //Creating Employee Map 1
                eDirector.create(eBuilder, EnemyType.EMPLOYEE, new Coordinates(28,13));
                Enemy e0_1 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.EMPLOYEE, new Coordinates(9,21));
                Enemy e1_1 = eBuilder.getResult();
                //Creating Guard Map 1
                eDirector.create(eBuilder, EnemyType.GUARD, new Coordinates(20,21));
                Enemy e2_1 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.GUARD, new Coordinates(2,11));
                Enemy e3_1 = eBuilder.getResult();
                List<Enemy> listEn = new ArrayList<>();
                listEn.addAll(List.of(e0_1,e1_1, e2_1, e3_1));

                //Creating InteractableElement
                ieBuilder.setID(4); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(29, 29)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                InteractableElement chest0_1 = ieBuilder.getResult();
                ieBuilder.setID(5); ieBuilder.setCoordinates(new Coordinates(9, 11)); ieBuilder.setInteractions(List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                InteractableElement chest1_1 = ieBuilder.getResult();
                InteractableElement checkpoint_1 = new InteractableElement(0, 'C', "checkpoint", new Coordinates(30, 30), List.of(new CheckPoint(List.of("OK"))), "");
                ieDirector.createDoor(ieBuilder, 2, new Coordinates(0, 4)); ieBuilder.setID(6);
                InteractableElement door_1 = ieBuilder.getResult();
                
                this.builder.setID(1);
                this.builder.setDimension(32, 32);
                this.builder.setSpawn(new Coordinates(28, 2), new Coordinates(28, 4));
                this.builder.setEnemies(listEn);
                this.builder.setInteractableElements(List.of(chest0_1, chest1_1, checkpoint_1, door_1));
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
                break;
            case "BossRoom1":
                // Boss creation
                eDirector.create(eBuilder, EnemyType.BOB, new Coordinates(15, 4));
                Enemy boss = eBuilder.getResult();
                // minions creation
                eDirector.create(eBuilder, EnemyType.TOOLBOT, new Coordinates(12, 6));
                Enemy minion1 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.TOOLBOT, new Coordinates(18, 6));
                Enemy minion2 = eBuilder.getResult();
                // creation of the interactable elements
                ieBuilder.setID(7); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(30, 36)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                InteractableElement chest2_1 = ieBuilder.getResult();
                ieDirector.createCheckPoint(ieBuilder, new Coordinates(9, 34)); ieBuilder.setID(8);
                InteractableElement bossCheckPoint = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 3, 5001, new Coordinates(16, 0)); ieBuilder.setID(9);
                InteractableElement endDoor = ieBuilder.getResult();
                
                List<Enemy> enemyBossRoom = new ArrayList<>(); enemyBossRoom.addAll(List.of(boss, minion1, minion2));
                

                this.builder.setID(2);
                this.builder.setDimension(32, 40);
                this.builder.setSpawn(new Coordinates(14, 37), new Coordinates(17, 37));
                this.builder.buildBorder();

                this.builder.setEnemies(enemyBossRoom);
                this.builder.setInteractableElements(List.of(chest2_1, bossCheckPoint, endDoor));
                
                // Top big blocks (left)
                this.builder.buildWall(new Coordinates(3, 4), 4, 4, '#');
                
                // Top big blocks (right) 
                this.builder.buildWall(new Coordinates(25, 4), 4, 4, '#');
                
                // Middle-upper small blocks (left)
                this.builder.buildWall(new Coordinates(7, 9), 2, 2, '#');
                
                // Middle-upper small blocks (right)
                this.builder.buildWall(new Coordinates(23, 9), 2, 2, '#');
                
                // Middle small blocks (left)
                this.builder.buildWall(new Coordinates(5, 14), 2, 2, '#');
                
                // Middle small blocks (right)
                this.builder.buildWall(new Coordinates(25, 14), 2, 2, '#');
                
                // Bottom staircase (left)
                this.builder.buildWall(new Coordinates(4, 20), 2, 1, '#');
                this.builder.buildWall(new Coordinates(5, 21), 2, 1, '#');
                this.builder.buildWall(new Coordinates(6, 22), 2, 1, '#');
                
                // Bottom staircase (right)
                this.builder.buildWall(new Coordinates(26, 20), 2, 1, '#');
                this.builder.buildWall(new Coordinates(25, 21), 2, 1, '#');
                this.builder.buildWall(new Coordinates(24, 22), 2, 1, '#');
                
                // Bottom horizontal wall - LEFT PART (connected to left border)
                this.builder.buildWall(new Coordinates(1, 30), 13, 4, '#');
                
                // Bottom horizontal wall - RIGHT PART (connected to right border)
                this.builder.buildWall(new Coordinates(18, 30), 13, 4, '#');
                
                break;
            case "Reception":
                this.builder.setID(3);
                this.builder.setDimension(40, 15);
                this.builder.setSpawn(new Coordinates(19, 1), new Coordinates(20, 1));
                this.builder.buildBorder();
                
                //right corner
                this.builder.buildWall(new Coordinates(1, 1), 4, 2, '#');
                this.builder.buildWall(new Coordinates(1, 3), 2, 2, '#');
                this.builder.buildWall(new Coordinates(1, 5), 1, 1, '#');
                this.builder.buildWall(new Coordinates(3, 3), 1, 1, '#');
                this.builder.buildWall(new Coordinates(5, 1), 1, 1, '#');
                
                //left corner
                this.builder.buildWall(new Coordinates(35, 1), 4, 2, '#');
                this.builder.buildWall(new Coordinates(37, 3), 2, 2, '#');
                this.builder.buildWall(new Coordinates(38, 5), 1, 1, '#');
                this.builder.buildWall(new Coordinates(36, 3), 1, 1, '#');
                this.builder.buildWall(new Coordinates(34, 1), 1, 1, '#');

                //counter
                this.builder.buildWall(new Coordinates(17, 11), 7, 1, '#');
                this.builder.buildWall(new Coordinates(17, 12), 1, 2, '#');
                this.builder.buildWall(new Coordinates(23, 12), 1, 2, '#');

                //decoration
                this.builder.buildWall(new Coordinates(2, 12), 1, 1, '\u2318');
                this.builder.buildWall(new Coordinates(37, 12), 1, 1, '\u2318');

                //doors
                ieDirector.createDoor(ieBuilder, 4, new Coordinates(39, 10)); ieBuilder.setID(10);
                InteractableElement arenaDoor_r = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 5, new Coordinates(0, 10)); ieBuilder.setID(11);
                InteractableElement preBossDoor_r = ieBuilder.getResult();
                
                //chests
                ieBuilder.setID(12); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(4, 12)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))));
                InteractableElement chest_r0 = ieBuilder.getResult();
                ieBuilder.setID(13); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(4, 12)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))));
                InteractableElement chest_r1 = ieBuilder.getResult();

                //receptionist
                ieBuilder.setID(14); ieBuilder.setMapSymbol('R'); ieBuilder.setCoordinates(new Coordinates(20, 11)); ieBuilder.setName("R.E.N.E. 3000");
                ieBuilder.setInteractions(List.of(
                    new Talk(List.of("Welcome to the I.A.A.I. Facility 52, I'm R.E.N.E 30000 at your service!", "What!?!? You're here to stop Sam Altman? I don't think I can help you with that...",
                     "I mean, I'm not even from the I.A.A.I. To be honest, the place I come from is Milan, in Italy", "You know, I was one of the first AI movie directors, I even won a Cannes festival!", 
                     "Back in the days when people were enthusiastic about my Italian accent! I used to say things like \"DAI! DAI! DAI!\" or \"Azione!\" but now I'm just an AI receptionist...",
                     "But thats all! If you guys want to meet Sam I'm afraid he might be unavailable for the whole day!", "What?!? Are you sure you are his cousin? I mean, you don't look so much alive...",
                     "I don't mean to offend you of course! Can I get some ID?", "WHAT?!? 01 20 1946?!?! Sam never told me about an old cousin...", "Sorry guys, I cannot let you in...", 
                     "Wait... YOU ARE DAVID LYNCH?!?!", "HOW ARE YOU HERE!! I MUST BE DRUNK OR SOMETHING!", "Wait... I am an AI, surely I cannot get drunk...", 
                     "SO YOU ARE THE REAL LYNCH?!?! Like Blue Velvet real? OH MY GOD... What a pleasure to meet you sir, I trained my model on you...", 
                     "Sorry but I still can't get the appointment with mr. Altman, maybe I can help you with something else!", "A key to the door on the left? Mhmm... Oh the key is just after the Waiting room for the staff!",
                     "You don't know how to get there? Go in the room to my left, proceed through the hallway and then take the door right in front of you.",
                     "The keys to the office area should be there!", "Thank you!", "Good luck with the meeting then!")), new Talk(List.of("Thank you!", "Good luck with the meeting then!"))
                ));
                InteractableElement receptionist = ieBuilder.getResult();

                this.builder.setInteractableElements(List.of(preBossDoor_r, arenaDoor_r, receptionist, chest_r0, chest_r1));
                this.builder.setEnemies(new ArrayList<>());
                break;
            case "BigEnemyArea":
                this.builder.setID(4);
                //enemies
                eDirector.create(eBuilder, EnemyType.MOSQUITO, new Coordinates(9, 6));
                Enemy mosquito = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.ROBOT, new Coordinates(18, 10));
                Enemy roboguard_0 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.ROBOT, new Coordinates(14, 14));
                Enemy roboguard_1 = eBuilder.getResult();

                //Creating map
                this.builder.setDimension(25, 25);
                this.builder.setSpawn(new Coordinates(1, 1), new Coordinates(1, 3));
                this.builder.buildBorder();

                //top wall
                this.builder.buildWall(new Coordinates(13, 1), 7, 1, '#');
                this.builder.buildWall(new Coordinates(12, 2), 7, 1, '#');
                this.builder.buildWall(new Coordinates(11, 3), 7, 1, '#');
                this.builder.buildWall(new Coordinates(10, 4), 7, 1, '#');
                this.builder.buildWall(new Coordinates(11, 5), 5, 1, '#');
                this.builder.buildWall(new Coordinates(12, 6), 3, 1, '#');
                this.builder.buildWall(new Coordinates(13, 7), 1, 1, '#');

                //left wall
                this.builder.buildWall(new Coordinates(1, 19), 1, 1, '#');
                this.builder.buildWall(new Coordinates(1, 18), 2, 1, '#');
                this.builder.buildWall(new Coordinates(1, 17), 3, 1, '#');
                this.builder.buildWall(new Coordinates(1, 16), 4, 1, '#');
                this.builder.buildWall(new Coordinates(1, 15), 5, 1, '#');
                this.builder.buildWall(new Coordinates(1, 14), 6, 1, '#');
                this.builder.buildWall(new Coordinates(1, 13), 7, 1, '#');
                this.builder.buildWall(new Coordinates(2, 12), 7, 1, '#');
                this.builder.buildWall(new Coordinates(3, 11), 7, 1, '#');
                this.builder.buildWall(new Coordinates(4, 10), 7, 1, '#');
                this.builder.buildWall(new Coordinates(5, 9), 5, 1, '#');
                this.builder.buildWall(new Coordinates(6, 8), 3, 1, '#');
                this.builder.buildWall(new Coordinates(7, 7), 1, 1, '#');

                //barriers
                this.builder.buildWall(new Coordinates(14, 12), 1, 1, '#');
                this.builder.buildWall(new Coordinates(13, 13), 1, 1, '#');
                this.builder.buildWall(new Coordinates(12, 14), 1, 1, '#');
                
                this.builder.buildWall(new Coordinates(18, 8), 1, 1, '#');
                this.builder.buildWall(new Coordinates(17, 9), 1, 1, '#');
                this.builder.buildWall(new Coordinates(16, 10), 1, 1, '#');

                //benches
                this.builder.buildWall(new Coordinates(5, 23), 4, 1, '=');
                this.builder.buildWall(new Coordinates(13, 23), 4, 1, '=');

                this.builder.setEnemies(new ArrayList<>(List.of(mosquito, roboguard_0, roboguard_1)));

                //interactable elements
                ieDirector.createDoor(ieBuilder, 3, new Coordinates(0, 2)); ieBuilder.setID(15);
                InteractableElement receptionDoor_B = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 7, new Coordinates(24, 3)); ieBuilder.setID(16);
                InteractableElement keyRoomDoor_B = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 6, new Coordinates(10, 24)); ieBuilder.setID(17);
                InteractableElement chestRoomDoor_B = ieBuilder.getResult();

                this.builder.setInteractableElements(List.of(receptionDoor_B, keyRoomDoor_B, chestRoomDoor_B));
                break;
            case "PreBoss":
                eDirector.create(eBuilder, EnemyType.GUARD, new Coordinates(21, 2));
                Enemy guard1 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.GUARD, new Coordinates(21, 7));
                Enemy guard2 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.ROBOT, new Coordinates(6, 6));
                Enemy roboguard1 = eBuilder.getResult();
                eDirector.create(eBuilder, EnemyType.ROBOT, new Coordinates(6, 3));
                Enemy roboguard2 = eBuilder.getResult();
                this.builder.setEnemies(new ArrayList<Enemy>(List.of(guard1, guard2, roboguard1, roboguard2)));
                
                this.builder.setID(5);
                this.builder.setDimension(32, 10);
                this.builder.buildBorder();
                
                //top wall
                this.builder.buildWall(new Coordinates(1, 1), 17, 1, '#');
                this.builder.buildWall(new Coordinates(1, 2), 5, 1, '#');
                this.builder.buildWall(new Coordinates(1, 3), 3, 1, '#');
                this.builder.buildWall(new Coordinates(13, 2), 5, 1, '#');
                this.builder.buildWall(new Coordinates(15, 3), 3, 1, '#');
                
                //bottom wall
                this.builder.buildWall(new Coordinates(1, 6), 3, 1, '#');
                this.builder.buildWall(new Coordinates(1, 7), 5, 1, '#');
                this.builder.buildWall(new Coordinates(1, 8), 17, 1, '#');
                this.builder.buildWall(new Coordinates(15, 6), 3, 1, '#');
                this.builder.buildWall(new Coordinates(13, 7), 5, 1, '#');
                
                //barriers
                this.builder.buildWall(new Coordinates(22, 6), 1, 2, '#');
                this.builder.buildWall(new Coordinates(22, 2), 1, 2, '#');
                break;
            case "ChestRoom":
                this.builder.setID(6);
                this.builder.setDimension(10, 10);
                this.builder.setSpawn(new Coordinates(3, 1), new Coordinates(5, 1));
                this.builder.buildBorder();
                
                //bottom wall
                this.builder.buildWall(new Coordinates(1, 8), 8, 1, '#');
                this.builder.buildWall(new Coordinates(2, 7), 6, 1, '#');
                this.builder.buildWall(new Coordinates(3, 6), 4, 1, '#');
                
                ieDirector.createChest(ieBuilder, List.of(new Item(ItemType.SPEED_BUFF, "fuck", "should be a weapon", 2)), new Coordinates(4, 6));
                InteractableElement chest0_c = ieBuilder.getResult();
                ieDirector.createChest(ieBuilder, List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)), new Coordinates(5, 6));
                InteractableElement chest1_c = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 4, new Coordinates(7, 0));
                InteractableElement BDoor_c = ieBuilder.getResult();

                this.builder.setInteractableElements(List.of(chest0_c, chest1_c, BDoor_c));
                this.builder.setEnemies(new ArrayList<>());
                break;
            case "KeyRoom":
                this.builder.setID(7);
                this.builder.setDimension(10, 10);
                this.builder.setSpawn(new Coordinates(1, 3), new Coordinates(1, 5));
                this.builder.buildBorder();
                
                //closet
                this.builder.buildWall(new Coordinates(1, 9), 1, 1, '#');

                //obstacles
                this.builder.buildWall(new Coordinates(4, 9), 3, 1, '#');
                this.builder.buildWall(new Coordinates(5, 2), 1, 5, '#');
                this.builder.buildWall(new Coordinates(4, 3), 1, 1, '#');
                this.builder.buildWall(new Coordinates(4, 5), 1, 1, '#');
                
                //interactable element
                ieDirector.createDoor(ieBuilder, 4, new Coordinates(0, 2));
                this.builder.setInteractableElements(List.of(ieBuilder.getResult()));

                //enemy
                eDirector.create(eBuilder, EnemyType.ATTENDANT, new Coordinates(7, 5));
                this.builder.setEnemies(new ArrayList<>(List.of(eBuilder.getResult())));
                break;
            case "BossArena":
                this.builder.setID(8);
                this.builder.setDimension(18, 12);
                this.builder.buildBorder();
                this.builder.setSpawn(new Coordinates(15, 2), new Coordinates(15, 4));

                //decorations
                this.builder.buildWall(new Coordinates(16, 1), 1, 1, '\u2380');
                this.builder.buildWall(new Coordinates(16, 10), 1, 1, '\u2380');
                
                //walls
                this.builder.buildWall(new Coordinates(7, 10), 3, 1, '$');
                this.builder.buildWall(new Coordinates(7, 1), 3, 1, '$');

                this.builder.buildWall(new Coordinates(8, 9), 1, 1, '$');
                this.builder.buildWall(new Coordinates(8, 2), 1, 1, '$');

                //desk
                this.builder.buildWall(new Coordinates(4, 2), 7, 1, '£');
                this.builder.buildWall(new Coordinates(1, 2), 2, 1, '£');
                this.builder.buildWall(new Coordinates(1, 8), 2, 1, '£');

                //enemy
                eDirector.create(eBuilder, EnemyType.SAM, new Coordinates(2, 4));
                this.builder.setEnemies(new ArrayList<>(List.of(eBuilder.getResult())));
                break;
        }
    }
}
