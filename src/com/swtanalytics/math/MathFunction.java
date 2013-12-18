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
    
    /**
     * Generate a function which fits the specified set of points.
     * @param fit_points
     */
    public MathFunction( Vector<Fraction> fit_points ) {
        if ((fit_points.size() < 2) || ((fit_points.size() % 2) != 0)) {
            throw new IllegalArgumentException( "The fit points must be given as a non-empty" +
            		"sequence of fractions, of the form x_1 y_1 [x_2 y_2 ...]" );
        }
        
        // Use Neville's formula to generate the function.  Note that the Wikipedia article for
        // Neville's function (as of this writing) uses an example in which the value of 'x' is
        // known at the time Neville's algorithm executes.  Our task is slightly more complicated
        // because we need to leave 'x' as a free variable.
        
        MathFunction[] currentFuncs              = new MathFunction[ fit_points.size() / 2 ];
        Integer[]      currentFuncsLowIdx        = new Integer     [ fit_points.size() / 2 ];
        Integer[]      currentFuncsHighIdx       = new Integer     [ fit_points.size() / 2 ];
        Fraction[]     original_x_positions      = new Fraction    [ fit_points.size() / 2 ];
        
        for (int i = 0; i < currentFuncs.length; ++i ) {
        	Fraction this_x = fit_points.elementAt(  2*i    );
        	Fraction this_y = fit_points.elementAt( (2*i)+1 );

        	// Through all rounds of Neville's function, we'll need to remember the original
        	// X position associated with each index.
        	original_x_positions[i] = this_x;
        	
        	// For the first round of Neville's algorithm, the functions are simply of the form 
        	// f(x) = y_i = (this_y)*x^(0)
        	MathFunction  f = new MathFunction();
        	f.addTerm( new Term( this_y, 0 ));
        	currentFuncs[i] = f;
        	
        	currentFuncsLowIdx[i] = i;
        	currentFuncsHighIdx[i] = i;
        }
        
        while (currentFuncs.length > 1) {
        	MathFunction[] newFuncs        = new MathFunction[currentFuncs.length - 1];
            Integer[]      newFuncsLowIdx  = new Integer     [currentFuncs.length - 1];
            Integer[]      newFuncsHighIdx = new Integer     [currentFuncs.length - 1];
        	
        	for (int newFuncIdx = 0; newFuncIdx < newFuncs.length; ++newFuncIdx) {
        		// Combine two current functions...
        		int i = currentFuncsLowIdx [newFuncIdx  ];
        		int j = currentFuncsHighIdx[newFuncIdx+1]; 
        		
        		MathFunction f1 = currentFuncs[newFuncIdx  ]; // p_{i, (j-1)}(x)
        		MathFunction f2 = currentFuncs[newFuncIdx+1]; // p_{(i+1), j}(x)
        		
        		Fraction x_i = original_x_positions[ i ];
        		Fraction x_j = original_x_positions[ j ];

        		// fA(x) = (x_j - x) = (x_j)x^(0) + (-1)x^(1)
        		MathFunction fA = new MathFunction();
        		fA.addTerm(new Term( x_j, 0 ));
        		fA.addTerm(new Term( -1 , 1 ));

        		// fB(x) = (x - x_i) = (1)x^(1) + (-1 * x_i)x^(0)
        		MathFunction fB = new MathFunction();
        		fB.addTerm(new Term( 1           , 1 ));
        		fB.addTerm(new Term( x_i.negate(), 0 ));
        		
        		// divisor = x_j - x_i
        		// Model it as the function (x_j - x_i)x^(0), because then we can just use the
        		// existing method MathFunction.multiply(...)
        		MathFunction divisor_as_coeff = new MathFunction();
        		Fraction divisor = x_j.add( x_i.negate() );
        		divisor_as_coeff.addTerm(new Term( divisor.invert(), 0));
        		
        		// Assemble the final version of p_i_j
        		MathFunction fA1 = fA.multiply(f1);
        		MathFunction fB2 = fB.multiply(f2);
        		MathFunction fA1_plus_fB2 = fA1.add( fB2 ); // TODO: MathFunction.add()
        		
        		MathFunction p_i_j = divisor_as_coeff.multiply( fA1_plus_fB2 );
        		
        		// Install the new function...
        		newFuncs       [ newFuncIdx ] = p_i_j;
        		newFuncsLowIdx [ newFuncIdx ] = i;
        		newFuncsHighIdx[ newFuncIdx ] = j;
        	} // end for
        	
        	currentFuncs        = newFuncs;
        	currentFuncsLowIdx  = newFuncsLowIdx;
        	currentFuncsHighIdx = newFuncsHighIdx;
        } // end while
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MathFunction)) return false;

        MathFunction otherFunc = (MathFunction) o;

        Set<Fraction> thisExponents  = this.termsByExponent.keySet();
        Set<Fraction> otherExponents = otherFunc.termsByExponent.keySet();
        
        if (! thisExponents.equals(otherExponents)) {
        	return false;
        }

        // Note: The following loop would discover if there's an entry in this.termsByExponent 
        // that's absent from otherFunc.  So it might be possible to speed up the set equality test
        // above with an asymmetric subset check.
        
    	for ( Map.Entry<Fraction, Term> thisEntry : this.termsByExponent.entrySet() ) {
    		Fraction expo = thisEntry.getKey();
    		Term thisTerm = thisEntry.getValue();
    		Term otherTerm = otherFunc.termsByExponent.get( expo );
    		
    		if (! thisTerm.equals(otherTerm)) {
    			return false;
    		}
    	}
        
        return true;
    }
    
    public MathFunction multiply( MathFunction otherFunc ) {
    	MathFunction prodFunc = new MathFunction();

    	for ( Term thisTerm : this.termsByExponent.values() ) {
        	for ( Term otherFuncTerm : otherFunc.termsByExponent.values() ) {
        		Term prodTerm = thisTerm.multiply( otherFuncTerm );
        		prodFunc.addTerm( prodTerm );
        	}
    	}
    	
    	return prodFunc;
    }
    
    @SuppressWarnings("unchecked")
	public MathFunction clone() {
    	MathFunction c = new MathFunction();
    	
    	c.termsByExponent = (TreeMap<Fraction, Term>) this.termsByExponent.clone();
    	c.cachedDerivative = (this.cachedDerivative == null) ? null : this.cachedDerivative.clone();
    	c.cachedIntegral   = (this.cachedIntegral   == null) ? null : this.cachedIntegral.clone();
    	
    	return c;
    }
    
    public MathFunction add( MathFunction otherFunc ) {
    	MathFunction sum = this.clone();
    	
    	for ( Term otherFuncTerm : otherFunc.termsByExponent.values() ) {
    		sum.addTerm( otherFuncTerm );
    	}
    	
    	return sum;
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