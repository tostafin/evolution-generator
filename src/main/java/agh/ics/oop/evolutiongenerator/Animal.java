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

    public Animal(WorldMap map, Vector2d initialPosition, int energy) {
        this.map = map;
        this.animalsPos = initialPosition;
        this.animalsDir = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, 8)];
        this.observers = new LinkedList<>();
        this.genotype = new Genotype();
        this.energy = energy;
    }

    @Override
    public Vector2d getPosition() {
        return animalsPos;
    }

    public int getEnergy() {
        return this.energy;
    }

    public String toString() {
        switch (animalsDir) {
            case NORTH:
                return "N";

            case NORTH_EAST:
                return "NE";

            case EAST:
                return "E";

            case SOUTH_EAST:
                return "SE";

            case SOUTH:
                return "S";

            case SOUTH_WEST:
                return "SW";

            case WEST:
                return "W";

            case NORTH_WEST:
                return "NW";
        }
        return null;
    }

    public void move() {
        int move = this.genotype.getAnimalsMove();
        Vector2d oldPos = this.animalsPos;
        switch (move) {
            case 0:
                if (this.map.canMoveTo(this.animalsPos.add(Objects.requireNonNull(this.animalsDir.toUnitVector())))) {
                    this.animalsPos = this.animalsPos.add(Objects.requireNonNull(this.animalsDir.toUnitVector()));
                    this.positionChanged(oldPos, this.animalsPos);
                }
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
                if (this.map.canMoveTo(this.animalsPos.subtract(Objects.requireNonNull(this.animalsDir.toUnitVector())))) {
                    this.animalsPos = this.animalsPos.subtract(Objects.requireNonNull(this.animalsDir.toUnitVector()));
                    this.positionChanged(oldPos, this.animalsPos);
                }
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
        this.energy--;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver obs : this.observers) obs.positionChanged(this, oldPosition, newPosition);
    }

    @Override
    public String getSource() {
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
}