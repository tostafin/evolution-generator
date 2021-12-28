package agh.ics.oop.evolutiongenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {
    WorldMap worldMap = new WorldMap(11, 11, 10, 10, 2, 2,
            new Vector2d(4, 4), new Vector2d(6, 6));
    Animal animalGregory = new Animal(this.worldMap, new Vector2d(2, 2), 4);
    Animal animalJohannes = new Animal(this.worldMap, new Vector2d(4, 7), 4);
    Animal animalMargaret = new Animal(this.worldMap, new Vector2d(7, 8), 4);

    @Test
    void canMoveTo() {
        assertFalse(this.worldMap.canMoveTo(new Vector2d(21, 1)));
        assertFalse(this.worldMap.canMoveTo(new Vector2d(3, -2)));
        assertTrue(this.worldMap.canMoveTo(new Vector2d(2, 1)));
    }

    @Test
    void place() {
        Animal Carlson = new Animal(this.worldMap, new Vector2d(14, 7), 4);
        assertTrue(this.worldMap.place(this.animalGregory));
        assertTrue(this.worldMap.place(this.animalJohannes));
        assertTrue(this.worldMap.place(this.animalMargaret));
        assertThrows(IllegalArgumentException.class, ()-> this.worldMap.place(Carlson));
    }

    @Test
    void isOccupied() {
        this.worldMap.place(this.animalGregory);
        this.worldMap.place(this.animalJohannes);
        this.worldMap.place(this.animalMargaret);
        assertTrue(this.worldMap.isOccupied(this.animalGregory.getPosition()));
        assertTrue(this.worldMap.isOccupied(this.animalJohannes.getPosition()));
        assertTrue(this.worldMap.isOccupied(this.animalMargaret.getPosition()));
    }

    @Test
    void objectAt() {
        this.worldMap.place(this.animalGregory);
        this.worldMap.place(this.animalJohannes);
        this.worldMap.place(this.animalMargaret);
        assertEquals(this.worldMap.objectAt(this.animalGregory.getPosition()), this.animalGregory);
        assertEquals(this.worldMap.objectAt(this.animalJohannes.getPosition()), this.animalJohannes);
        assertEquals(this.worldMap.objectAt(this.animalMargaret.getPosition()), this.animalMargaret);
    }
}