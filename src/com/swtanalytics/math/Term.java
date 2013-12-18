package com.swtanalytics.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Term implements Comparable<Term> {

    protected final Fraction coefficient;
    protected final Fraction exponent;

    public Term(Fraction c, Fraction e) {
        this.coefficient = c;
        this.exponent = e;
    }

    public Term(Fraction c, int e) {
        this.coefficient = c;
        this.exponent = new Fraction(e);
    }

    public Term(int c, Fraction e) {
        this.coefficient = new Fraction(c);
        this.exponent = e;
    }

    public Term(int c, int e) {
        this.coefficient = new Fraction(c);
        this.exponent = new Fraction(e);
    }
    
    public Term multiply( Term t ) {
    	Fraction coeff = this.coefficient.multiply( t.coefficient );
    	Fraction exp = this.exponent.add( t.exponent );
    	return new Term( coeff, exp );
    }

    public String prettyPrint(boolean isFirstTerm) {
        return this.formatString(isFirstTerm);
    }

    protected String formatString(boolean isFirstTerm) {
        if (this.coefficient.sign() == 0) {
            return "";
        }

        String coefficientPart = formatCoefficientPart(isFirstTerm);
        String variablePart = formatVariablePart();

        return coefficientPart + variablePart;
    }

    private String formatVariablePart() {
        String variablePart;
        if (this.exponent.sign() == 0) {
            variablePart = "";
        } else if (this.exponent.equals( new Fraction(1,1) )) {
            variablePart = "x";
        } else {
            variablePart = "x^" + this.exponent.formatString(true, false);
        }

        return variablePart;
    }

    private String formatCoefficientPart(boolean isFirstTerm) {
        return this.coefficient.equals( new Fraction(1,1) )
                ? ""
                : this.coefficient.formatString(isFirstTerm, !isFirstTerm);
    }

    public String toString() {
        // Calling this will not strip the sign from a positive coefficient
        return formatString(false);
    }

    public int compareTo(Term t) {
        return this.exponent.compareTo(t.exponent);
    }

    // NOTE: The Java SDK provides no pow(...) function for BigDecimal or BigInteger objects, which
    // are what the Fraction class uses to support unbounded-precision fractions.
    // Therefore, for practical purposes, we switch to using 64-bit double precision numbers here.
    // A future enhancement might be to implement pow(...) support to the Fraction class, but that's
    // beyond the scope of the current effort.
    public double evaluate(double value, MathContext mc) {
        double returnValue = Math.pow(value, exponent.bigDecimalValue(mc).doubleValue()) * 
        			  coefficient.bigDecimalValue(mc).doubleValue();

        return returnValue;
    }
    
    public Term add(Term toAdd) {
        if (!exponent.equals(toAdd.exponent)) {
            throw new IllegalArgumentException("Exponents must match in order to add terms.");
        }

        return new Term(coefficient.add(toAdd.coefficient), toAdd.exponent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Term)) return false;

        Term term = (Term) o;

        if (!coefficient.equals(term.coefficient)) return false;
        if (!exponent.equals(term.exponent)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = coefficient.hashCode();
        result = 31 * result + exponent.hashCode();
        return result;
    }
}