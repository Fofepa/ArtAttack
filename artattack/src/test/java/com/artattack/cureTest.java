package com.artattack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.artattack.Item;
import com.artattack.Characters;


public class cureTest {
    private Item i;
    private Characters c;

    @Before
    public void setUp() throws Exception{
        c = new MovieDirector();
        assertNotNull(c);
        i = new Item();
        assertNotNull(i);
    }

    @After
    public void tearDown() throws Exception{
        c = null;
        i = null;

        assertNull(c);
        assertNull(i);
    }

    @Test
    public void testUse(){
        assertEquals("Failed cure use", c.getCurrHP() + i.getHealAmount(), c.getItem(i).use());
    }

}