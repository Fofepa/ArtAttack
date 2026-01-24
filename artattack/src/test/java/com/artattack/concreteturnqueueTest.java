package com.artattack;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.artattack.level.Coordinates;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.turns.ConcreteTurnQueue;

import java.util.*;

public class concreteturnqueueTest {
    private ConcreteTurnQueue queue;

    @Before
    public void setUp(){
        queue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(List.of(new Enemy(0, '1', " ", null, EnemyType.EMPLOYEE), 
                new Player(1, '2', "o", new Coordinates(5,5)))));
        assertNotNull(queue);
    }

    @Test
    public void addTest(){
        queue.add(new Enemy(3, '3', "Davide", new Coordinates(1,0), EnemyType.EMPLOYEE));
        assertEquals(new Enemy(3, '3', "Davide", new Coordinates(1,0), EnemyType.EMPLOYEE), queue.getTurnQueue().get(2));
    }

    @Test
    public void removeTest(){
        queue.remove(new Player(1, '2', "o", new Coordinates(5,5)));
        assertEquals(1, queue.getTurnQueue().size());
        
    }

    @After
    public void tearDown(){
        queue = null;
        assertNull(queue);
    }
}
