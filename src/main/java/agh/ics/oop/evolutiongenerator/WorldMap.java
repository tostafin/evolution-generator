package agh.ics.oop.evolutiongenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Map<Vector2d, List<Animal>> animals;
    private final List<Animal> animalsList;
    private final List<Animal> deadAnimalsList;
    private final Map<Vector2d, Grass> grassFields;
    private final List<IPositionChangeObserver> observers;
    private final int moveEnergy;

    public WorldMap(int width, int height, int moveEnergy) {
        int width1 = width - 1;
        int height1 = height - 1;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width1, height1);
        this.animals = new LinkedHashMap<>();
        this.animalsList = new ArrayList<>();
        this.deadAnimalsList = new ArrayList<>();
        this.grassFields = new LinkedHashMap<>();
        this.observers = new LinkedList<>();
        this.moveEnergy = moveEnergy;
        while (this.grassFields.size() < 2) {
            int x = ThreadLocalRandom.current().nextInt(0, width);
            int y = ThreadLocalRandom.current().nextInt(0, height);
            Vector2d pos = new Vector2d(x, y);
            if (!(this.grassFields.containsKey(pos))) {
                Grass grass = new Grass(new Vector2d(x, y));
                this.grassFields.put(grass.getPosition(), grass);
            }
        }
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
            return this.animals.get(position).get(0);
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
