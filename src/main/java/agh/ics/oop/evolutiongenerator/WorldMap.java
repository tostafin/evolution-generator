package agh.ics.oop.evolutiongenerator;

import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    private final boolean boundedMap;
    private final GridPane gridPane;
    private final int width;
    private final int height;
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final int mapSize;
    private final int jungleSize;
    private final Map<Vector2d, List<Animal>> animals;
    private final List<Animal> animalsList;
    private final Map<Animal, Vector2d> jungleAnimals;
    private final Map<Vector2d, Grass> grassFields;
    private int steppeGrassFieldsNum;
    private int jungleGrassFieldsNum;
    private final List<IPositionChangeObserver> observers;
    private final int moveEnergy;
    private final int startEnergy;
    private final int plantEnergy;

    public WorldMap(boolean boundedMap, GridPane gridPane, int width, int height, int startEnergy, int moveEnergy, int plantEnergy,
                    int jungleWidth, int jungleHeight, Vector2d jungleLowerLeft, Vector2d jungleUpperRight) {
        this.boundedMap = boundedMap;
        this.gridPane = gridPane;
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.width - 1, this.height - 1);
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
        this.mapSize = this.width * this.height;
        this.jungleSize = (jungleWidth + 1) * (jungleHeight + 1);
        this.animals = new LinkedHashMap<>();
        this.animalsList = new ArrayList<>();
        this.jungleAnimals = new LinkedHashMap<>();
        this.grassFields = new LinkedHashMap<>();
        this.steppeGrassFieldsNum = 0;
        this.jungleGrassFieldsNum = 0;
        this.observers = new LinkedList<>();
        this.moveEnergy = moveEnergy;
        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
    }

    public boolean isBoundedMap() {
        return this.boundedMap;
    }

    public Vector2d getLowerLeft() {
        return this.lowerLeft;
    }

    public Vector2d getUpperRight() {
        return this.upperRight;
    }

    public GridPane getGridPane() {
        return this.gridPane;
    }

    public List<Animal> getAnimalsList() {
        return animalsList;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public boolean place(Animal animal) {
        if (this.canMoveTo(animal.getPosition())) {
            List<Animal> samePosAnimals = this.animals.get(animal.getPosition());
            if (samePosAnimals == null) {
                samePosAnimals = new ArrayList<>();
            }
            samePosAnimals.add(animal);
            samePosAnimals.sort(Comparator.comparing(Animal::getEnergy).reversed());
            this.animals.put(animal.getPosition(), samePosAnimals);
            this.animalsList.add(animal);
            if (isInJungle(animal.getPosition())) this.jungleAnimals.put(animal, animal.getPosition());
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException("Invalid animal's position.");
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (this.animals.containsKey(position) && this.animals.get(position).size() != 0) {
            return true;
        }
        return this.grassFields.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (this.animals.containsKey(position) && this.animals.get(position).size() != 0) {
            Animal animal = this.animals.get(position).get(0);
            this.eatGrass(animal);
            return animal;
        }
        if (this.grassFields.containsKey(position)) return this.grassFields.get(position);
        return null;
    }

    public void removeDeadAnimals() {
        Iterator<Animal> iter = this.animalsList.iterator();
        while (iter.hasNext()) {
            Animal animal = iter.next();
            if (animal.getEnergy() <= 0) {
                this.animals.get(animal.getPosition()).remove(animal);
                this.jungleAnimals.remove(animal);
                iter.remove();
                for (IPositionChangeObserver observer : this.observers) {
                    observer.positionChanged(animal, animal.getPosition(), new Vector2d(-2, -2), this,
                            this.gridPane);
                }
            }
        }
    }

    public void moveAnimals() {
        for (Animal a : this.animalsList) {
            Vector2d oldPos = a.getPosition();
            a.move(this.moveEnergy);
            Vector2d newPos = a.getPosition();
            if (this.isInJungle(newPos)) this.jungleAnimals.put(a, newPos);
            else this.jungleAnimals.remove(a);
            for (IPositionChangeObserver observer : this.observers) {
                observer.positionChanged(a, oldPos, newPos, this, this.gridPane);
            }
        }
    }

    public void eatGrass(Animal animal) {
        if (this.grassFields.get(animal.getPosition()) != null) {
            this.grassFields.remove(animal.getPosition());
            if (this.isInJungle(animal.getPosition())) this.jungleGrassFieldsNum--;
            else this.steppeGrassFieldsNum--;
            animal.addEnergy(this.plantEnergy);
        }
    }

    public boolean isInJungle(Vector2d position) {
        return this.jungleLowerLeft.x <= position.x && position.x <= this.jungleUpperRight.x &&
                this.jungleLowerLeft.y <= position.y && position.y <= this.jungleUpperRight.y;
    }

    public void addGrassFields() {
        if (this.jungleGrassFieldsNum < this.jungleSize - this.jungleAnimals.size()) {
            int jungleGrassFieldsAdded = 0;
            while (jungleGrassFieldsAdded < 1) {
                int x = ThreadLocalRandom.current().nextInt(this.jungleLowerLeft.x, this.jungleUpperRight.x + 1);
                int y = ThreadLocalRandom.current().nextInt(this.jungleLowerLeft.y, this.jungleUpperRight.y + 1);
                Vector2d pos = new Vector2d(x, y);
                if (!(isOccupied(pos))) {
                    this.grassFields.put(pos, new Grass(pos));
                    jungleGrassFieldsAdded++;
                }
            }
            this.jungleGrassFieldsNum += jungleGrassFieldsAdded;
        }

        if (this.steppeGrassFieldsNum < (this.mapSize - this.jungleSize) -
                (this.animalsList.size() - this.jungleAnimals.size())) {
            int steppeGrassFieldsAdded = 0;
            while (steppeGrassFieldsAdded < 1) {
                int x = ThreadLocalRandom.current().nextInt(0, this.width);
                int y = ThreadLocalRandom.current().nextInt(0, this.height);
                Vector2d pos = new Vector2d(x, y);
                if (!(isOccupied(pos)) && !(isInJungle(pos))) {
                    this.grassFields.put(pos, new Grass(pos));
                    steppeGrassFieldsAdded++;
                }
            }
            this.steppeGrassFieldsNum += steppeGrassFieldsAdded;
        }
    }

    public void reproduceAnimals() {
        for (var entry : this.animals.entrySet()) {
            List<Animal> samePosAnimals = entry.getValue();
            if (samePosAnimals.size() > 1 && samePosAnimals.get(1).getEnergy() >= this.startEnergy / 2) {
                Animal[] parents = {samePosAnimals.get(0), samePosAnimals.get(1)};
                Animal newBornAnimal = new Animal(this, parents[0].getPosition(),
                        (int) (0.25 * parents[0].getEnergy()) + (int) (0.25 * parents[1].getEnergy()),
                        new Genotype(parents[0], parents[1]));
                samePosAnimals.add(newBornAnimal);
                this.animals.put(entry.getKey(), samePosAnimals);
                this.animalsList.add(newBornAnimal);
                if (isInJungle(newBornAnimal.getPosition())) this.jungleAnimals.put(newBornAnimal,
                        newBornAnimal.getPosition());
                newBornAnimal.addObserver(this);
            }

        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition, WorldMap map, GridPane gridPane) {
            this.animals.get(oldPosition).remove(animal);
            List<Animal> newSamePosAnimals = this.animals.get(newPosition);
            if (newSamePosAnimals == null) newSamePosAnimals = new ArrayList<>();
            newSamePosAnimals.add(animal);
            newSamePosAnimals.sort(Comparator.comparing(Animal::getEnergy).reversed());
            this.animals.put(newPosition, newSamePosAnimals);
    }
}
