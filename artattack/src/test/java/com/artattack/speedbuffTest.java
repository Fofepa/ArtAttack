package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class speedbuffTest{

    private SpeedBuff sb;

    @Before
    public void setUp() throws Exception {
        sb = new SpeedBuff();
        assertNotNull(sb);
    }

    @After
    public void tearDown() throws Exception {
        sb = null;
        assertNull(sb);
    }

    @Test
    public void testUse() {
        assertEquals(0, 0);
    }

}