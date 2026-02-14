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
import com.artattack.mapelements.PlayerType;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;
import com.artattack.moves.Weapon;

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
                ieBuilder.setID(0); ieBuilder.setMapSymbol('$'); ieBuilder.setName("Chest"); ieBuilder.setCoordinates(new Coordinates(1, 23));
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                ieBuilder.setSpritePath("/images/melis.jpg");
                InteractableElement chest_t = ieBuilder.getResult();
                ieBuilder.setID(1); ieBuilder.setMapSymbol('i'); ieBuilder.setName("Georges Méliès"); ieBuilder.setCoordinates(new Coordinates(29, 22));
                ieBuilder.setSpritePath("/images/melies.jpg");
                ieBuilder.setInteractions(List.of(new Talk(
                    List.of("You see all these panels in front of you? You can access all of them by pressing specific keys. You'll know when you're accessing a panel because it'll be highlighted.",
                            "If you press M, you'll access the MAP PANEL, where you can see the MAP. By focusing on it, you'll be able to interact with the world!",
                            "Each one of you has a CURSOR. It has many uses, like moving!",
                            "You can position your CURSOR using WASD or the Arrow Keys while you're focused on the MAP PANEL. Then, use the ENTER Key to move where the CURSOR is!",
                            "Or, you can position your CURSOR on something that interests you and INTERACT with it using the E Key. You can also talk to people this way!",
                            "Keep in mind that you have to be right next to something to INTERACT with it. After all, it has to be within an arm's reach!",
                            "That's the basics. If you want me to refresh your memory, just talk to me.",
                            "Understood? Alright, now go and beat up the bad guys."))));
                InteractableElement npc_t = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 1, new Coordinates(0, 2)); ieBuilder.setID(2);
                InteractableElement door_t = ieBuilder.getResult();

                //Creating TriggerGroups
                TriggerGroup chestDialogue = new TriggerGroup(new Talk(
                    List.of("Whoa, is that a CHEST?", 
                            "I thought everything was stored in the cloud these days!",
                            "There might be something valuable inside! You can grab it by INTERACTING with the CHEST.",
                            "The object inside the chest will be stored in your INVENTORY, displayed in the INVENTORY PANEL, at the top left. You can access it by pressing the I Key.",
                            "When an object's name in your INVENTORY is highlighted, a short description of its effect will be displayed.",
                            "If you have multiple objects in your INVENTORY, you can browse them by using WASD or the Arrow Keys. Then, you can USE them anytime by using the ENTER Key.")));
                TriggerGroup visionAreaDialogue = new TriggerGroup(new Talk(
                    List.of("Wait... Look! That red guy over there! It's an employee! You have to defeat him to get to the next floor!",
                            "Now that you're approaching an enemy, I need you to listen up!",
                            "You see that red area around him? That's their line of sight! Or VISION AREA, if you'd like!",
                            "If you enter that area, they will notice you and become hostile!",
                            "So it's important that you know how to defend yourself Press the F Key to access your WEAPON INVENTORY, on the left!",
                            "Every WEAPON has a set of MOVES that you can use. By using the D Key or Right Arrow Key while in the WEAPON INVENTORY, you'll be able to browse through a WEAPON's MOVES and their ATTACK AREA will be displayed in RED on the MAP!",
                            "You can use a MOVE by using the ENTER Key. If an enemy is within your MOVE's ATTACK AREA, they will be hit and lose HP equal to that MOVE's POWER!",
                            "However, not all MOVES do damage! In fact, some will heal your party! In that case, their HEAL AREA will be displayed in YELLOW!",
                            "Now, go teach him a lesson!")));
                TriggerGroup turnQueueDialogue = new TriggerGroup(new Talk(
                    List.of("Okay, now they're mad! As you can see, the enemy has now entered the TURN QUEUE, at the bottom right! It tells you what the order of turns is!",
                            "As you can see, the QUEUE is numbered, and the slot belonging to the person who's turn it is will be highlighted.",
                            "When all your ACTION POINTS are depleted, your turn will pass and it'll be the next person in the queue's turn!",
                            "Instead, if it's your turn and you don't want to do anything else, press SPACE to pass your turn! We don't have all day, you know!",
                            "Oh! And before I forget, once you defeat the enemy, you'll gain SKILL POINTS!",
                            "SKILL POINTS can be spent on your SKILL TREE, which you can open by using the O Key.",
                            "Every NODE on the SKILL TREE will grant you newfound power! But before you can spend a SKILL POINT on a NODE, you'll have to acquire the ones that come before it!", 
                            "You can browse through the available NODES to see what they will give you, then you can acquire its power by using the ENTER Key.",
                            "If you want to return to action, press the ESCAPE Key!")));

                //Building Map
                this.builder.setID(0);
                this.builder.setDimension(32, 26);
                this.builder.setSpawn(new Coordinates(29, 23), new Coordinates(26, 23));
                this.builder.setEnemies(listE);
                this.builder.setInteractableElements(List.of(npc_t, chest_t, door_t));
                this.builder.addTriggerGroup(chestDialogue, new Coordinates(1, 21), 12, 4, "/images/melies.jpg");
                this.builder.addTriggerGroup(visionAreaDialogue, new Coordinates(1, 9), 11, 6, "/images/melies.jpg");
                this.builder.addTriggerGroup(turnQueueDialogue, new Coordinates(1, 1), 11, 8, "/images/melies.jpg");
                this.builder.buildBorder();
                this.builder.buildWall(new Coordinates(18, 21), 2, 4, '\u2588');
                this.builder.buildWall(new Coordinates(22, 20), 9, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 19), 12, 2, '\u2588');
                this.builder.buildWall(new Coordinates(1, 15), 12, 2, '\u2588');
                this.builder.buildWall(new Coordinates(12, 1), 2, 11, '\u2588');
                this.builder.buildWall(new Coordinates(24, 11), 7, 1, '\u2588');
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
                ieBuilder.setSpritePath(null);
                InteractableElement chest0_1 = ieBuilder.getResult();
                ieBuilder.setID(5); ieBuilder.setCoordinates(new Coordinates(9, 11)); ieBuilder.setInteractions(List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))));
                ieBuilder.setSpritePath(null);
                InteractableElement chest1_1 = ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 2, new Coordinates(0, 4)); ieBuilder.setID(6);
                InteractableElement door_1 = ieBuilder.getResult();
                
                this.builder.setID(1);
                this.builder.setDimension(32, 32);
                this.builder.setSpawn(new Coordinates(28, 2), new Coordinates(28, 4));
                this.builder.setEnemies(listEn);
                this.builder.setInteractableElements(List.of(chest0_1, chest1_1, door_1));
                this.builder.buildBorder();
                this.builder.buildWall(new Coordinates(11, 1), 8, 25, '\u2588');
                this.builder.buildWall(new Coordinates(14, 28), 1, 3, '\u2588');
                this.builder.buildWall(new Coordinates(1, 14), 4, 1, '\u2588');
                this.builder.buildWall(new Coordinates(8, 8), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(11, 26), 1, 2, '\u2588');
                this.builder.buildWall(new Coordinates(18, 26), 1, 2, '\u2588');
                this.builder.buildWall(new Coordinates(23, 11), 8, 1, '\u2588');
                this.builder.buildWall(new Coordinates(19, 19), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(2, 19), 9, 1, '\u2588');
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
                ieBuilder.setSpritePath(null);
                InteractableElement chest2_1 = ieBuilder.getResult();
                ieDirector.createCheckPoint(this.ieBuilder, new Coordinates(9, 34)); ieBuilder.setID(8);
                ieBuilder.setInteractions(List.of(new CheckPoint(List.of("Hey there! It's me Aretha Franklin didn't you recognise me? Oh come on!", "You guys don't look so good... ",
                "I can help you guys! If something goes bad you'll get to me! Just talk to me whenever you see me around!", "Just don't get yourselves killed, I hate those things!"))));
                InteractableElement bossCheckPoint = ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 3, 5001, new Coordinates(16, 0)); ieBuilder.setID(9);
                InteractableElement endDoor = ieBuilder.getResult();
                
                List<Enemy> enemyBossRoom = new ArrayList<>(); enemyBossRoom.addAll(List.of(boss, minion1, minion2));
                

                this.builder.setID(2);
                this.builder.setDimension(32, 40);
                this.builder.setSpawn(new Coordinates(14, 37), new Coordinates(17, 37));
                this.builder.buildBorder();

                this.builder.setEnemies(enemyBossRoom);
                this.builder.setInteractableElements(List.of(chest2_1, bossCheckPoint, endDoor));
                
                // Top big blocks (left)
                this.builder.buildWall(new Coordinates(3, 4), 4, 4, '\u2588');
                
                // Top big blocks (right) 
                this.builder.buildWall(new Coordinates(25, 4), 4, 4, '\u2588');
                
                // Middle-upper small blocks (left)
                this.builder.buildWall(new Coordinates(7, 9), 2, 2, '\u2588');
                
                // Middle-upper small blocks (right)
                this.builder.buildWall(new Coordinates(23, 9), 2, 2, '\u2588');
                
                // Middle small blocks (left)
                this.builder.buildWall(new Coordinates(5, 14), 2, 2, '\u2588');
                
                // Middle small blocks (right)
                this.builder.buildWall(new Coordinates(25, 14), 2, 2, '\u2588');
                
                // Bottom staircase (left)
                this.builder.buildWall(new Coordinates(4, 20), 2, 1, '\u2588');
                this.builder.buildWall(new Coordinates(5, 21), 2, 1, '\u2588');
                this.builder.buildWall(new Coordinates(6, 22), 2, 1, '\u2588');
                
                // Bottom staircase (right)
                this.builder.buildWall(new Coordinates(26, 20), 2, 1, '\u2588');
                this.builder.buildWall(new Coordinates(25, 21), 2, 1, '\u2588');
                this.builder.buildWall(new Coordinates(24, 22), 2, 1, '\u2588');
                
                // Bottom horizontal wall - LEFT PART (connected to left border)
                this.builder.buildWall(new Coordinates(1, 30), 13, 4, '\u2588');
                
                // Bottom horizontal wall - RIGHT PART (connected to right border)
                this.builder.buildWall(new Coordinates(18, 30), 13, 4, '\u2588');
                
                break;
            case "Reception":
                
                this.builder.setID(3);
                this.builder.setDimension(40, 15);
                this.builder.setSpawn(new Coordinates(19, 1), new Coordinates(20, 1));
                this.builder.buildBorder();
                
                //right corner
                this.builder.buildWall(new Coordinates(1, 1), 4, 2, '\u2588');
                this.builder.buildWall(new Coordinates(1, 3), 2, 2, '\u2588');
                this.builder.buildWall(new Coordinates(1, 5), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(3, 3), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(5, 1), 1, 1, '\u2588');
                
                //left corner
                this.builder.buildWall(new Coordinates(35, 1), 4, 2, '\u2588');
                this.builder.buildWall(new Coordinates(37, 3), 2, 2, '\u2588');
                this.builder.buildWall(new Coordinates(38, 5), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(36, 3), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(34, 1), 1, 1, '\u2588');

                //counter
                this.builder.buildWall(new Coordinates(17, 11), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(17, 12), 1, 2, '\u2588');
                this.builder.buildWall(new Coordinates(23, 12), 1, 2, '\u2588');

                //decoration
                this.builder.buildWall(new Coordinates(2, 12), 1, 1, '\u2318');
                this.builder.buildWall(new Coordinates(37, 12), 1, 1, '\u2318');

                //doors
                ieDirector.createDoor(ieBuilder, 4, new Coordinates(39, 10)); ieBuilder.setID(10);
                InteractableElement arenaDoor_r = ieBuilder.getResult();
                ieDirector.createDoor(ieBuilder, 5, 5002, new Coordinates(0, 10)); ieBuilder.setID(11);
                InteractableElement preBossDoor_r = ieBuilder.getResult();
                
                //chests
                ieBuilder.setID(12); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(4, 12)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))));
                ieBuilder.setSpritePath(null);
                InteractableElement chest_r0 = ieBuilder.getResult();
                ieBuilder.setID(13); ieBuilder.setMapSymbol('$'); ieBuilder.setCoordinates(new Coordinates(35, 12)); ieBuilder.setName("Chest");
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))));
                ieBuilder.setSpritePath(null);
                InteractableElement chest_r1 = ieBuilder.getResult();

                //checkpoints
                ieDirector.createCheckPoint(ieBuilder, new Coordinates(16, 7));
                InteractableElement checkpoint = ieBuilder.getResult();

                //receptionist
                ieBuilder.setID(14); ieBuilder.setMapSymbol('\u22B7'); ieBuilder.setCoordinates(new Coordinates(20, 11)); ieBuilder.setName("R.E.N.E. 3000"); ieBuilder.setSpritePath("/images/bobinatro-fotor-2026021023422.jpg");
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
                     "The keys to the office area should be there!", "The key guy is a bit attached to his keys but I'm 100% sure he has the key that you are searching for!", "Thank you!", "Good luck with the meeting then!")), new Talk(List.of("Good luck with the meeting then!"))
                ));
                InteractableElement receptionist = ieBuilder.getResult();

                this.builder.setInteractableElements(List.of(preBossDoor_r, arenaDoor_r, receptionist, chest_r0, chest_r1, checkpoint));
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
                this.builder.buildWall(new Coordinates(13, 1), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(12, 2), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(11, 3), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(10, 4), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(11, 5), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(12, 6), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(13, 7), 1, 1, '\u2588');

                //left wall
                this.builder.buildWall(new Coordinates(1, 19), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 18), 2, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 17), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 16), 4, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 15), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 14), 6, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 13), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(2, 12), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(3, 11), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(4, 10), 7, 1, '\u2588');
                this.builder.buildWall(new Coordinates(5, 9), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(6, 8), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(7, 7), 1, 1, '\u2588');

                //barriers
                this.builder.buildWall(new Coordinates(14, 12), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(13, 13), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(12, 14), 1, 1, '\u2588');
                
                this.builder.buildWall(new Coordinates(18, 8), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(17, 9), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(16, 10), 1, 1, '\u2588');

                //benches
                this.builder.buildWall(new Coordinates(5, 23), 4, 1, '\u2583');
                this.builder.buildWall(new Coordinates(13, 23), 4, 1, '\u2583');

                this.builder.setEnemies(new ArrayList<>(List.of(mosquito, roboguard_0, roboguard_1)));

                //interactable elements
                ieDirector.createDoor(this.ieBuilder, 3, new Coordinates(0, 2), new Coordinates(38, 8), new Coordinates(38, 10)); this.ieBuilder.setID(15);
                InteractableElement receptionDoor_B = ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 7, new Coordinates(24, 3)); this.ieBuilder.setID(16);
                InteractableElement keyRoomDoor_B = ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 6, new Coordinates(10, 24)); this.ieBuilder.setID(17);
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
                this.builder.setEnemies(new ArrayList<>(List.of(guard1, guard2, roboguard1, roboguard2)));

                ieDirector.createDoor(this.ieBuilder, 3, new Coordinates(31, 4), new Coordinates(1, 8), new Coordinates(1, 10)); ieBuilder.setID(18);
                InteractableElement receptionDoor_p = this.ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 8, new Coordinates(0, 4)); ieBuilder.setID(19);
                InteractableElement bossDoor_p = this.ieBuilder.getResult();

                this.builder.setInteractableElements(List.of(receptionDoor_p, bossDoor_p));
                
                this.builder.setID(5);
                this.builder.setDimension(32, 10);
                this.builder.buildBorder();
                this.builder.setSpawn(new Coordinates(30, 3), new Coordinates(30, 5));
                
                //top wall
                this.builder.buildWall(new Coordinates(1, 1), 17, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 2), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 3), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(13, 2), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(15, 3), 3, 1, '\u2588');
                
                //bottom wall
                this.builder.buildWall(new Coordinates(1, 6), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 7), 5, 1, '\u2588');
                this.builder.buildWall(new Coordinates(1, 8), 17, 1, '\u2588');
                this.builder.buildWall(new Coordinates(15, 6), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(13, 7), 5, 1, '\u2588');
                
                //barriers
                this.builder.buildWall(new Coordinates(22, 6), 1, 2, '\u2588');
                this.builder.buildWall(new Coordinates(22, 2), 1, 2, '\u2588');
                break;
            case "ChestRoom":
                this.builder.setID(6);
                this.builder.setDimension(10, 10);
                this.builder.setSpawn(new Coordinates(3, 1), new Coordinates(5, 1));
                this.builder.buildBorder();
                
                //bottom wall
                this.builder.buildWall(new Coordinates(1, 8), 8, 1, '\u2588');
                this.builder.buildWall(new Coordinates(2, 7), 6, 1, '\u2588');
                this.builder.buildWall(new Coordinates(3, 6), 4, 1, '\u2588');

                ab.addShape("base");
                mb1.setName("Fragile Swing"); mb1.setPower(16); mb1.setActionPoints(1); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move fragileSwing = mb1.getResult();
                Weapon glassPanel = new Weapon("Glass Panel", " ", 1, List.of(fragileSwing), PlayerType.MUSICIAN);
                ieBuilder.setID(20); ieBuilder.setMapSymbol('$'); ieBuilder.setName("Chest"); ieBuilder.setCoordinates(new Coordinates(4, 6));
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a glass panel!"), glassPanel), new Talk(List.of("This chest is empty."))));
                ieBuilder.setSpritePath(null);
                InteractableElement chest0_c = ieBuilder.getResult();
                ab.addShape("circle",5,true);
                mb1.setName("Button Press"); mb1.setPower(0); mb1.setActionPoints(1); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move buttonPress = mb1.getResult();
                Weapon strangeRemote = new Weapon("Strange Remote", " ", 1, List.of(buttonPress), PlayerType.MOVIE_DIRECTOR);
                ieBuilder.setID(21); ieBuilder.setMapSymbol('$'); ieBuilder.setName("Chest"); ieBuilder.setCoordinates(new Coordinates(5, 6));
                ieBuilder.setInteractions(List.of(
                    new Give(List.of("You found a strange remote!"), strangeRemote)));
                ieBuilder.setSpritePath(null);
                InteractableElement chest1_c = ieBuilder.getResult();
                ieDirector.createDoor(this.ieBuilder, 4, new Coordinates(7, 0), new Coordinates(10, 23), new Coordinates(11, 23));
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
                this.builder.buildWall(new Coordinates(1, 9), 1, 1, '\u2588');

                //obstacles
                this.builder.buildWall(new Coordinates(4, 9), 3, 1, '\u2588');
                this.builder.buildWall(new Coordinates(5, 2), 1, 5, '\u2588');
                this.builder.buildWall(new Coordinates(4, 3), 1, 1, '\u2588');
                this.builder.buildWall(new Coordinates(4, 5), 1, 1, '\u2588');
                
                //interactable element
                ieDirector.createDoor(ieBuilder, 4, new Coordinates(0, 2), new Coordinates(23, 2), new Coordinates(23, 4));
                this.builder.setInteractableElements(List.of(ieBuilder.getResult()));

                //enemy
                eDirector.create(eBuilder, EnemyType.ATTENDANT, new Coordinates(7, 5));
                this.builder.setEnemies(new ArrayList<>(List.of(eBuilder.getResult())));
                break;
            case "BossArena":
                this.builder.setID(8);
                this.builder.setDimension(18, 12);
                this.builder.setSpawn(new Coordinates(15, 4), new Coordinates(15, 6));
                
                //triggers
                TriggerGroup evilDialogue = new TriggerGroup(new Talk(List.of(
                    "...Computer, create a cool villain monologue, and say it out loud...",
                    "...PROCESSING PROMPT...", "...PROCESSING COMPLETE.",
                    "Welcome to my office. I have been waiting for you. Take a seat.",
                    "So, you've come to take your revenge, eh? How naive.",
                    "Don't get your hopes up just because you were able to get through our security system. My latest AI model has reached a state of absolute perfection. You have no chance of defeating me.",
                    "I worked hard to get where I am. I invested my money wisely. I walked all over my competition. I AM A SELF MADE MAN!",
                    "And you? With your silly little art projects? Playing your kiddie instruments and shooting your silly movies? NONSENSE!",
                    "You and your fans are nothing but poor, poor communists. AI could make any song, any movie, any painting, ANYTHING, BETTER THAN ANY OF YOU \"ARTISTS\" COULD!",
                    "You're nothing but FRAUDS! BILLIONAIRES ARE THE TRUE HEIRS OF THE WORLD!",
                    "NOW DIE!"
                )));

                this.builder.addTriggerGroup(evilDialogue, new Coordinates(5, 3), 11, 6, "/images/saminator-pixel.jpg");

                this.builder.buildBorder();

                //decorations
                this.builder.buildWall(new Coordinates(16, 1), 1, 1, '\u2380');
                this.builder.buildWall(new Coordinates(16, 10), 1, 1, '\u2380');
                
                //walls
                this.builder.buildWall(new Coordinates(7, 10), 3, 1, '$');
                this.builder.buildWall(new Coordinates(7, 1), 3, 1, '$');

                this.builder.buildWall(new Coordinates(8, 9), 1, 1, '$');
                this.builder.buildWall(new Coordinates(8, 2), 1, 1, '$');

                //desk
                this.builder.buildWall(new Coordinates(4, 2), 1, 8, '\u20AC');
                this.builder.buildWall(new Coordinates(1, 2), 3, 1, '\u20AC');
                this.builder.buildWall(new Coordinates(1, 9), 3, 1, '\u20AC');


                //enemy
                eDirector.create(eBuilder, EnemyType.SAM, new Coordinates(2, 5));
                this.builder.setEnemies(new ArrayList<>(List.of(eBuilder.getResult())));
                break;
        }
    }
}
