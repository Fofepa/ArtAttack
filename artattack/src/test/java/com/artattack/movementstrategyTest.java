package com.artattack;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.MovementStrategy;
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



public class movementstrategyTest {
    private MapBuilderTypeOne tmb;
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;
    private Player player;

    @Before
    public void setUp() throws Exception{
        EnemyDirector ed = new EnemyDirector();
        ConcreteEnemyBuilder eb = new ConcreteEnemyBuilder();
        PlayerDirector pd = new PlayerDirector();
        ConcretePlayerBuilder pb = new ConcretePlayerBuilder();
        ed.create(eb, EnemyType.GUARD, new Coordinates(10,10));
        Enemy enemy = eb.getResult();
        pd.create(pb, CharacterType.DIRECTOR, 0);
        Player p1 = pb.getResult();
        pd.create(pb, CharacterType.MUSICIAN, 1);
        Player p2 = pb.getResult();
        MapBuilder mapBuilder = new MapBuilderTypeOne(); 
        mapBuilder.setDimension(36, 150);
        mapBuilder.setSpawn(new Coordinates(5, 5), new Coordinates(4, 4));
        mapBuilder.setEnemies(new ArrayList<>(List.of(enemy)));
        mapBuilder.buildBorder();
        mapBuilder.setPlayerOne(p1);
        mapBuilder.setPlayerTwo(p2);
        mapBuilder.setDict();
        mapBuilder.setTurnQueue();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        this.map = mapBuilder.getResult();
        assertNotNull(map);
        move = new MovementStrategy(map, map.getPlayerOne());
        move.setMainFrame(new MainFrame(this.map));
        assertNotNull(move);
        //player = new MovieDirector();

    }

    @After
    public void tearDown() throws Exception{
        move = null;
        map = null;
        assertNull(map);
        assertNull(move);
    }

    @Test
    public void executeTest(){
        Coordinates t = move.getCursor();
        move.execute(0,-1);
        assertTrue(Coordinates.sum(t, new Coordinates(0, -1)).equals(move.getCursor()));    // it skips the player coordinates since the cursor is always at 0, 1 from the player.
    }

    @Test
    public void acceptmovementTest(){
        move.acceptMovement();
        assertTrue(map.getPlayerOne().getCoordinates().equals(new Coordinates(6,6)));

        move.execute(0,1);
        move.acceptMovement();
        assertEquals(3, map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().size());
        assertEquals(3, map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().size());

        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        move.execute(1,0);
        move.acceptMovement();
        assertEquals(2, map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().size());

    }
}