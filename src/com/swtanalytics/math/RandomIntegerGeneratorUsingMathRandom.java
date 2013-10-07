package com.swtanalytics.math;

public class RandomIntegerGeneratorUsingMathRandom implements RandomGenerator {
    @Override
    public int generateInt(int minInclusive, int maxInclusive) {
        int width = maxInclusive - minInclusive + 1;
        return (int)(Math.random() * width) + minInclusive;
    }

    @Override
    public double generateDouble() {
        return Math.random();
    }
}
