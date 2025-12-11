package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class attackbuffTest extends TestCase {

    private AttackBuff ab;

    @Before
    public void setUp() throws Exception {
        ab = new AttackBuff();
        assertNotNull(ab);
    }

    @After
    public void tearDown() throws Exception {
        ab = null;
        assertNull(ab);
    }

    @Test
    public void testUse() {
        assertEquals(0, 0);
    }

}