package com.swtanalytics.math;

import org.ejml.data.Complex64F;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class MathFunction {
    private static final Fraction zero = new Fraction(0, 1);
    private static final Fraction one = new Fraction(1, 1);

    private TreeMap<Fraction, Term> termsByExponent = new TreeMap<Fraction, Term>();

    public MathFunction() {}

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
    	double returnValue = 0d;
    	
    	for (Term term : termsByExponent.values())
    	{
    		returnValue += term.evaluate(value);
    	}

        return returnValue;
    }

    public double findMinimum(double minDomain, double maxDomain) {
        List<Double> candidates = getValuesToConsider(minDomain, maxDomain);

        double globalMin = Double.POSITIVE_INFINITY;

        for (Double candidate : candidates) {
            if (evaluate(candidate) < globalMin) {
                globalMin = evaluate(candidate);
            }
        }

        return globalMin;
    }

    private List<Double> getValuesToConsider(double minDomain, double maxDomain) {
        MathFunction derivative = differentiate();
        int maxExponent = 0;
        for (Term term : derivative.terms) {
            int exponentAsInt = (int) term.exponent.doubleValue();
            if (exponentAsInt > maxExponent) {
                maxExponent = exponentAsInt;
            }
        }

        double[] inputCoefficients = new double[maxExponent + 1];
        for (Term term : derivative.terms) {
            if ((int)term.coefficient.doubleValue() != 0)
                inputCoefficients[(int)term.exponent.doubleValue()] = term.coefficient.doubleValue();
        }

        List<Double> candidates;

        if (inputCoefficients.length > 1) {
            PolynomialRootFinder rootFinder = new PolynomialRootFinder();
            Complex64F[] roots = rootFinder.findRoots(inputCoefficients);
            candidates = getRealRootsInRange(roots, minDomain, maxDomain);
        }
        else {
            candidates = new ArrayList<Double>();
        }

        candidates.add(minDomain);
        candidates.add(maxDomain);

        return candidates;
    }

    public double findMaximum(double minDomain, double maxDomain) {
        List<Double> candidates = getValuesToConsider(minDomain, maxDomain);

        double globalMax = Double.NEGATIVE_INFINITY;

        for (Double candidate : candidates) {
            if (evaluate(candidate) > globalMax) {
                globalMax = evaluate(candidate);
            }
        }

        return globalMax;
    }

    private List<Double> getRealRootsInRange(Complex64F[] roots, double minDomain, double maxDomain) {
        List<Double> realRoots = new ArrayList<Double>();

        for (Complex64F root : roots) {
            if (root.isReal() && root.getReal() >= minDomain && root.getReal() <= maxDomain) {
                realRoots.add(root.getReal());
            }
        }

        return realRoots;
    }

    public boolean isLinearFunction() {
        for (Fraction term : termsByExponent.keySet()){
            if (!term.equals(zero) && !term.equals(one)) {
                return false;
            }
        }

        return true;
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