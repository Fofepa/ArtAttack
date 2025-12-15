package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class mapTest {
    private Maps map;
    
    
    @Before
    public void setUp() throws Exception{
        map = new Maps();
        assertNotNull(map);
    }

    @After
    public void tearDown() throws Exception{
        map = null;
        assertNull(map);
    }

    @Test
    public void setcellTest(){
        map.setCell(1,1,'#');
        assertEquals('#', map.getCell(1,1));

    }  
}