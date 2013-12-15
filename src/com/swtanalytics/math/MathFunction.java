package com.swtanalytics.math;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;

public class MathFunction {
    // TODO: These constants belong in Fraction
    private static final Fraction zero = new Fraction(0, 1);
    private static final Fraction one = new Fraction(1, 1);

    private static final double NEWTON_X_EPSILON = 1.0E-12;

    private TreeMap<Fraction, Term> termsByExponent = new TreeMap<Fraction, Term>(new ReverseFractionComparator());
    private MathFunction cachedDerivative;
    private MathFunction cachedIntegral;

    public MathFunction() {
    }

    public void addTerm(Term t) {
        Term newTerm = termsByExponent.containsKey(t.exponent)
                ? termsByExponent.get(t.exponent).add(t)
                : t;
        if (newTerm.coefficient.compareTo(zero) == 0) {
            termsByExponent.remove(newTerm.exponent);
        } else {
            termsByExponent.put(newTerm.exponent, newTerm);
        }
        cachedDerivative = null;
        cachedIntegral = null;
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

        if (termsByExponent.isEmpty()) {
            result += "0";
        } else {
            boolean first_term = true;
            for (Term t : termsByExponent.values()) {
                result += (t.prettyPrint(first_term) + " ");
                first_term = false;
            }
        }

        return result;
    }

    public MathFunction differentiate() {
        if (cachedDerivative == null) {
            cachedDerivative = new MathFunction();
            for (Term t : termsByExponent.values()) {
                // XXX This will make uncollapsed x^0 and x^1 terms in the
                //     Style of the original class.
                if (t.exponent.sign() != 0) {
                    Term dt = new Term(t.coefficient.multiply(t.exponent), t.exponent.subtract(new Fraction(1, 1)));
                    cachedDerivative.addTerm(dt);
                }
            }
        }

        return cachedDerivative;
    }

    public MathFunction integrate() {
        if (cachedIntegral == null) {
            cachedIntegral = new MathFunction();

            for (Term t : termsByExponent.values()) {
                Fraction exp = new Fraction(
                		t.exponent.numerator.add(t.exponent.denominator), 
                		t.exponent.denominator);
                
                Fraction coef = t.coefficient.multiply(new Fraction(exp.denominator, exp.numerator));

                Term integralTerm = new Term(coef, exp);
                cachedIntegral.addTerm(integralTerm);
            }
        }

        return cachedIntegral;
    }

    public double evaluate(double value, MathContext mc) {
        if (termsByExponent.isEmpty()) {
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
                return dominantTerm.coefficient.bigDecimalValue(mc).doubleValue();
            }

            int coefficientSign = dominantTerm.coefficient.compareTo(zero);

            if (0 < value) {
                // Limit at positive infinity
                return coefficientSign < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            } else {
                // Limit at negative infinity
                assert !hasFractionalExponent(); // Checked this case above
                if ((dominantTerm.exponent.wholePart().mod( BigInteger.valueOf(2) ) != BigInteger.valueOf(0))) {
                    coefficientSign = -coefficientSign;
                }
                return coefficientSign < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
        }

        double returnValue = 0d;

        for (Term term : termsByExponent.values()) {
            returnValue += term.evaluate(value, mc);
        }

        return returnValue;
    }

    public Fraction degree() {
        return termsByExponent.isEmpty() ? new Fraction(0) : termsByExponent.firstKey();
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
        return (!termsByExponent.isEmpty()) && (termsByExponent.lastKey().compareTo(zero) < 0);
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

    public List<Double> solve(MathContext mc) {
        // We *could* special-case f(x) = 0, which has infinite solutions, but I'm not sure what we would return in that
        // case, so let's treat it as any other constant function in the check below

        if (isConstant()) {
            // No solutions
            return Collections.emptyList();
        }

        if (isLinearFunction()) {
            // Closed-form solution to 0 = ax+b : x = -b/a
            double a = getCoefficient(one).bigDecimalValue(mc).doubleValue();
            double b = getCoefficient(zero).bigDecimalValue(mc).doubleValue();
            return Collections.singletonList(-b / a);
        }

        if (0 < termsByExponent.lastKey().compareTo(zero)) {
            // If all terms are of degree > 0, then 0 is a solution, and the other solutions can be found by dividing
            // this function by its least significant term, and solving

            Fraction leastDegree = termsByExponent.lastKey();
            MathFunction simplified = new MathFunction();
            for (Term term : termsByExponent.values()) {
                simplified.addTerm(new Term(term.coefficient, term.exponent.subtract(leastDegree)));
            }

            List<Double> solutions = simplified.solve( mc );
            if (!solutions.contains(0.0)) {
                solutions = new ArrayList<Double>(solutions); // solutions could be read-only
                // TODO: Find & insert instead of sort
                solutions.add(0.0);
                Collections.sort(solutions);
            }
            return solutions;
        }

        // TODO - we could check for and implement the closed-form solution for a simple quadratic equation here

        // General case: divide the domain into spans between critical points. Any such span should have at most one
        // interior solution; use Newton's method to find it if it exists.

        List<Double> solutions = new ArrayList<Double>();

        double domainBegin;
        double rangeBegin;
        double domainEnd = Double.NEGATIVE_INFINITY;
        double rangeEnd = evaluate(domainEnd, mc);

        for (double newDomainEnd : getCriticalPoints(mc)) {
            domainBegin = domainEnd;
            rangeBegin = rangeEnd;
            domainEnd = newDomainEnd;
            rangeEnd = evaluate(domainEnd, mc);

            if (rangeBegin == 0) {
                solutions.add(domainBegin);
            }
            double solution = interiorSolution(domainBegin, domainEnd, mc);
            if (!Double.isNaN(solution)) {
                solutions.add(solution);
            }
        }

        domainBegin = domainEnd;
        rangeBegin = rangeEnd;
        domainEnd = Double.POSITIVE_INFINITY;
        rangeEnd = evaluate(domainEnd, mc);

        if (rangeBegin == 0) {
            solutions.add(domainBegin);
        }
        double solution = interiorSolution(domainBegin, domainEnd, mc);
        if (!Double.isNaN(solution)) {
            solutions.add(solution);
        }
        if (rangeEnd == 0) {
            solutions.add(domainEnd);
        }

        return solutions;
    }

    private double interiorSolution(double domainBegin, double domainEnd, MathContext mc) {
        int rangeBeginSign = Double.compare(evaluate(domainBegin, mc), 0);
        int rangeEndSign = Double.compare(evaluate(domainEnd, mc), 0);
        if (rangeBeginSign == 0 || rangeEndSign == 0 || rangeBeginSign == rangeEndSign) {
            return Double.NaN;
        }

        double x = getDivisionPoint(domainBegin, domainEnd);

        MathFunction derivative = differentiate();

        while (true) {
            if (Math.abs(domainEnd - domainBegin) < NEWTON_X_EPSILON) {
                return x;
            }

            double y = evaluate(x, mc);
            if (y == 0) {
                return x;
            }
            int ySign = Double.compare(y, 0);
            assert ySign != 0;
            assert rangeBeginSign != 0;
            assert rangeEndSign != 0;
            assert rangeBeginSign != rangeEndSign;
            assert (ySign == rangeBeginSign) ^ (ySign == rangeEndSign);

            if (ySign == rangeBeginSign) {
                domainBegin = x;
                rangeBeginSign = ySign;
            } else {
                domainEnd = x;
                rangeEndSign = ySign;
            }

            double newX = x - y / derivative.evaluate(x, mc);
            if (Math.abs(x - newX) < NEWTON_X_EPSILON) {
                return x;
            }

            if (Double.isNaN(newX) || newX <= domainBegin || domainEnd <= newX) {
                // Either we've had some rounding error, we're diverging, we're looping, or our walk took us out of the search range.
                // In any case, let's fall back to a search-by-division on this step.
                if (ySign != rangeBeginSign) {
                    newX = getDivisionPoint(domainBegin, x);
                } else {
                    newX = getDivisionPoint(x, domainEnd);
                }
            }

            x = newX;
        }
    }

    private double getDivisionPoint(double begin, double end) {
        double x;
        if (Double.isInfinite(begin)) {
            if (Double.isInfinite(end)) {
                x = 0;
            } else {
                x = end - 1;
            }
        } else {
            if (Double.isInfinite(end)) {
                x = begin + 1;
            } else {
                x = (begin + end) / 2;
            }
        }
        return x;
    }

    private List<Double> getCriticalPoints( MathContext mc ) {
        if (isLinearFunction()) {
            return Collections.emptyList();
        }
        if (hasNegativeExponent()) {
            throw new UnsupportedOperationException(); // an exercise for the reader ;-)
        }
        if (hasFractionalExponent()) {
            throw new UnsupportedOperationException(); // an exercise for the reader ;-)
        }

        return differentiate().solve( mc );
    }

    public double findMaximum(double domainMin, double domainMax, MathContext mc) {
        double xMax = domainMin;
        double yMax = evaluate(xMax, mc);
        for (double critical : getCriticalPoints( mc )) {
            if (critical <= domainMin) continue;
            if (domainMax <= critical) break;
            double y = evaluate(critical, mc);
            if (yMax < y) {
                xMax = critical;
                yMax = y;
            }
        }
        double y = evaluate(domainMax, mc);
        if (yMax < y) {
            xMax = domainMax;
        }
        return xMax;
    }

    public double findMinimum(double domainMin, double domainMax, MathContext mc) {
        double xMin = domainMin;
        double yMin = evaluate(xMin, mc);
        for (double critical : getCriticalPoints(mc)) {
            if (critical <= domainMin) continue;
            if (domainMax <= critical) break;
            double y = evaluate(critical, mc);
            if (y < yMin) {
                xMin = critical;
                yMin = y;
            }
        }
        double y = evaluate(domainMax, mc);
        if (y < yMin) {
            xMin = domainMax;
        }
        return xMin;
    }
}