package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;


public class cureTest{
    private Item i;
    private Characters c;
    private static int k;

    @Before
    public void setUp() throws Exception{
        c = new MovieDirector();
        assertNotNull(c);
        i = new Cure();
        assertNotNull(i);
        k = 0;
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
        assertEquals("Failed cure use", 
        c.getCurrHP() + i.getHealAmount(), 
        c.getItem(k).use()); //use() expects returns from useItem
    }

}