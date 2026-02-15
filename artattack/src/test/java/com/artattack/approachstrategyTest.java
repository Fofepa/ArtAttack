package com.artattack;

import java.util.ArrayList;
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
import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.Maps;
import com.artattack.mapelements.ConcreteEnemyBuilder;
import com.artattack.mapelements.ConcretePlayerBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyDirector;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerDirector;
import com.artattack.view.CharacterType;
import com.artattack.view.MainFrame;

public class approachstrategyTest {
    private Maps map;
    private Enemy enemy;
    private MainFrame mainFrame;
    private ApproachStrategy approach;
    
    @Before
    public void setUp(){
        
        EnemyDirector ed = new EnemyDirector();
        ConcreteEnemyBuilder eb = new ConcreteEnemyBuilder();
        PlayerDirector pd = new PlayerDirector();
        ConcretePlayerBuilder pb = new ConcretePlayerBuilder();
        ed.create(eb, EnemyType.GUARD, new Coordinates(10,10));
        this.enemy = eb.getResult();
        pd.create(pb, CharacterType.DIRECTOR, 0);
        Player p1 = pb.getResult();
        pd.create(pb, CharacterType.MUSICIAN, 1);
        Player p2 = pb.getResult();
        MapBuilder mapBuilder = new MapBuilderTypeOne(); 
        mapBuilder.setDimension(36, 150);
        mapBuilder.setSpawn(new Coordinates(12, 12), new Coordinates(26, 23));
        mapBuilder.setEnemies(new ArrayList<>(List.of(enemy)));
        mapBuilder.buildBorder();
        mapBuilder.setPlayerOne(p1);
        mapBuilder.setPlayerTwo(p2);
        mapBuilder.setDict();
        mapBuilder.setTurnQueue();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        this.map.getConcreteTurnHandler().getConcreteTurnQueue().add(enemy);
        this.mainFrame = new MainFrame(this.map);
        assertNotNull(mainFrame);
        approach = new ApproachStrategy(mainFrame);
    }

    @Test
    public void executeTest(){
        double c_old = Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerOne().getCoordinates());
        approach.execute(enemy,map);
        double c_new = Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerOne().getCoordinates());

        assertTrue(c_new < c_old);
        
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
