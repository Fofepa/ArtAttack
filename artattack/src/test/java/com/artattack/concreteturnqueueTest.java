package com.artattack;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class concreteturnqueueTest {
    private ConcreteTurnQueue queue;

    @Before
    public void setUp(){
        queue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(List.of(new Enemy(0, '1', " ", null), 
                new MovieDirector(1, '2', "o", new Coordinates(5,5)))));
        assertNotNull(queue);
    }

    @Test
    public void addTest(){
        queue.add(new Enemy(3, '3', "Davide", new Coordinates(1,0)));
        assertEquals(new Enemy(3, '3', "Davide", new Coordinates(1,0)), queue.getTurnQueue().get(2));
    }

    @Test
    public void removeTest(){
        queue.remove(new MovieDirector(1, '2', "o", new Coordinates(5,5)));
        assertEquals(1, queue.getTurnQueue().size());
        
    }

    @After
    public void tearDown(){
        queue = null;
        assertNull(queue);
    }
}
