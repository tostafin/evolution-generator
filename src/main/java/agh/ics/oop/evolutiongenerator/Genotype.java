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
            }
            else idx++;
        }
    }

    public int getAnimalsMove() {
        return this.genotype[ThreadLocalRandom.current().nextInt(0, 32)];
    }
}
