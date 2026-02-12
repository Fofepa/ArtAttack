package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.Maps;
import com.artattack.mapelements.ConcreteEnemyBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyDirector;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;

public class enemyTest {
    private Enemy enemy;
    private Player player1;
    private Player player2;
    private MapBuilderTypeOne tmb;
    private Maps map;

    @Before
    public void setUp() throws Exception{
        //Creating enemy
        ConcreteEnemyBuilder eb = new ConcreteEnemyBuilder();
        EnemyDirector ed = new EnemyDirector();
        ed.create(eb, EnemyType.GUARD, new Coordinates(0,0)); eb.setCurrHP(35); eb.setMaxHP(35); eb.setDroppedXP(20);
        this.enemy = eb.getResult();
        //Creating player1
        this.player1 = new Player(0, 'M', "David Lynch", new Coordinates(0,0), null, 5,5, null, 20, 20, 0, 20, 1, 2, 1, new ArrayList<>(), null, PlayerType.MOVIE_DIRECTOR);
        //Creating player2
        this.player2 = new Player(1, 'F', "Frank Zappa", new Coordinates(1,0), null, 5,5, null, 20, 20, 0, 20, 1, 2, 1, new ArrayList<>(), null, PlayerType.MOVIE_DIRECTOR);
        //Initializing tmb
        this.tmb = new MapBuilderTypeOne();
        assertNotNull(this.tmb);
        //Creating map
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(this.player1);
        tmb.setPlayerTwo(this.player2);
        tmb.setEnemies(List.of(this.enemy));
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();

        assertNotNull(this.enemy);
        assertNotNull(this.player1);
        assertNotNull(this.player2);
        assertNotNull(this.map);

    }

    @After
    public void tearDown(){
        this.enemy = null;
        this.player1 = null;
        this.player2 = null;
        this.map = null;
        assertNull(enemy);
        assertNull(player1);
        assertNull(player2);
        assertNull(map);
    }


    @Test
    public void dropxpTest(){
        enemy.updatePlayerOneDamage(35);
        enemy.updatePlayerTwoDamage(0);
        enemy.dropXP(player1, player2);
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 0, player1.getCurrXP());
        assertEquals(2, player1.getLevel());
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 0, player2.getCurrXP());
    
    
    }

    @Test
    public void removeTest(){
        enemy.remove(map);
        assertFalse("removeTest faild. Enemy still in the dict.", map.getDict().containsValue(enemy));
        assertTrue("removeTest faild. mapMatrix not updated.", map.getMapMatrix()[enemy.getCoordinates().getX()][enemy.getCoordinates().getY()]=='.');
    }
}
