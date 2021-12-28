package agh.ics.oop.evolutiongenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement {
    private final WorldMap map;
    private Vector2d animalsPos;
    private MapDirection animalsDir;
    private final List<IPositionChangeObserver> observers;
    private final Genotype genotype;
    private int energy;

    public Animal(WorldMap map, Vector2d initialPosition, int startEnergy) {
        this.map = map;
        this.animalsPos = initialPosition;
        this.animalsDir = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, 8)];
        this.observers = new LinkedList<>();
        this.genotype = new Genotype();
        this.energy = startEnergy;
    }

    public Animal(WorldMap map, Vector2d initialPosition, int startEnergy, Genotype genotype) {
        this.map = map;
        this.animalsPos = initialPosition;
        this.animalsDir = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, 8)];
        this.observers = new LinkedList<>();
        this.genotype = genotype;
        this.energy = startEnergy;
    }

    @Override
    public Vector2d getPosition() {
        return animalsPos;
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    @Override
    public String getImageSource() {
        switch (this.animalsDir) {
            case NORTH:
                return "src/main/resources/up.png";

            case NORTH_EAST:
                return "src/main/resources/up_right.png";

            case EAST:
                return "src/main/resources/right.png";

            case SOUTH_EAST:
                return "src/main/resources/down_right.png";

            case SOUTH:
                return "src/main/resources/down.png";

            case SOUTH_WEST:
                return "src/main/resources/down_left.png";

            case WEST:
                return "src/main/resources/left.png";

            case NORTH_WEST:
                return "src/main/resources/up_left.png";

            default:
                return null;
        }
    }

    public void move(int moveEnergy) {
        int move = this.genotype.getAnimalsMove();
        Vector2d oldPos = this.animalsPos;
        switch (move) {
            case 0:
                Vector2d newPosForward = this.animalsPos.add(Objects.requireNonNull(this.animalsDir.toUnitVector()));
                this.moveAnimal(oldPos, newPosForward);
                break;

            case 1:
                this.animalsDir = this.animalsDir.next();
                break;

            case 2:
                assert this.animalsDir.next() != null;
                this.animalsDir = this.animalsDir.next().next();
                break;

            case 3:
                assert this.animalsDir.next() != null;
                assert this.animalsDir.next().next() != null;
                this.animalsDir = this.animalsDir.next().next().next();
                break;

            case 4:
                Vector2d newPosBackward = this.animalsPos.subtract(Objects.requireNonNull(this.animalsDir.toUnitVector()));
                this.moveAnimal(oldPos, newPosBackward);
                break;

            case 5:
                assert this.animalsDir.previous() != null;
                assert this.animalsDir.previous().previous() != null;
                this.animalsDir = this.animalsDir.previous().previous().previous();
                break;

            case 6:
                assert this.animalsDir.previous() != null;
                this.animalsDir = this.animalsDir.previous().previous();
                break;

            case 7:
                this.animalsDir = this.animalsDir.previous();
                break;
        }
        this.energy -= moveEnergy;
    }

    public void moveAnimal(Vector2d oldPos, Vector2d newPos) {
        if (this.map.isBoundedMap()) {
            if (this.map.canMoveTo(newPos)) {
                this.animalsPos = newPos;
                this.positionChanged(oldPos, this.animalsPos);
            }
        }
        else {
            this.moveOnBorders(newPos);
            if (this.animalsPos.equals(oldPos)) {
                this.animalsPos = newPos;
            }
            this.positionChanged(oldPos, this.animalsPos);
        }
    }

    public void moveOnBorders(Vector2d newPos) {
        int upperBoundary = this.map.getUpperRight().y + 1;
        int rightBoundary = this.map.getUpperRight().x + 1;
        int lowerBoundary = this.map.getLowerLeft().y - 1;
        int leftBoundary = this.map.getLowerLeft().x - 1;
        if (newPos.y == upperBoundary) this.animalsPos = new Vector2d(newPos.x, lowerBoundary + 1);
        if (newPos.x == rightBoundary) this.animalsPos = new Vector2d(leftBoundary + 1, newPos.y);
        if (newPos.y == lowerBoundary) this.animalsPos = new Vector2d(newPos.x, upperBoundary - 1);
        if (newPos.x == leftBoundary) this.animalsPos = new Vector2d(rightBoundary - 1, newPos.y);
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver obs : this.observers) obs.positionChanged(this, oldPosition, newPosition,
                this.map, this.map.getGridPane());
    }
}