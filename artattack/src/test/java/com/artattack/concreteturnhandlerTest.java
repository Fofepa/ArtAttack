package com.artattack;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;

public class concreteturnhandlerTest {
    private ConcreteTurnHandler turn;
    private ConcreteTurnQueue queue;

    @Before
    public void setUp(){
        this.queue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(List.of(new Enemy(0, '1', " ", null, EnemyType.EMPLOYEE), 
                new Player(1, '2', "o", null))));
        this.turn = new ConcreteTurnHandler(this.queue);
        assertNotNull(this.queue);
        assertNotNull(this.turn);
    }

    @Test
    public void hasnextTest(){
        assertTrue(turn.hasNext());
        ActiveElement t = turn.next();
        ActiveElement g = turn.next();
        assertEquals(0, turn.getIndex());
    }

    @Test
    public void nextTest(){
        Enemy enemy = (Enemy)turn.current();
        assertEquals(enemy, queue.getTurnQueue().get(0));
        Player lynch = (Player)turn.next();
        assertEquals(lynch, queue.getTurnQueue().get(1));
    }

    @Test
    public void currentTest(){
        Enemy enemy = (Enemy)turn.current();
        assertEquals(enemy, turn.current());
    }

    @Test
    public void resetindexTest(){
        turn.next();
        turn.next();
        turn.resetIndex();
        assertEquals(0,turn.getIndex());

    }

    @After
    public void tearDown(){
        queue = null;
        turn = null;
        assertNull(queue);
        assertNull(turn);
    }
}
