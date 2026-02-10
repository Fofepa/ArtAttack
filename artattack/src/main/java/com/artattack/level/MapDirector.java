package com.artattack.level;

import java.util.ArrayList;
import java.util.List;

import com.artattack.interactions.CheckPoint;
import com.artattack.interactions.Give;
import com.artattack.interactions.SwitchMap;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.items.Key;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;
import com.artattack.moves.Weapon;

public class MapDirector {
    private MapBuilder builder;
    private MoveBuilder1 mb1;
    private AreaBuilder ab;

    public MapDirector(MapBuilder builder) {
        this.builder = builder;
        this.mb1 = new MoveBuilder1();
        this.ab = new AreaBuilder();
    }

    public void changeBuilder(MapBuilder builder) {
        this.builder = builder;
    }

    public void make(String type) {
        switch (type) {
            case "Tutorial":
                //Creating Areas
                ab.addShape("base");
                List<Coordinates> moveArea_t = ab.getResult();
                ab.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_t = ab.getResult();

                //Creating Enemies
                ab.addShape("8");
                List<Coordinates> moveArea = ab.getResult();
                ab.addShape("4");
                List<Coordinates> area4 = ab.getResult();
                ab.addShape("square", 1, true);
                mb1.setName("Scared Punch"); mb1.setPower(1); mb1.setActionPoints(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move m1 = mb1.getResult();
                Weapon enemyWeapon = new Weapon(" ", " ", 5, List.of(m1), null);
                Enemy e = new Enemy(0, 'E', "Employee", new Coordinates(6, 3), EnemyType.EMPLOYEE, 20, 20, 2, List.of(enemyWeapon), 10, 10, moveArea_t, enemyVisionArea_t, null, null, 20);
                List<Enemy> listE = new ArrayList<>();
                listE.add(e);

                //Creating InteractableElements
                InteractableElement chest_t = new InteractableElement(0, 'O', "Chest", new Coordinates(1, 23), List.of(
                    new Give(List.of("You found a Cure!", "Wow! This'll come in handy! You can press I to open your INVENTORY and browse your ITEMS. If you want to use one, press Enter."), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))),"");
                InteractableElement npc_t = new InteractableElement(0, 'M', "George Melies", new Coordinates(29, 22), List.of(new Talk(
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
                ab.addShape("base");
                List<Coordinates> moveArea_1 = ab.getResult();
                ab.addShape("square", 5, true);
                List<Coordinates> enemyVisionArea_1 = ab.getResult();

                ab.addShape("8");
                List<Coordinates> mArea = ab.getResult();
                ab.addShape("4");
                List<Coordinates> a4 = ab.getResult();

                // creation of the enemies
                // move creation for employee
                ab.addShape("square", 1, true);
                mb1.setName("Scared Punch"); mb1.setActionPoints(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult()); mb1.setPower(1);
                Move employeeMove = mb1.getResult();
                // move creation for guard
                ab.addShape("square", 1, true);
                mb1.setName("Legal Bat"); mb1.setActionPoints(2); mb1.setPower(4); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move guardMove1 = mb1.getResult();
                
                Weapon eWeapon = new Weapon(" ", " ", 5, List.of(employeeMove), null);
                Enemy e0_1 = new Enemy(1, 'E', "Employee", new Coordinates(28, 13), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e1_1 = new Enemy(2, 'G', "Guard", new Coordinates(20, 21), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e2_1 = new Enemy(3, 'E', "Employee", new Coordinates(9, 21), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 30);
                Enemy e3_1 = new Enemy(4, 'G', "Guard", new Coordinates(2, 11), EnemyType.EMPLOYEE, 10, 10, 2, List.of(eWeapon), 5, 5, moveArea_1, enemyVisionArea_1, null, null, 40);
                List<Enemy> listEn = new ArrayList<>();
                listEn.addAll(List.of(e0_1,e1_1, e2_1, e3_1));

                InteractableElement chest0_1 = new InteractableElement(0, '$', "Chest", new Coordinates(29, 29), List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))), null);
                InteractableElement chest1_1 = new InteractableElement(0, '$', "Chest", new Coordinates(9, 11), List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))), null);
                InteractableElement checkpoint_1 = new InteractableElement(0, 'C', "checkpoint", new Coordinates(30, 30), List.of(new CheckPoint(List.of("OK"))), "");
                InteractableElement door_1 = new InteractableElement(0, '\u2339', "Door", new Coordinates(0, 4), 
                List.of(new SwitchMap(2, List.of(new Coordinates(30, 19), new Coordinates(30, 21)))),"");
                
                this.builder.setID(1);
                this.builder.setDimension(32, 32);
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
                // move1 creation
                ab.addShape("square", 1, true);
                mb1.setName("Screw Thrust"); mb1.setActionPoints(5); mb1.setPower(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move bossm1 = mb1.getResult();
                // move2 creation
                ab.addShape("square", 1, true);
                mb1.setName("Hammer Swing"); mb1.setActionPoints(6); mb1.setPower(3); mb1.setAreaAttack(true); mb1.setAttackArea(ab.getResult());
                Move bossm2 = mb1.getResult();
                // move3 creation
                ab.addShape("square", 1, true);
                mb1.setName("Wrench Repair"); mb1.setActionPoints(7); mb1.setHealAmount(10); mb1.setHealArea(ab.getResult()); mb1.setAreaHeal(false);
                Move bossm3 = mb1.getResult();
                //boss weapon creation
                Weapon bossWeapon = new Weapon("BossWeapon", " ", 4, List.of(bossm1,bossm2,bossm3), null);
                // Boss initialization
                ab.addShape("circle", 8, true);
                List<Coordinates> bossMA = ab.getResult();
                ab.addShape("square", 25, true);
                List<Coordinates> bossVA = ab.getResult();
                Key key = new Key("1st floor key", "Let's you and your party go upstairs!", 5001);
                Enemy boss = new Enemy(4, 'B', "B.O.B.", new Coordinates(15, 4), EnemyType.BOB, 35, 35, 5, List.of(bossWeapon), 18, 18, bossMA, bossVA, null, List.of(key), 50);

                // minions creation
                // move1 creation
                ab.addShape("square", 1, true);
                mb1.setName("Groom Wank"); mb1.setActionPoints(3); mb1.setPower(2); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move minionM1 = mb1.getResult();
                // move2 creation
                ab.addShape("square", 1, true);
                mb1.setName("Mop Swing"); mb1.setActionPoints(2); mb1.setPower(3); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(true);
                Move minionM2 = mb1.getResult();
                // move3 creation
                ab.addShape("circle", 4, true);
                mb1.setName("Bleach Throw"); mb1.setActionPoints(5); mb1.setPower(2); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move minionM3 = mb1.getResult();
                // minionWeapon creation
                Weapon minionWeapon = new Weapon(" ", " ", 3, List.of(minionM1, minionM2, minionM3), null);
                // minions creation
                ab.addShape("square", 3, true);
                List<Coordinates> minionMA = ab.getResult();
                ab.addShape("square", 23, true);
                List<Coordinates> minionVA = ab.getResult();
                Enemy minion1 = new Enemy(5, 'E', "T.O.O.L BOT", new Coordinates(12, 6), EnemyType.TOOLBOT, 15, 15, 4, List.of(minionWeapon), 8, 8, minionMA, minionVA, null, null, 10);
                Enemy minion2 = new Enemy(6, 'E', "T.O.O.L BOT", new Coordinates(18, 6), EnemyType.TOOLBOT, 15, 15, 4, List.of(minionWeapon), 8, 8, minionMA, minionVA, null, null, 10);
                // creation of the interactable elements
                InteractableElement chest2_1 = new InteractableElement(0, '$', "Chest", new Coordinates(30, 36), List.of(
                new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5))), new Talk(List.of("This chest is empty."))), null);
                InteractableElement bossCheckPoint = new InteractableElement(1, 'C', "checkpoint", new Coordinates(9, 34), List.of(new CheckPoint(List.of("OK"))), "");
                InteractableElement endDoor = new InteractableElement(0, '\u2339', "Door", new Coordinates(16, 0), 
                List.of(new SwitchMap(5001, 3, List.of(new Coordinates(19, 1), new Coordinates(20, 1)))),"");
                
                List<Enemy> enemyBossRoom = new ArrayList<>(); enemyBossRoom.addAll(List.of(boss, minion1, minion2));
                

                this.builder.setID(2);
                this.builder.setDimension(32, 40);
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
                this.builder.setDimension(40, 15);
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
                InteractableElement bossDoor_r = new InteractableElement(0, 'H', "Door", new Coordinates(0, 10), new ArrayList<>(), null);
                InteractableElement arenaDoor_r = new InteractableElement(0, 'H', "Door", new Coordinates(39, 10), new ArrayList<>(), null);
                InteractableElement floor0door_r0 = new InteractableElement(0, 'H', "Door", new Coordinates(19, 0), new ArrayList<>(), null);
                InteractableElement floor0door_r1 = new InteractableElement(0, 'H', "Door", new Coordinates(20, 0), new ArrayList<>(), null);
                
                //chests
                InteractableElement chest_r0 = new InteractableElement(0, '$', "Chest", new Coordinates(4, 12), List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))), 
                    "");
                InteractableElement chest_r1 = new InteractableElement(0, '$', "Chest", new Coordinates(35, 12), List.of(
                    new Give(List.of("You found a Cure!"), List.of(new Item(ItemType.CURE, "Cure", "Heals 5 HP.", 5)))), 
                    "");

                //receptionist
                InteractableElement receptionist = new InteractableElement(0, 'R', "R.E.N.E. 3000", new Coordinates(20, 11), List.of(
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
                ), null);

                this.builder.setInteractableElements(List.of(bossDoor_r, arenaDoor_r, floor0door_r0, floor0door_r1, receptionist, chest_r0, chest_r1));
                this.builder.setEnemies(new ArrayList<>());
                break;
            case "BigEnemyArea":
                AreaBuilder ab_B = new AreaBuilder();
                ab_B.addShape("square", 3, false);
                List<Coordinates> mosquitoMA = ab_B.getResult();
                ab_B.addShape("diamond", 10, true);
                List<Coordinates> mosquitoVA = ab_B.getResult();
                ab_B.addShape("square", 2, true);
                List<Coordinates> roboguardMA = ab_B.getResult();
                ab_B.addShape("diamond", 6, true);
                List<Coordinates> roboguardVA = ab_B.getResult();

                this.builder.setDimension(25, 25);
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

                //enemies
                Enemy mosquito = new Enemy(0, 'M', "Mosquito", new Coordinates(9, 6), EnemyType.MOSQUITO, 
                10, 10, 10, new ArrayList<>(), 8, 8, mosquitoMA, mosquitoVA, new ArrayList<>(), new ArrayList<>(), 1);
                Enemy roboguard_0 = new Enemy(0, 'G', "Roboguard", new Coordinates(18, 10), EnemyType.GUARD,
                18, 18, 5, new ArrayList<>(), 20, 20, roboguardMA, roboguardVA, new ArrayList<>(), new ArrayList<>(), 8);
                Enemy roboguard_1 = new Enemy(0, 'G', "Roboguard", new Coordinates(14, 14), EnemyType.GUARD,
                18, 18, 5, new ArrayList<>(), 20, 20, roboguardMA, roboguardVA, new ArrayList<>(), new ArrayList<>(), 8);

                this.builder.setEnemies(new ArrayList<>(List.of(mosquito, roboguard_0, roboguard_1)));

                //interactable elements
                InteractableElement receptionDoor_B = new InteractableElement(0, 'H', "Door", new Coordinates(0, 2), List.of(
                new SwitchMap(3, List.of(new Coordinates(19, 1), new Coordinates(20, 1)))), 
                "");
                InteractableElement keyRoomDoor_B = new InteractableElement(0, 'H', "Door", new Coordinates(24, 3), List.of(
                new SwitchMap(10, List.of(new Coordinates(1, 1), new Coordinates(2, 1)))), 
                "");
                InteractableElement chestRoomDoor_B = new InteractableElement(0, 'H', "Door", new Coordinates(10, 24), List.of(
                new SwitchMap(10, List.of(new Coordinates(1, 1), new Coordinates(2, 1)))),
                "");

                this.builder.setInteractableElements(List.of(receptionDoor_B, keyRoomDoor_B, chestRoomDoor_B));
                break;
            case "PreBoss":
                this.builder.setDimension(32, 10);
                this.builder.buildBorder();

                this.builder.buildWall(new Coordinates(1, 1), 17, 1, '#');
                this.builder.buildWall(new Coordinates(1, 2), 5, 1, '#');
                this.builder.buildWall(new Coordinates(1, 3), 3, 1, '#');
                this.builder.buildWall(new Coordinates(13, 2), 5, 1, '#');
                this.builder.buildWall(new Coordinates(15, 3), 3, 1, '#');
                
                this.builder.buildWall(new Coordinates(1, 6), 3, 1, '#');
                this.builder.buildWall(new Coordinates(1, 7), 5, 1, '#');
                this.builder.buildWall(new Coordinates(1, 8), 17, 1, '#');
                this.builder.buildWall(new Coordinates(15, 6), 3, 1, '#');
                this.builder.buildWall(new Coordinates(13, 7), 5, 1, '#');

                this.builder.buildWall(new Coordinates(22, 6), 1, 2, '#');
                this.builder.buildWall(new Coordinates(22, 2), 1, 2, '#');
                break;
        }
    }
}
