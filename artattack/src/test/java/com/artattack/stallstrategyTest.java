package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.enemystrategy.StallStrategy;
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
public class stallstrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private StallStrategy stall;

    @Before
    public void setUp(){
        
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(10,10),EnemyType.EMPLOYEE, 20, 20, 3,
                                 null,5,5,List.of(new Coordinates(-1, -1)),null,null,null,0);
                                 MapBuilder mapBuilder = new TestMapBuilder(); 
                                 mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(15, 34), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
                                 mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(25, 34), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        this.mainFrame = new MainFrame(map);
        assertNotNull(mainFrame);
        stall = new StallStrategy(mainFrame);
    }

    @Test
    public void executeTest(){
        stall.execute(enemy,map);
        assertEquals(4,enemy.getActionPoints());
    }
    
    @After
    public void tearDown(){
        enemy = null;
        assertNull(enemy);
        mainFrame = null;
        assertNull(mainFrame);
        stall = null;
        assertNull(stall);
        map = null;
        assertNull(map);
    }
}
