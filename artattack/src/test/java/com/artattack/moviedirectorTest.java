package com.artattack;

import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;



public class moviedirectorTest{

    private MovieDirector m;

    @Before
    public void setUp() throws Exception{
        m = new MovieDirector(0, 'M', "David Lynch", new Coordinates(0,0), null, 5, null, 20, 20, 0, 20, 5, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
        /* m = new MovieDirector(0,'M', "David Lynch", new Coordinates(0,0), 20, 20, 0, 20, 1, 5, 2, new ArrayList<Weapon>(),
        25, new ArrayList<Item>(), new ArrayList<Coordinates>(), new ArrayList<Coordinates>()); */
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

    @Test
    public void additemTest(){
        m.addItem(new Cure("Pozza","This one heals a lot", 50)); 
        assertEquals(1,m.getInventory().size());
        assertEquals("Pozza",m.getInventory().get(0).getName());
        assertEquals("This one heals a lot",m.getInventory().get(0).getDescription());
        assertEquals(50,(m.getInventory().get(0)).getAmount());
        m.getInventory().clear();
        m.addItem(new Cure("Pozza","This one heals a lot", 60)); 
         assertEquals(60,(m.getInventory().get(0)).getAmount());

    }
}