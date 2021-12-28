package agh.ics.oop.evolutiongenerator;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void equals() {
        assertEquals(new Vector2d(10, 71), new Vector2d(10, 71));
        assertNotEquals(new Vector2d(-6, -7), new Vector2d(-6, -8));

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        assertEquals(new Vector2d(a, b), new Vector2d(a, b));
        int c = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int d = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        while (a == c && b == d) d = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        assertNotEquals(new Vector2d(a, b), new Vector2d(c, d));
    }

    @Test
    public void testToString() {
        assertEquals("(2, -1)", new Vector2d(2, -1).toString());
        assertNotEquals("(2, -1)", new Vector2d(-2, -1).toString());

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        String resString = "(" + a + ", " + b + ")";
        assertEquals(new Vector2d(a, b).toString(), resString);
        int c;
        do {
            c = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        } while (c == b);
        assertNotEquals(new Vector2d(a, c).toString(), resString);
    }

    @Test
    public void precedes() {
        assertTrue(new Vector2d(2, 5).precedes(new Vector2d(3, 5)));
        assertTrue(new Vector2d(2, 5).precedes(new Vector2d(2, 7)));
        assertFalse(new Vector2d(2, 5).precedes(new Vector2d(3, 4)));
        assertFalse(new Vector2d(2, 5).precedes(new Vector2d(1, 6)));

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int c = ThreadLocalRandom.current().nextInt(a, 1 << 15 + 1);
        int d = ThreadLocalRandom.current().nextInt(b, 1 << 15 + 1);
        assertTrue(new Vector2d(a, b).precedes(new Vector2d(c, d)));
        c = ThreadLocalRandom.current().nextInt(-(1 << 15), a + 1);
        d = ThreadLocalRandom.current().nextInt(-(1 << 15), b + 1);
        assertFalse(new Vector2d(a, b).precedes(new Vector2d(c, d)));
    }

    @Test
    public void follows() {
        assertTrue(new Vector2d(4, 6).follows(new Vector2d(4, 5)));
        assertTrue(new Vector2d(7, 11).follows(new Vector2d(5, 10)));
        assertFalse(new Vector2d(1, 2).follows(new Vector2d(1, 3)));
        assertFalse(new Vector2d(123, 456).follows(new Vector2d(456, 789)));

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int c = ThreadLocalRandom.current().nextInt(-(1 << 15), a + 1);
        int d = ThreadLocalRandom.current().nextInt(-(1 << 15), b + 1);
        assertTrue(new Vector2d(a, b).follows(new Vector2d(c, d)));
        c = ThreadLocalRandom.current().nextInt(-(1 << 15), a + 1);
        d = ThreadLocalRandom.current().nextInt(-(1 << 15), b + 1);
        assertFalse(new Vector2d(a, b).precedes(new Vector2d(c, d)));
    }

    @Test
    public void add() {
        assertEquals(new Vector2d(2, 6).add(new Vector2d(4, 9)), new Vector2d(6, 15));
        assertEquals(new Vector2d(12, 65).add(new Vector2d(44, 35)), new Vector2d(56, 100));
        assertNotEquals(new Vector2d(-5, -2).add(new Vector2d(4, 3)), new Vector2d(-1, 0));
        assertNotEquals(new Vector2d(12, 13).add(new Vector2d(0, 3)), new Vector2d(12, 15));

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int c = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int d = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        assertEquals(new Vector2d(a, b).add(new Vector2d(c, d)), new Vector2d(a + c, b + d));
        assertNotEquals(new Vector2d(a, b).add(new Vector2d(c, d)), new Vector2d(a + c - 1, b + d + 1));
    }

    @Test
    public void subtract() {
        assertEquals(new Vector2d(33, 22).subtract(new Vector2d(10, 11)), new Vector2d(23, 11));
        assertEquals(new Vector2d(100, 200).subtract(new Vector2d(10, 240)), new Vector2d(90, -40));
        assertNotEquals(new Vector2d(100, 200).subtract(new Vector2d(10, -60)), new Vector2d(-80, 140));
        assertNotEquals(new Vector2d(-10, -20).subtract(new Vector2d(-30, -40)), new Vector2d(-8300, 1140));

        int a = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int b = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int c = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        int d = ThreadLocalRandom.current().nextInt(-(1 << 15), 1 << 15 + 1);
        assertEquals(new Vector2d(a, b).subtract(new Vector2d(c, d)), new Vector2d(a - c, b - d));
        assertNotEquals(new Vector2d(a, b).subtract(new Vector2d(c, d)), new Vector2d(a - c - 1, b - d + 1));
    }
}