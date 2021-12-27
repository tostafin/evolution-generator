package agh.ics.oop.evolutiongenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable {
    private final Vector2d[] animalsPos;
    private final WorldMap mapInstance;
    private final List<IPositionChangeObserver> observers = new LinkedList<>();
    private int moveDelay;

    public SimulationEngine(WorldMap mapInstance, int numOfAnimals, int width, int height, int energy) {
        this.mapInstance = mapInstance;
        this.animalsPos = new Vector2d[numOfAnimals];
        this.moveDelay = 0;
        for (int i = 0; i < numOfAnimals; i++) {
            Vector2d animalsPos = new Vector2d(
                    ThreadLocalRandom.current().nextInt(0, width),
                    ThreadLocalRandom.current().nextInt(0, height));
            Animal animal = new Animal(this.mapInstance, animalsPos, energy);
            this.mapInstance.place(animal);
            this.animalsPos[i] = animalsPos;
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void positionChanged(Animal animal, Vector2d oldPos, Vector2d newPos) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(animal, oldPos, newPos);
        }
    }

    public void changeMoveDelay(int newMoveDelay) {
        this.moveDelay = newMoveDelay;
    }

    @Override
    public void run() {
        int movesNo = 100; // TODO: change to variable
        int animalsNo = this.animalsPos.length;
        for (int i = 0; i < movesNo; i++) {
            Object obj = this.mapInstance.objectAt(this.animalsPos[i % animalsNo]);
            if (obj instanceof Animal) {
                Animal animal = (Animal) obj;
                Vector2d oldPos = animal.getPosition();
                animal.move();
                Vector2d newPos = animal.getPosition();
                this.animalsPos[i % animalsNo] = newPos;
                this.positionChanged(animal, oldPos, newPos);
            }

            try {
                System.out.println("Thread started.");
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                System.out.println("Simulation disrupted by an exception: " + e);
            }
        }
    }
}