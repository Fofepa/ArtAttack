package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.enemystrategy.FleeStrategy;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.moves.Weapon;
import com.artattack.view.MainFrame;

public class fleestrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private FleeStrategy flee;

    @Before
    public void setUp(){
        
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(3,70),EnemyType.GUARD, 20, 20, 3,
        null,5,5,List.of(new Coordinates(-1, -1), new Coordinates(-1, 0), new Coordinates(-1, 1), new Coordinates(0, -1), new Coordinates(-2, 0), 
                                new Coordinates(0, 2), new Coordinates(0, -2), new Coordinates(2, 0), new Coordinates(-2, -2), new Coordinates(2, 2), new Coordinates(2, -2), new Coordinates(-2, 2), 
                                new Coordinates(0, 1), new Coordinates(1, -1), new Coordinates(1, 0), new Coordinates(1, 1)),null,null,null,0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(3, 78), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(1, 1), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        this.mainFrame = new MainFrame(map);
        assertNotNull(mainFrame);
        flee = new FleeStrategy(mainFrame);
    }

    @Test
    public void executeTest(){
        flee.execute(enemy,map);
        Coordinates oldPos = enemy.getCoordinates();
        double oldDist = Math.min(
        Coordinates.getDistance(oldPos, map.getPlayerOne().getCoordinates()),
        Coordinates.getDistance(oldPos, map.getPlayerTwo().getCoordinates())
        );

        flee.execute(enemy, map);

        Coordinates newPos = enemy.getCoordinates();
        double newDist = Math.min(
            Coordinates.getDistance(newPos, map.getPlayerOne().getCoordinates()),
            Coordinates.getDistance(newPos, map.getPlayerTwo().getCoordinates())
        );
        
        assertTrue(newDist > oldDist); //Check if the new distance from the player is greater than the old one
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
