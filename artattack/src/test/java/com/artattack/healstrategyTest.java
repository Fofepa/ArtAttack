package com.artattack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.enemystrategy.HealStrategy;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.view.MainFrame;


public class healstrategyTest {
    private Maps map;
    private Enemy enemy, injuredEnemy, badInjuredEnemy;
    private MainFrame mainFrame;
    private HealStrategy healStrategy;

    @Before
    public void setUp(){
        AreaBuilder ab = new AreaBuilder();
        ab.addShape("square", 4, true);
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(List.of(new Coordinates(-1, 0))); m1.setActionPoints(3); m1.setAreaAttack(false);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(List.of(new Coordinates(4, 4))); m2.setActionPoints(4);
        Move m3 = new Move(); m3.setName("Heal"); m3.setHealAmount(20); m3.setHealArea(ab.getResult()); m3.setActionPoints(2); m3.setAreaHeal(false);
        Weapon enemyWeapon = new Weapon(" ", " ", 4, List.of(m1,m2, m3), null);
        // the enemy must be BOB, because only BOB can heal! (for now at least)
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(1,1),EnemyType.BOB, 20, 20, 3,
                                 List.of(enemyWeapon),30,30,null,null,null,null,0);
        // Initialization of the injured enemy
        this.injuredEnemy = new Enemy(0, 'I', "Beaten", new Coordinates(3, 3), EnemyType.DUMMY, 10, 50, 3, null, 0, 10, null, null, null, null, 0);
        // initialization of the even worse injured enemy!
        this.badInjuredEnemy = new Enemy(1, 'B', "Almost dead", new Coordinates(3, 2), EnemyType.DUMMY, 1, 50, 3, null, 0, 10, null, null, null, null, 0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Player(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 1, PlayerType.MUSICIAN)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, PlayerType.MUSICIAN));
        mapBuilder.setPlayerTwo(new Player(0, '@', "Lynch", new Coordinates(5, 5),List.of(new Weapon("Hoe", "", 1, PlayerType.MOVIE_DIRECTOR)), 5, 5 , null, 20, 20, 0, 20, 1, 5, 2, null, null, PlayerType.MOVIE_DIRECTOR));
        mapBuilder.setEnemies(List.of(enemy, injuredEnemy, badInjuredEnemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        this.mainFrame = new MainFrame(map);
        assertNotNull(mainFrame);
        healStrategy = new HealStrategy(mainFrame);
        Map<Move,Integer> moves = new LinkedHashMap<>(); moves.put(m1,1);moves.put(m2,1); moves.put(m3,2);
        healStrategy.setHealMove(m3);
    }

    @Test
    public void executeTest(){
        healStrategy.execute(enemy, map);
        assertEquals(21, map.getEnemies().get(2).getCurrHP());
       /*  assertNotEquals(20,map.getPlayerOne().getCurrHP());
        assertNotEquals(5, enemy.getActionPoints()); */
    }
    

    @After
    public  void tearDown(){
        mainFrame = null;
        assertNull(mainFrame);
        map = null;
        assertNull(map);
        enemy = null;
        assertNull(enemy);
        injuredEnemy = null;
        assertNull(injuredEnemy);
        badInjuredEnemy = null;
        assertNull(badInjuredEnemy);
        healStrategy = null;
        assertNull(healStrategy);
    }
}
