package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.enemystrategy.ApproachStrategy;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.moves.Weapon;
import com.artattack.view.MainFrame;

public class approachstrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private ApproachStrategy approach;
    
    @Before
    public void setUp(){
        
        this.enemy = new Enemy(0, 'E', "Frank", new Coordinates(10,10),EnemyType.GUARD, 20, 20, 3,
        null,5,5,List.of(new Coordinates(-1, -1), new Coordinates(-1, 0), new Coordinates(-1, 1), new Coordinates(0, -1), new Coordinates(-2, 0), 
        new Coordinates(0, 2), new Coordinates(0, -2), new Coordinates(2, 0), new Coordinates(-2, -2), new Coordinates(2, 2), new Coordinates(2, -2), new Coordinates(-2, 2), 
        new Coordinates(0, 1), new Coordinates(1, -1), new Coordinates(1, 0), new Coordinates(13,24), new Coordinates(1, 1)),null,null,null,0);
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Player(1, '@', "Zappa", new Coordinates(15, 34), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null, PlayerType.MUSICIAN));
        mapBuilder.setPlayerTwo(new Player(0, '@', "Lynch", new Coordinates(25, 34), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null, PlayerType.MOVIE_DIRECTOR));
        mapBuilder.setEnemies(List.of(enemy));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        this.mainFrame = new MainFrame(this.map);
        assertNotNull(mainFrame);
        approach = new ApproachStrategy(mainFrame);
    }

    @Test
    public void executeTest(){
        approach.execute(enemy,map);
        assertTrue(enemy.getCoordinates().equals(new Coordinates(23, 34)));
    }
    
    
     @After
    public  void tearDown(){
        mainFrame = null;
        assertNull(mainFrame);
        map = null;
        assertNull(map);
        enemy = null;
        assertNull(enemy);
        approach = null;
        assertNull(approach);
    }
}
