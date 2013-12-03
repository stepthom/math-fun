package com.swtanalytics.math;

import java.util.*;

public class MathFunction {
    // TODO: These constants belong in Fraction
    private static final Fraction zero = new Fraction(0, 1);
    private static final Fraction one = new Fraction(1, 1);

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
        return (termsByExponent.size() <= 1) && degree() == zero;
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
}