package com.artattack;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import com.artattack.MovieDirector;

public class moviedirectorTest extends TestCase {

    private MovieDirector m ;

    @Before
    public void setUp() throws Exception{
        m = new MovieDirector();
        assertNotNull(m);
    }
    
    @After
    public void tearDown() throws Exception{
        m = null;
        assertNull(m);
    }

    @Test
    public void testMove(){
        assertEquals(0, 0);
    }

}
