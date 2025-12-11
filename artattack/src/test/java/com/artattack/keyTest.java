package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class keyTest extends TestCase {

    private Key k;

    @Before
    public void setUp() throws Exception {
        k = new Key();
        assertNotNull(k);
    }

    @After
    public void tearDown() throws Exception {
        k = null;
        assertNull(k);
    }

    @Test
    public void testUse() {
        assertEquals(0, 0);
    }

}