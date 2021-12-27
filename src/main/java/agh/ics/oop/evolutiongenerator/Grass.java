package agh.ics.oop.evolutiongenerator;

public class Grass implements IMapElement {
    private final Vector2d position;
    public Grass(Vector2d pos) {
        this.position = pos;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return "*";
    }

    @Override
    public String getSource() {
        return "src/main/resources/grass.png";
    }
}