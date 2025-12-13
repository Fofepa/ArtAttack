package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;



public class moviedirectorTest{

    private MovieDirector m ;

    @Before
    public void setUp() throws Exception{
        m = new MovieDirector(0,'M', "David Lynch", new Coordinates(0,0));
        assertNotNull(m);
    }
    
    @After
    public void tearDown() throws Exception{
        m = null;
        assertNull(m);
    }

    @Test
    public void setcoordinatesTest(){

        Coordinates new_coord = new Coordinates(1,1);

        m.setCoordinates(new_coord);
        assertEquals(new_coord,m.getCoordinates());
    }

    

}
