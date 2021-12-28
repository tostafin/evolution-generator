package agh.ics.oop.evolutiongenerator;

import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable {
    private final WorldMap mapInstance;
    private int moveDelay;
    private int numOfAnimals;

    public SimulationEngine(WorldMap mapInstance, int numOfAnimals, int width, int height, int startEnergy,
                            int jungleRatio) {
        this.mapInstance = mapInstance;
        this.moveDelay = 0;
        this.numOfAnimals = numOfAnimals;
        for (int i = 0; i < numOfAnimals; i++) {
            Vector2d animalsPos = new Vector2d(
                    ThreadLocalRandom.current().nextInt(0, width),
                    ThreadLocalRandom.current().nextInt(0, height));
            Animal animal = new Animal(this.mapInstance, animalsPos, startEnergy);
            this.mapInstance.place(animal);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.mapInstance.addObserver(observer);
    }

    public void setMoveDelay(int newMoveDelay) {
        this.moveDelay = newMoveDelay;
    }

    @Override
    public void run() {
        while (this.numOfAnimals > 0) {
            this.mapInstance.removeDeadAnimals();
            this.mapInstance.moveAnimals();
            this.mapInstance.addGrassFields();
            this.numOfAnimals = this.mapInstance.getAnimalsList().size();
            System.out.println(numOfAnimals);

            try {
                System.out.println("Thread started.");
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                System.out.println("Simulation disrupted by an exception: " + e);
            }
        }
    }
}