package agh.ics.oop.evolutiongenerator;

import java.util.concurrent.ThreadLocalRandom;

public class Genotype {
    private final int[] genotype;

    public Genotype() {
        int[] genotypeCnt = new int[8];
        for (int i = 0; i < 32; i++) genotypeCnt[ThreadLocalRandom.current().nextInt(0, 8)]++;
        this.genotype = new int[32];
        int idx = 0;
        for (int i = 0; i < 32; i++) {
            if (genotypeCnt[idx] > 0) {
                this.genotype[i] = idx;
                genotypeCnt[idx]--;
            } else idx++;
        }
    }

    public Genotype(Animal firstParent, Animal secondParent) {
        this.genotype = new int[32];
        int genotypeCutIdx = firstParent.getEnergy() / (firstParent.getEnergy() + secondParent.getEnergy()) * 32;
        String[] sides = {"left", "right"};
        String randomSide = sides[ThreadLocalRandom.current().nextInt(0, 2)];
        switch (randomSide) {
            case "left":
                for (int i = 0; i < genotypeCutIdx; i++) {
                    this.genotype[i] = firstParent.getGenotype().getGenotype()[i];
                }
                for (int i = genotypeCutIdx; i < 32; i++) {
                    this.genotype[i] = secondParent.getGenotype().getGenotype()[i];
                }

            case "right":
                for (int i = 0; i < 32 - genotypeCutIdx; i++) {
                    this.genotype[i] = secondParent.getGenotype().getGenotype()[i];
                }
                for (int i = 32 - genotypeCutIdx; i < 32; i++) {
                    this.genotype[i] = firstParent.getGenotype().getGenotype()[i];
                }
        }
    }

    public int[] getGenotype() {
        return this.genotype;
    }

    public int getAnimalsMove() {
        return this.genotype[ThreadLocalRandom.current().nextInt(0, 32)];
    }


}
