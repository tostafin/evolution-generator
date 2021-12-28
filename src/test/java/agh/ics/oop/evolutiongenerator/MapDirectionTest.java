package agh.ics.oop.evolutiongenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void next() {
        assertEquals(MapDirection.NORTH.next(), MapDirection.NORTH_EAST);
        assertEquals(MapDirection.NORTH_EAST.next(), MapDirection.EAST);
        assertEquals(MapDirection.EAST.next(), MapDirection.SOUTH_EAST);
        assertEquals(MapDirection.SOUTH_EAST.next(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(), MapDirection.SOUTH_WEST);
        assertEquals(MapDirection.SOUTH_WEST.next(), MapDirection.WEST);
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTH_WEST);
        assertEquals(MapDirection.NORTH_WEST.next(), MapDirection.NORTH);
    }

    @Test
    void previous() {
        assertEquals(MapDirection.NORTH.previous(), MapDirection.NORTH_WEST);
        assertEquals(MapDirection.NORTH_WEST.previous(), MapDirection.WEST);
        assertEquals(MapDirection.WEST.previous(), MapDirection.SOUTH_WEST);
        assertEquals(MapDirection.SOUTH_WEST.previous(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.SOUTH_EAST);
        assertEquals(MapDirection.SOUTH_EAST.previous(), MapDirection.EAST);
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTH_EAST);
        assertEquals(MapDirection.NORTH_EAST.previous(), MapDirection.NORTH);
    }
}