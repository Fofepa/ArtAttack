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

public class enemyTest {
    private Enemy enemy;
    private Player player1;
    private Player player2;
    private TestMapBuilder tmb;
    private Maps map;

    @Before
    public void setUp() throws Exception{
        //Creating enemy
        this.enemy = new Enemy(0,'+'," ", new Coordinates(0,0),EnemyType.GUARD,0,35,0,null,0,null,null,null,null,10);
        //Creating player1
        this.player1 = new MovieDirector(0, 'M', "David Lynch", new Coordinates(0,0), null, 5, null, 20, 20, 0, 20, 5, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
        //Creating player2
        this.player2 = new Musician(1, 'F', "Frank Zappa", new Coordinates(1,0), null, 5, null, 20, 20, 0, 20, 5, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
        //Initializing tmb
        this.tmb = new TestMapBuilder();
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
        enemy.updatePlayerOneDemage(25);
        enemy.updatePlayerTwoDemage(10);
        enemy.dropXP(player1, player2);
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 7, player1.getCurrXP());
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 3, player2.getCurrXP());
    
    
    }

    @Test
    public void removeTest(){
        enemy.remove(map);
        assertFalse("removeTest faild. Enemy still in the dict.", map.getDict().containsValue(enemy));
        assertTrue("removeTest faild. mapMatrix not updated.", map.getMapMatrix()[enemy.getCoordinates().getX()][enemy.getCoordinates().getY()]=='.');
    }
}
