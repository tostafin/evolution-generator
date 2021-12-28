package agh.ics.oop.evolutiongenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    private final int width;
    private final int height;
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Map<Vector2d, List<Animal>> animals;
    private final List<Animal> animalsList;
    private final List<Animal> deadAnimalsList;
    private final Map<Vector2d, Grass> grassFields;
    private int numOfGrassFields;
    private final List<IPositionChangeObserver> observers;
    private final int moveEnergy;
    private final int plantEnergy;

    public WorldMap(int width, int height, int moveEnergy, int plantEnergy, Vector2d jungleLowerLeft, Vector2d jungleUpperRight) {
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.width - 1, this.height - 1);
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
        this.animals = new LinkedHashMap<>();
        this.animalsList = new ArrayList<>();
        this.deadAnimalsList = new ArrayList<>();
        this.grassFields = new LinkedHashMap<>();
        this.numOfGrassFields = 0;
        this.observers = new LinkedList<>();
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.addGrassFields();
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
            animal.addObserver(this);
            return true;
        }
        return false;
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
                this.deadAnimalsList.add(animal);
                iter.remove();
                for (IPositionChangeObserver observer : this.observers) {
                    observer.positionChanged(animal, animal.getPosition(), new Vector2d(-1, -1));
                }
            }
        }
    }

    public void moveAnimals() {
        for (Animal a : this.animalsList) {
            Vector2d oldPos = a.getPosition();
            a.move(this.moveEnergy);
            Vector2d newPos = a.getPosition();
            for (IPositionChangeObserver observer : this.observers) {
                observer.positionChanged(a, oldPos, newPos);
            }
        }
    }

    public void eatGrass(Animal animal) {
        if (this.grassFields.get(animal.getPosition()) != null) {
            this.grassFields.remove(animal.getPosition());
            this.numOfGrassFields--;
            animal.addEnergy(this.plantEnergy);
        }
    }

    public void addGrassFields() {
        if (this.numOfGrassFields < this.width * this.height - this.animalsList.size()) {
            int grassFieldsAdded = 0;
            while (grassFieldsAdded < 1) {  // TODO: change to < 2 and adjust the condition b/c it won't work that way
                int x = ThreadLocalRandom.current().nextInt(0, this.width);
                int y = ThreadLocalRandom.current().nextInt(0, this.height);
                Vector2d pos = new Vector2d(x, y);
                if (!(isOccupied(pos))) {
                    Grass grass = new Grass(new Vector2d(x, y));
                    this.grassFields.put(grass.getPosition(), grass);
                    grassFieldsAdded++;
                }
            }
            this.numOfGrassFields += grassFieldsAdded;
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
            this.animals.get(oldPosition).remove(animal);
            List<Animal> newSamePosAnimals = this.animals.get(newPosition);
            if (newSamePosAnimals == null) newSamePosAnimals = new ArrayList<>();
            newSamePosAnimals.add(animal);
            newSamePosAnimals.sort(Comparator.comparing(Animal::getEnergy).reversed());
            this.animals.put(newPosition, newSamePosAnimals);
    }
}
