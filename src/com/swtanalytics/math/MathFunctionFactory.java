package com.swtanalytics.math;

public class MathFunctionFactory {
    private TermFactory termFactory;
    private RandomGenerator randomGenerator;

    public MathFunctionFactory(TermFactory termFactory, RandomGenerator randomGenerator) {
        this.termFactory = termFactory;
        this.randomGenerator = randomGenerator;
    }

    public MathFunction create(boolean forceWholeNumberCoefficients, boolean forceLinearFunction) {
        MathFunction result = new MathFunction();

        int numTerms = randomGenerator.generateInt(1, 5);
        int maxExponent = forceLinearFunction
                                ? 1
                                : 100;
        for (int j = 0; j < numTerms; ++j) {
            Term t = termFactory.create(forceWholeNumberCoefficients, maxExponent);
            result.addTerm(t);
        }

        return result;
    }
}