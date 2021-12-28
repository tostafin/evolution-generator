package agh.ics.oop.evolutiongenerator;

import javafx.scene.layout.GridPane;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition, WorldMap map, GridPane gridPane);
}