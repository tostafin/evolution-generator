package agh.ics.oop.evolutiongenerator;

import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    WorldMap unboundedWorldMap = new WorldMap(false, new GridPane(), 11, 11, 10, 10, 10, 2, 2,
            new Vector2d(4, 4), new Vector2d(6, 6));
    Animal animalGregory = new Animal(this.unboundedWorldMap, new Vector2d(2, 2), 4);
    @Test
    void move() {
        Vector2d oldPos = this.animalGregory.getPosition();
        MapDirection oldDir = this.animalGregory.getAnimalsDir();
        int oldEnergy = this.animalGregory.getEnergy();
        int moveEnergy = 2;
        this.animalGregory.move(moveEnergy);
        assertTrue(!(oldPos.equals(this.animalGregory.getPosition())) ||
                oldDir != this.animalGregory.getAnimalsDir());
        assertEquals(oldEnergy, this.animalGregory.getEnergy() + moveEnergy);
    }
}