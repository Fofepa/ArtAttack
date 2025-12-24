package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;


public class cureTest{
    private Cure c;
    private Cure c_;
    private Player p;

    @Before
    public void setUp() throws Exception{
        c = new Cure("TestCure", "TestDescription", 1);
        c_ = new Cure("TestCure", "TestDescription", 11);
        p = new MovieDirector(0, 'M', "David Lynch", new Coordinates(0,0), 
            null, 10, null, 10, 20, 0, 20, 1, 5);
        
        assertNotNull(c);
        assertNotNull(c_);
        assertNotNull(p);
    }

    @After
    public void tearDown() throws Exception{
        c = null;
        c_ = null;
        p = null;

        assertNull(c);
        assertNull(c_);
        assertNull(p);
    }

    @Test
    public void testUse(){
        assertEquals("Cure.use(Player player) has failed", 1, c.use(p)); //Case: cureHP doesn't exceed maxHP
        p.updateHP(-1);
        assertEquals("Cure.use(Player player) has failed", 10, c_.use(p)); //Case: cureHP exceeds maxHP
        p.updateHP(-10);
    }

}