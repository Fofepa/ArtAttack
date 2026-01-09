package com.artattack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.view.MainFrame;

public class attackstrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private AttackStrategy attack;

    @Before
    public void setUp(){
        this.mainFrame = new MainFrame();
        assertNotNull(mainFrame);
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(List.of(new Coordinates(-1, 0))); m1.setActionPoints(3); m1.setAreaAttack(false);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(List.of(new Coordinates(1, 1))); m2.setActionPoints(4);
        Move m3 = new Move(); 
        m3.setName("Explode"); m3.setPower(3); m3.setAttackArea(List.of(new Coordinates(-1, 0), new Coordinates(4, 4))); m3.setAreaAttack(true); m3.setActionPoints(3);
        Weapon enemyWeapon = new Weapon(" ", " ", List.of(m1,m2), 0);
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(1,1),EnemyType.GUARD, 20, 20, 3,
                                 List.of(enemyWeapon),5,null,null,null,null,0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 0)), 5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        attack = new AttackStrategy(mainFrame);
        Map<Move,Integer> moves = new LinkedHashMap<>(); moves.put(m1,1);moves.put(m2,1);moves.put(m3,2);
        attack.setMoves(moves); 
    }

    @Test
    public void executeTest(){
        attack.execute(enemy, map);
        assertNotEquals(20,map.getPlayerOne().getCurrHP());
        assertNotEquals(5, enemy.getActionPoints());
    }
    

    @After
    public  void tearDown(){
        mainFrame = null;
        assertNull(mainFrame);
        map = null;
        assertNull(map);
        enemy = null;
        assertNull(enemy);
        attack = null;
        assertNull(attack);
    }
}