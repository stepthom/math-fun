package com.swtanalytics.math;

public interface FractionFactory {
    public Fraction createCoefficient(boolean forceWholeNumber, double wholeProbability);
    public Fraction createExponent(int max);
}
