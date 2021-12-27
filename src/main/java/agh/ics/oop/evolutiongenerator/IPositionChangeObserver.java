package agh.ics.oop.evolutiongenerator;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}