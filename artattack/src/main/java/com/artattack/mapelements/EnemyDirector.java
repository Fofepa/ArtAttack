package com.artattack.mapelements;

import java.util.ArrayList;
import java.util.List;

import com.artattack.items.Key;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;
import com.artattack.moves.Weapon;

public class EnemyDirector {
    private static int ID = 0;
    private AreaBuilder ab = new AreaBuilder();
    private MoveBuilder1 mb1 = new MoveBuilder1();

    public void create(EnemyBuilder builder, EnemyType enemyType, Coordinates coordinates){
        builder.setID(ID);
        switch(enemyType){
            case EnemyType.EMPLOYEE ->{
                ab.addShape("base");
                List<Coordinates> employeeMA = ab.getResult();
                ab.addShape("square", 4, true);
                List<Coordinates> employeeVA = ab.getResult();
                ab.addShape("square", 1, true);
                mb1.setName("Scared Punch"); mb1.setPower(1); mb1.setActionPoints(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move employeeMove = mb1.getResult();
                Weapon employeeWeapon = new Weapon(" ", " ", 5, List.of(employeeMove), null);
                builder.setMapSymbol('E'); builder.setName("Employee"); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(8); builder.setMaxHP(8); builder.setDroppedXP(20);
                builder.setSpeed(2); builder.setWeapons(List.of(employeeWeapon)); builder.setActionPoints(5); builder.setMaxActionPoints(5); builder.setMoveArea(employeeMA); builder.setVisionArea(employeeVA);
                builder.setSpritePath("/images/Employee.jpg");
                if(ID == 0){
                    builder.setDroppedXP(40); 
                    ab.addShape("square", 5, true);
                    builder.setVisionArea(ab.getResult());
                } 
            }
            
            case EnemyType.GUARD ->{
                ab.addShape("square", 2, true);
                List<Coordinates> guardMA = ab.getResult();
                ab.addShape("square", 5, true);
                List<Coordinates> guardVA = ab.getResult();
                ab.addShape("square", 1, true);
                mb1.setName("Legal Bat"); mb1.setActionPoints(2); mb1.setPower(4); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move guardMove1 = mb1.getResult();
                ab.addShape("circle", 5, true);
                mb1.setName("gunshot"); mb1.setActionPoints(6); mb1.setPower(5); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(true);
                Move guardMove2 = mb1.getResult();
                Weapon guardWeapon = new Weapon(" ", " ", 5, List.of(guardMove1, guardMove2), null);
                builder.setMapSymbol('G');builder.setName("Guard"); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(14); builder.setMaxHP(14); builder.setDroppedXP(40);
                builder.setSpeed(4); builder.setWeapons(List.of(guardWeapon)); builder.setActionPoints(10); builder.setMaxActionPoints(10); builder.setMoveArea(guardMA); builder.setVisionArea(guardVA);
                builder.setSpritePath("/images/guard.jpg");
            }

            case EnemyType.BOB ->{
                // Boss creation
                // move1 creation
                ab.addShape("square", 1, true);
                mb1.setName("Screw Thrust"); mb1.setActionPoints(5); mb1.setPower(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move bobMove1 = mb1.getResult();
                // move2 creation
                ab.addShape("square", 1, true);
                mb1.setName("Hammer Swing"); mb1.setActionPoints(6); mb1.setPower(3); mb1.setAreaAttack(true); mb1.setAttackArea(ab.getResult());
                Move bobMove2 = mb1.getResult();
                // move3 creation
                ab.addShape("square", 1, true);
                mb1.setName("Wrench Repair"); mb1.setActionPoints(7); mb1.setHealAmount(10); mb1.setHealArea(ab.getResult()); mb1.setAreaHeal(false);
                Move bobMove3 = mb1.getResult();
                //boss weapon creation
                
                Weapon bobWeapon = new Weapon("BobWeapon", " ", 4, new ArrayList<Move>(List.of(bobMove1,bobMove2,bobMove3)), null);
                // Boss initialization
                ab.addShape("circle", 8, true);
                List<Coordinates> bobMA = ab.getResult();
                ab.addShape("square", 25, true);
                List<Coordinates> bobVA = ab.getResult();
                Key key = new Key("1st floor key", "Let's you and your party go upstairs!", 5001);
                builder.setMapSymbol('B'); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(35); builder.setMaxHP(35); builder.setDroppedXP(100); builder.setKeys(List.of(new Key("1st floor key", "Lets you go upstairs!", 5001)));
                builder.setSpeed(6);builder.setName("B.O.B"); builder.setWeapons(List.of(bobWeapon)); 
                builder.setActionPoints(12); builder.setMaxActionPoints(12); builder.setMoveArea(bobMA); builder.setVisionArea(bobVA); builder.setSpritePath("/images/boss_tosto.jpg");
            }
            
            case EnemyType.MOSQUITO ->{
                ab.addShape("square", 5, false);
                List<Coordinates> mosquitoMA = ab.getResult();
                ab.addShape("diamond", 10, true);
                List<Coordinates> mosquitoVA = ab.getResult();
                ab.addShape("square", 2, true);
                mb1.setName("Repair"); mb1.setActionPoints(7); mb1.setHealAmount(6); mb1.setHealArea(ab.getResult()); mb1.setAreaHeal(false);
                Move mosquitoMove = mb1.getResult();
                Weapon mosquitoWeapon = new Weapon("MosquitoWeapon", " ", 4, List.of(mosquitoMove), null);
                builder.setMapSymbol('M'); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(7); builder.setMaxHP(7); builder.setDroppedXP(40); 
                builder.setSpeed(12);builder.setName("MosquitoBot"); builder.setWeapons(List.of(mosquitoWeapon)); builder.setActionPoints(12); builder.setMaxActionPoints(12); builder.setMoveArea(mosquitoMA); builder.setVisionArea(mosquitoVA);
                builder.setSpritePath("/images/mosquito.jpg");
            }
            
            case EnemyType.ROBOT ->{
                ab.addShape("square", 3, true);
                List<Coordinates> roboguardMA = ab.getResult();
                ab.addShape("diamond", 6, true);
                List<Coordinates> roboguardVA = ab.getResult();
                ab.addShape("circle", 5, true);
                mb1.setName("Laser Ray"); mb1.setActionPoints(6); mb1.setPower(6); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move robotMove1 = mb1.getResult();
                ab.addShape("circle", 3, true); 
                mb1.setName("Laser wave"); mb1.setActionPoints(7); mb1.setPower(4); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(true);
                Move robotMove2 = mb1.getResult();
                ab.addShape("base");
                mb1.setName("Big Ol' Punch"); mb1.setActionPoints(5); mb1.setPower(5); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move robotMove3 = mb1.getResult();
                Weapon robotWeapon = new Weapon("RobotWeapon", " ", 4, List.of(robotMove1, robotMove2, robotMove3), null);
                builder.setMapSymbol('R'); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(18); builder.setMaxHP(18); builder.setDroppedXP(70);
                builder.setSpeed(8);builder.setName("RoboGuard"); builder.setWeapons(List.of(robotWeapon)); builder.setActionPoints(16); builder.setMaxActionPoints(16); builder.setMoveArea(roboguardMA); builder.setVisionArea(roboguardVA);
                builder.setSpritePath("/images/roboGuard.jpg");
            }
            
            case EnemyType.TOOLBOT ->{
                 // minions creation
                // move1 creation
                ab.addShape("square", 1, true);
                mb1.setName("Groom Wank"); mb1.setActionPoints(3); mb1.setPower(2); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move minionMove1 = mb1.getResult();
                // move2 creation
                ab.addShape("square", 1, true);
                mb1.setName("Mop Swing"); mb1.setActionPoints(2); mb1.setPower(3); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(true);
                Move minionMove2 = mb1.getResult();
                // move3 creation
                ab.addShape("circle", 4, true);
                mb1.setName("Bleach Throw"); mb1.setActionPoints(5); mb1.setPower(2); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move minionMove3 = mb1.getResult();
                // minionWeapon creation
                Weapon minionWeapon = new Weapon(" ", " ", 3, List.of(minionMove1, minionMove2, minionMove3), null);
                // minions creation
                ab.addShape("square", 3, true);
                List<Coordinates> minionMA = ab.getResult();
                ab.addShape("square", 23, true);
                List<Coordinates> minionVA = ab.getResult();
                builder.setMapSymbol('T'); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(15); builder.setMaxHP(15); builder.setDroppedXP(45);
                builder.setSpeed(2);builder.setName("T.O.O.L.Bot"); builder.setWeapons(List.of(minionWeapon)); builder.setActionPoints(8); 
                builder.setMaxActionPoints(8); builder.setMoveArea(minionMA); builder.setVisionArea(minionVA); builder.setSpritePath("/images/toolbot_tosto.jpg");
            }

            case EnemyType.SAM ->{
                ab.addShape("circle", 4, true);
                mb1.setName("Polluted Waters"); mb1.setActionPoints(4); mb1.setPower(8); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(true);
                Move samMove1 = mb1.getResult();
                ab.addShape("diamond", 2, true);
                mb1.setName("Stock Manipulation"); mb1.setActionPoints(7); mb1.setPower(12); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false);
                Move samMove2 = mb1.getResult();
                Weapon samWeapon = new Weapon(" ", " ", 3, List.of(samMove1, samMove2), null);
                ab.addShape("square", 5, true);
                List<Coordinates> samMA = ab.getResult();
                ab.addShape("square", 7, true);
                List<Coordinates> samVA = Coordinates.sum(ab.getResult(), new Coordinates(6, 0));
                samVA.add(new Coordinates(6, 0));

                builder.setMapSymbol('S'); builder.setCoordinates(coordinates); builder.setEnemyType(enemyType); builder.setCurrHP(80); builder.setMaxHP(80); builder.setDroppedXP(0); builder.setSpritePath("/images/saminator-pixel.jpg");
                builder.setSpeed(50); builder.setName("Sam Altman"); builder.setWeapons(List.of(samWeapon)); builder.setActionPoints(15); builder.setMaxActionPoints(15); builder.setMoveArea(samMA); builder.setVisionArea(samVA);
            }
            case EnemyType.ATTENDANT -> {
                builder.setName("Attendant");
                builder.setID(ID);
                builder.setEnemyType(enemyType);
                builder.setMapSymbol('A');
                builder.setMaxActionPoints(4);
                builder.setActionPoints(4);
                ab.addShape("square", 1, true);
                builder.setMoveArea(ab.getResult());
                ab.addShape("square", 10, true);
                builder.setVisionArea(ab.getResult());
                builder.setWeapons(new ArrayList<>());
                builder.setMaxHP(10);
                builder.setCurrHP(10);
                builder.setCoordinates(coordinates);
                builder.setKeys(List.of(new Key("Key", "Key", 5002)));
                builder.setDroppedXP(1);
                builder.setSpeed(3);
                ab.addShape("square", 1, true);
                mb1.setName("Scared Punch"); mb1.setPower(1); mb1.setActionPoints(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move attendantMove = mb1.getResult();
                Weapon attendantWeapon = new Weapon("", "", 2,List.of(attendantMove), null);
                builder.setWeapons(new ArrayList<>(List.of(attendantWeapon)));

            }
        }
    }
}
