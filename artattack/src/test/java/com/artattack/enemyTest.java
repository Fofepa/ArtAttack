package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class enemyTest {
    private Enemy enemy;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() throws Exception{
        this.enemy = new Enemy(0,'+'," ", new Coordinates(0,0),0,35,0,null,0,null,null,null,10);
        this.player1 = new MovieDirector(0, 'M', "David Lynch", new Coordinates(0,0), null, 5, null, 20, 20, 0, 20, 5, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
        this.player2 = new Musician(1, 'F', "Frank Zappa", new Coordinates(1,0), null, 5, null, 20, 20, 0, 20, 5, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
    }

    @After
    public void tearDown(){
        this.enemy = null;
        this.player1 = null;
        this.player2 = null;
        assertNull(enemy);
        assertNull(player1);
        assertNull(player2);
    }


    @Test
    public void dropxpTest(){
        enemy.updatePlayerOneDemage(25);
        enemy.updatePlayerTwoDemage(10);
        enemy.dropXP(player1, player2);
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 7, player1.getCurrXP());
        assertEquals("dropxpTest faild. PlayerOne currXP not as expected.", 3, player2.getCurrXP());
    
    
    }
}
