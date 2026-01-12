package com.artattack;


import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.MovementStrategy;
import com.artattack.interactions.Talk;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.Player;
import com.artattack.view.InteractionPanel;



public class movementstrategyTest {
    private TestMapBuilder tmb;
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;
    private Player player;

    @Before
    public void setUp() throws Exception{
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(0, 1)));
        tmb.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of("Ciao!"))), "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null
         )));
        tmb.setEnemies(List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(2, 3), EnemyType.EMPLOYEE, 10, 10, 1, null, 10,10, null, List.of(new Coordinates(1, 1)), null,null, 0),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25), EnemyType.EMPLOYEE, 20, 20, 2, null, 15,15, null, List.of(new Coordinates(10, 10)), null,null ,0)));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        assertNotNull(map);
        move = new MovementStrategy(map, new Musician(0,'M',"Frank Zappa",new Coordinates(1,1)));
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
        move.execute(0,1);
        assertEquals(new Coordinates(t.getX(),t.getY()+1),move.getCursor());

        t = move.getCursor();
        move.execute(1,2);
        assertNotEquals(new Coordinates(t.getX()+1,t.getY()+2),move.getCursor());

        t = move.getCursor();
        move.execute(3,3);
        assertNotEquals(new Coordinates(t.getX()+3,t.getY()+3),move.getCursor());


    }

    @Test
    public void acceptmovementTest(){
        move.acceptMovement();
        assertEquals(move.getCursor(),move.getPlayer().getCoordinates());

        move.execute(1,0);
        move.acceptMovement();
        assertEquals(new Enemy(0, 'E', "Goblin", new Coordinates(2, 3), EnemyType.EMPLOYEE), map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().get(0));

        
    }
}