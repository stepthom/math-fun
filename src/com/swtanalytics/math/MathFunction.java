package com.swtanalytics.math;

import java.util.*;

public class MathFunction {
    // TODO: These constants belong in Fraction
    private static final Fraction zero = new Fraction(0, 1);
    private static final Fraction one = new Fraction(1, 1);

    private static final double NEWTON_Y_EPSILON = 1.0E-12;
    private static final double NEWTON_X_EPSILON = 1.0E-12;

    private TreeMap<Fraction, Term> termsByExponent = new TreeMap<Fraction, Term>(new ReverseFractionComparator());

    public MathFunction() {
    }

    public void addTerm(Term t) {
        Term newTerm = termsByExponent.containsKey(t.exponent)
                ? termsByExponent.get(t.exponent).add(t)
                : t;
        termsByExponent.put(newTerm.exponent, newTerm);
    }

    public Collection<Term> getTerms() {
        return termsByExponent.values();
    }

    private Fraction getCoefficient(Fraction exponent) {
        Term term = termsByExponent.get(exponent);
        return term == null ? zero : term.coefficient;
    }

    public String toString() {
        String result = "f(x) = ";

        boolean first_term = true;
        for (Term t : termsByExponent.values()) {
            result += (t.prettyPrint(first_term) + " ");
            first_term = false;
        }

        return result;
    }

    public MathFunction differentiate() {
        MathFunction df = new MathFunction();
        for (Term t : termsByExponent.values()) {
            // XXX This will make uncollapsed x^0 and x^1 terms in the
            //     Style of the original class.
            Fraction c;
            Fraction e;
            if (t.exponent.numerator == 0) {
                // Leave the zero terms in case its the only one.
                c = new Fraction(0, 1);
                e = new Fraction(0, 1);
            } else {
                c = t.coefficient.multiply(t.exponent);
                e = t.exponent.subtract(new Fraction(1, 1));
            }
            Term dt = new Term(c, e);
            df.addTerm(dt);
        }

        return df;
    }

    public MathFunction integrate() {
        MathFunction integral = new MathFunction();

        for (Term t : termsByExponent.values()) {
            Fraction exp = new Fraction(t.exponent.numerator + t.exponent.denominator, t.exponent.denominator);
            Fraction coef = t.coefficient.multiply(new Fraction(exp.denominator, exp.numerator));

            Term integralTerm = new Term(coef, exp);
            integral.addTerm(integralTerm);
        }

        return integral;
    }

    public double evaluate(double value) {
        if (termsByExponent.size() == 0) {
            return 0;
        }

        if (value < 0 && hasFractionalExponent()) {
            // Result is complex
            return Double.NaN;
        }

        // Allow evaluation of limits at infinity
        if (Double.isInfinite(value)) {
            // Logic common to both positive and negative infinity
            Term dominantTerm = termsByExponent.firstEntry().getValue();
            assert !dominantTerm.coefficient.equals(zero);
            int exponentSign = dominantTerm.exponent.compareTo(zero);
            if (exponentSign < 0) {
                return 0;
            } else if (exponentSign == 0) {
                return dominantTerm.coefficient.doubleValue();
            }

            int coefficientSign = dominantTerm.coefficient.compareTo(zero);

            if (0 < value) {
                // Limit at positive infinity
                return coefficientSign < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            } else {
                // Limit at negative infinity
                assert !hasFractionalExponent(); // Checked this case above
                if ((dominantTerm.exponent.wholePart() % 2 != 0)) {
                    coefficientSign = -coefficientSign;
                }
                return coefficientSign < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
        }

        double returnValue = 0d;

        for (Term term : termsByExponent.values()) {
            returnValue += term.evaluate(value);
        }

        return returnValue;
    }

    public Fraction degree() {
        return termsByExponent.isEmpty() ? new Fraction(0, 0) : termsByExponent.firstKey();
    }

    public boolean isLinearFunction() {
        if (2 < termsByExponent.size()) {
            return false;
        }
        for (Fraction exponent : termsByExponent.keySet()) {
            if (!exponent.equals(zero) && !exponent.equals(one)) {
                return false;
            }
        }

        return true;
    }

    public boolean isConstant() {
        return (termsByExponent.size() <= 1) && degree().equals(zero);
    }

    public boolean hasNegativeExponent() {
        return (0 < termsByExponent.size()) && (termsByExponent.lastKey().compareTo(zero) < 0);
    }

    public boolean hasFractionalExponent() {
        for (Fraction exponent : termsByExponent.keySet()) {
            if (!exponent.isWhole()) {
                return true;
            }
        }
        return false;
    }

    public Fraction computeSlope() {
        if (!isLinearFunction()) {
            throw new IllegalArgumentException("Slope cannot be computed for non-linear functions.");
        }

        if (termsByExponent.containsKey(one)) {
            return termsByExponent.get(one).coefficient;
        }

        return zero;
    }

    public List<Double> solve() {
        // We *could* special-case f(x) = 0, which has infinite solutions, but I'm not sure what we would return in that
        // case, so let's treat it as any other constant function in the check below

        if (isConstant()) {
            // No solutions
            return Collections.<Double>emptyList();
        }

        if (isLinearFunction()) {
            // Closed-form solution to 0 = ax+b : x = -b/a
            double a = getCoefficient(one).doubleValue();
            double b = getCoefficient(zero).doubleValue();
            return Collections.singletonList(-b / a);
        }

        // TODO - we could check for and implement the closed-form solution for a simple quadratic equation here

        // General case: divide the domain into spans between inflection points. Any such span should have at most one
        // interior solution; use Newton's method to find it if it exists.

        MathFunction derivative = differentiate();
        List<Double> solutions = new ArrayList<Double>();

        double domainBegin;
        double rangeBegin;
        double domainEnd = Double.NEGATIVE_INFINITY;
        double rangeEnd = evaluate(domainEnd);

        for (double newDomainEnd : getInflections(derivative)) {
            domainBegin = domainEnd;
            rangeBegin = rangeEnd;
            domainEnd = newDomainEnd;
            rangeEnd = evaluate(domainEnd);

            if (rangeBegin == 0) {
                solutions.add(domainBegin);
            }
            double solution = interiorSolution(domainBegin, domainEnd, derivative);
            if (!Double.isNaN(solution)) {
                solutions.add(solution);
            }
        }

        domainBegin = domainEnd;
        rangeBegin = rangeEnd;
        domainEnd = Double.POSITIVE_INFINITY;
        rangeEnd = evaluate(domainEnd);

        if (rangeBegin == 0) {
            solutions.add(domainBegin);
        }
        double solution = interiorSolution(domainBegin, domainEnd, derivative);
        if (!Double.isNaN(solution)) {
            solutions.add(solution);
        }
        if (rangeEnd == 0) {
            solutions.add(domainEnd);
        }

        return solutions;
    }

    private double interiorSolution(double domainBegin, double domainEnd, MathFunction derivative) {
        double rangeBegin = evaluate(domainBegin);
        double rangeEnd = evaluate(domainEnd);
        if (0 <= rangeBegin && 0 <= rangeEnd) {
            return Double.NaN;
        }
        if (rangeBegin <= 0 && rangeEnd <= 0) {
            return Double.NaN;
        }

        double x;
        if (Double.isInfinite(domainBegin)) {
            if (Double.isInfinite(domainEnd)) {
                x = 0;
            } else {
                x = domainEnd - 1;
            }
        } else {
            if (Double.isInfinite(domainEnd)) {
                x = domainBegin + 1;
            } else {
                x = (domainBegin + domainEnd) / 2;
            }
        }

        while (true)
        {
            double y = evaluate(x);
            if (Math.abs(y) < NEWTON_Y_EPSILON) { return x; }

            double newX = x - y / derivative.evaluate(x);
            if (Math.abs(x - newX) < NEWTON_X_EPSILON) { return x; }

            x = newX;

            // TODO: Check for cycles or diversion, and fall back to something else (binary search, or something
            // fancier.) This shouldn't be an issue with simple polynomials.
        }
    }

    private List<Double> getInflections(MathFunction derivative) {
        if (isLinearFunction()) {
            return Collections.<Double>emptyList();
        }
        if (hasNegativeExponent()) {
            throw new UnsupportedOperationException(); // an exercise for the reader ;-)
        }
        if (hasFractionalExponent()) {
            throw new UnsupportedOperationException(); // an exercise for the reader ;-)
        }
        return derivative.solve();
    }
}