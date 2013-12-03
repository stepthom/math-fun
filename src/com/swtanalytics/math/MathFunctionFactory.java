package com.swtanalytics.math;

public class MathFunctionFactory {
    private TermFactory termFactory;
    private RandomGenerator randomGenerator;

    public MathFunctionFactory(TermFactory termFactory, RandomGenerator randomGenerator) {
        this.termFactory = termFactory;
        this.randomGenerator = randomGenerator;
    }

    public MathFunction create(boolean forceWholeNumberCoefficients, boolean forceLinearFunction) {
        return create(forceWholeNumberCoefficients, randomGenerator.generateInt(1, 5), forceLinearFunction ? 1 : 100, forceLinearFunction);
    }

    public MathFunction create(boolean forceWholeNumberCoefficients, int termCount, int maxDegree, boolean strictDegree) {
        MathFunction result = new MathFunction();

        Term firstTerm = getValidTerm(forceWholeNumberCoefficients, maxDegree);
        assert !firstTerm.coefficient.equals(new Fraction(0, 1));
        if (strictDegree)
        {
            firstTerm.exponent = new Fraction(maxDegree, 1);
        }
        result.addTerm(firstTerm);

        for (int j = 1; j < termCount; ++j) {
            // TODO: Should this check for duplicates, so that we can provide exactly the requested number of terms?
            Term t = termFactory.create(forceWholeNumberCoefficients, maxDegree);
            result.addTerm(t);
        }

        return result;
    }

    private Term getValidTerm(boolean forceWholeNumberCoefficients, int maxDegree) {
        // TODO: Rather than loop here, it would be better to re-define the TermFactory contract to require create() to return a non-zero coefficient.
        while (true)
        {
            Term term = termFactory.create(forceWholeNumberCoefficients, maxDegree);
            if (!term.coefficient.equals(new Fraction(0, 1)))
            {
                return term;
            }
        }
    }


}