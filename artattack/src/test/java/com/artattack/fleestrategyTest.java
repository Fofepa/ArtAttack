package com.artattack;

import java.util.*;

import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.artattack.view.MainFrame;


import org.junit.Before;

public class fleestrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private FleeStrategy flee;

    @Before
    public void setUp(){
                this.mainFrame = new MainFrame();
        assertNotNull(mainFrame);
        
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(3,70),EnemyType.GUARD, 20, 20, 3,
                                 null,5,List.of(new Coordinates(-1, -1), new Coordinates(-1, 0), new Coordinates(-1, 1), new Coordinates(0, -1), new Coordinates(-2, 0), 
                                new Coordinates(0, 2), new Coordinates(0, -2), new Coordinates(2, 0), new Coordinates(-2, -2), new Coordinates(2, 2), new Coordinates(2, -2), new Coordinates(-2, 2), 
                                new Coordinates(0, 1), new Coordinates(1, -1), new Coordinates(1, 0), new Coordinates(1, 1)),null,null,null,0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(3, 78), List.of(new Weapon("Hoe", "", 0)), 5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(1, 1), List.of(new Weapon("Hoe", "", 0)), 5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        flee = new FleeStrategy(mainFrame);
    }

    @Test
    public void executeTest(){
        flee.execute(enemy,map);
        assertTrue(enemy.getCoordinates().equals(new Coordinates(3,72)));
    }

    @After
    public void tearDown(){
        map = null;
        assertNull(map);
        enemy = null;
        assertNull(enemy);
        flee = null;
        assertNull(flee);
        mainFrame = null;
        assertNull(mainFrame);
    }
}
