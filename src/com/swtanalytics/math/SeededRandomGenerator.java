package com.swtanalytics.math;

import java.util.Random;

public class SeededRandomGenerator implements RandomGenerator {
    private Random random;

    public SeededRandomGenerator(long seed)
    {
        random = new Random(seed);
    }

    @Override
    public int generateInt(int minInclusive, int maxInclusive) {
        return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
    }

    @Override
    public double generateDouble() {
        return random.nextDouble();
    }
}
