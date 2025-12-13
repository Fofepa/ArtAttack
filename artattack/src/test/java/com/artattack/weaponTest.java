package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class weaponTest extends TestCase {

    private Weapon w;

    @Before
    public void setUp() throws Exception {
        w = new Weapon();
        assertNotNull(w);
    }

    @After
    public void tearDown() throws Exception {
        w = null;
        assertNull(w);
    }

    @Test
    public void testUse() {
        assertEquals(0, 0);
    }

}