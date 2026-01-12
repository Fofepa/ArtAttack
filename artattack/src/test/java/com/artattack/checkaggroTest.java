package com.artattack;

import java.util.*;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.MovementStrategy;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.moves.Weapon;


public class checkaggroTest {
    private MovementStrategy movementStrategy;
    private Maps map;
    private Enemy enemy;

    @Before
    public void setUp(){
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("8");
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(10,10),EnemyType.GUARD, 20, 20, 3,
                                 null,5,5,null,areaBuilder.getResult(),null,null,0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        areaBuilder.addShape("8");
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(12, 10), List.of(new Weapon("Hoe", "", 0)), 5,5, areaBuilder.getResult(), 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        mapBuilder.setTurnQueue();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        movementStrategy = new MovementStrategy(map, map.getPlayerOne());
        assertNotNull(movementStrategy);
    }

    @Test
    public void aggroTest(){
        movementStrategy.execute(0,-1);
        movementStrategy.acceptMovement();
        assertNotEquals(3, map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().size());
    }

    @After
    public void tearDown(){
        movementStrategy = null;
        assertNull(movementStrategy);
        map = null;
        assertNull(map);
        enemy = null;
        assertNull(enemy);
    }
    
}
