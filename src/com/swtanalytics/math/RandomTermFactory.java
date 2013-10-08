package com.swtanalytics.math;

public class RandomTermFactory implements TermFactory {
    private static final double WHOLE_PROBABILITY = .75;
    private FractionFactory fractionGenerator;

    public RandomTermFactory(FractionFactory fractionGenerator) {
        this.fractionGenerator = fractionGenerator;
    }

    @Override
    public Term create(boolean forceWholeNumberCoefficients, int maxExponent) {
        Fraction coefficient = fractionGenerator.createCoefficient(forceWholeNumberCoefficients, WHOLE_PROBABILITY);
        Fraction exponent = fractionGenerator.createExponent(maxExponent);

        return new Term(coefficient, exponent);
    }
}
