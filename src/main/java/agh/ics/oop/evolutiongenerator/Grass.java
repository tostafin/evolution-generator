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

    @Override
    public String getImageSource() {
        return "src/main/resources/grass.png";
    }

    @Override
    public int getEnergy() {
        return -1;
    }
}