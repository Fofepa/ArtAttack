package com.artattack.mapelements;

import java.util.ArrayList;
import java.util.List;

import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.skilltree.SkillTreeFactory;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;
import com.artattack.moves.Weapon;
import com.artattack.view.CharacterType;

public class PlayerDirector {
    private AreaBuilder ab = new AreaBuilder();
    private MoveBuilder1 mb1 = new MoveBuilder1();
    public void create(PlayerBuilder builder, CharacterType charachterType, int ID){
        PlayerType playerType = charachterType.getPlayerType();
        switch(playerType){
            case MUSICIAN ->{
                /*ab.addShape("base");
                ab.addShape("x", 2);*/
                ab.addShape("square",30, true); //for testing
                List<Coordinates> zappaMA = ab.getResult();
                ab.addShape("circle", 3, true);
                mb1.setName("Riff"); mb1.setActionPoints(2); mb1.setPower(20); mb1.setAreaAttack(true); mb1.setAttackArea(ab.getResult());  //TODO: power to 4 to reset
                Move zappaMove1 = mb1.getResult();
                ab.addShape("base");
                mb1.setName("Guitar Strike"); mb1.setActionPoints(3); mb1.setPower(4); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move zappaMove2 = mb1.getResult();
                List<Coordinates> zappaMove3Area = new ArrayList<Coordinates>();
                ab.addShape("circle", 1,false); 
                zappaMove3Area.addAll(ab.getResult());
                ab.addShape("circle", 3, false); 
                zappaMove3Area.addAll(ab.getResult());
                ab.addShape("circle", 5, false); 
                zappaMove3Area.addAll(ab.getResult());
                mb1.setName("Melody"); mb1.setActionPoints(4); mb1.setHealAmount(8); mb1.setAreaHeal(true); mb1.setHealArea(zappaMove3Area);
                Move zappaMove3 = mb1.getResult();
                ab.addShape("circle", 5, true);
                mb1.setName("Stink Foot"); mb1.setActionPoints(8); mb1.setPower(10); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move zappaMove4 = mb1.getResult();
                //For testing
                Move zappaSpecial2 = new Move(); zappaSpecial2.setName("ST. Alfonzo's Pancake Breakfast"); 
                zappaSpecial2.setDescription("Music nowdays does miracles, look at this beautiful Pancake Zappa has made! It heals a lot and can be used on the others!!");
                zappaSpecial2.setActionPoints(1);
                zappaSpecial2.setHealAmount(25);
                AreaBuilder ab = new AreaBuilder();
                ab.addShape("circle", 8, true);
                zappaSpecial2.setHealArea(ab.getResult());
                //TODO: reset AP at 15
                Weapon oldGuitar = new Weapon("Old guitar", "The guitar that Zappa was buried with" ,2, new ArrayList<>(List.of(zappaMove1,/*zappaMove2,zappaMove3, zappaMove4*/zappaSpecial2)), playerType);
                builder.setID(ID); builder.setName("Frank Zappa"); builder.setMapSymbol('♫'); builder.setCurrHP(20); builder.setMaxHP(20); builder.setLevel(1); builder.setMoveArea(zappaMA); builder.setSpritePath("/images/frank-zappa-fotor-20260206135640.jpg");
                builder.setCurrXP(0); builder.setCurrXP(20); builder.setActionPoints(50); builder.setMaxActionPoints(50); builder.setType(playerType); builder.setSkillTree(SkillTreeFactory.createSkillTree(playerType));
                builder.setSpeed(5); builder.setInventory(new ArrayList<>()); builder.setKeys(new ArrayList<>()); builder.setMaxWeapons(1); builder.setWeapons(new ArrayList<>(List.of(oldGuitar))); builder.setCoordinates(new Coordinates(0, 0));

            }

            case MOVIE_DIRECTOR ->{
                //ab.addShape("square",1,true);
                ab.addShape("square",30, true); //for testing
                List<Coordinates> lynchMA = ab.getResult();
                ab.addShape("circle", 2, true);
                mb1.setName("Action!"); mb1.setActionPoints(5); mb1.setPower(6); mb1.setAreaAttack(true); mb1.setAttackArea(ab.getResult());
                Move lynchMove1 = mb1.getResult();
                ab.addShape("base");
                mb1.setName("CiaK"); mb1.setActionPoints(3); mb1.setPower(5); mb1.setAttackArea(ab.getResult()); mb1.setAreaAttack(false); 
                Move lynchMove2 = mb1.getResult();
                List<Coordinates> lynchMove3Area = new ArrayList<>();
                ab.addShape("4", 3, true);
                lynchMove3Area.addAll(ab.getResult());
                ab.addShape("x", 3, true);
                lynchMove3Area.addAll(ab.getResult());
                ab.addShape("base");
                lynchMove3Area.addAll(ab.getResult());
                mb1.setName("Flash"); mb1.setActionPoints(10); mb1.setPower(0); mb1.setAreaAttack(true); mb1.setAttackArea(lynchMove3Area);
                Move lynchMove3 = mb1.getResult();
                ab.addShape("square", 4, true);
                mb1.setName("Do it for the camera"); mb1.setActionPoints(12); mb1.setPower(13); mb1.setAreaAttack(false); mb1.setAttackArea(ab.getResult());
                Move lynchMove4 = mb1.getResult();

                //TODO: reset the AP to 12
                Weapon actionCam = new Weapon("ActionCam", "" ,2, new ArrayList<>(List.of(lynchMove1,lynchMove2,lynchMove3, lynchMove4)), playerType);
                builder.setID(ID); builder.setName("David Lynch"); builder.setMapSymbol('◉'); builder.setCurrHP(25); builder.setMaxHP(25); builder.setLevel(1); builder.setMoveArea(lynchMA); builder.setSpritePath("/images/ozxg45isal6ve56l7tl6-fotor-20260206135846.jpg");
                builder.setCurrXP(0); builder.setCurrXP(20); builder.setActionPoints(52); builder.setMaxActionPoints(52); builder.setType(playerType); builder.setSkillTree(SkillTreeFactory.createSkillTree(playerType));
                builder.setSpeed(3); builder.setInventory(new ArrayList<>()); builder.setKeys(new ArrayList<>()); builder.setMaxWeapons(1); builder.setWeapons(new ArrayList<>(List.of(actionCam))); builder.setCoordinates(new Coordinates(0,0));
            }
        }

    }
}
