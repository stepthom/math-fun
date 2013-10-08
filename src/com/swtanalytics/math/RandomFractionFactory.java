package com.swtanalytics.math;

public class RandomFractionFactory implements FractionFactory {
    private RandomGenerator randomGenerator;

    public RandomFractionFactory(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public Fraction createCoefficient(boolean forceWholeNumber, double wholeProbability) {
        int n = randomGenerator.generateInt(-50, 49);
        int d = 1;

        if (!forceWholeNumber) {
            if (randomGenerator.generateDouble() > wholeProbability) {
                d = randomGenerator.generateInt(-50, 49);
                if (d == 0) {
                    d = 1;
                }
            }
        }

        return new Fraction(n, d);
    }

    public Fraction createExponent(int max) {
        int n = randomGenerator.generateInt(0, max);
        return new Fraction(n, 1);
    }
}
