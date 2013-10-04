package com.swtanalytics.math;

public class Term implements Comparable<Term> {

	protected Fraction coefficient;
	protected Fraction exponent;
		
	public Term (Fraction c, Fraction e) {
		this.coefficient = c;
		this.exponent = e;
	}
    public String prettyPrint(boolean isFirstTerm) {
        return this.formatString(isFirstTerm);
    }

    protected String formatString(boolean isFirstTerm) {
        if (this.coefficient.numerator == 0) {
            return "";
        }

        String coefficientPart = formatCoefficientPart(isFirstTerm);
        String variablePart = formatVariablePart();

        return coefficientPart + variablePart;
    }

    private String formatVariablePart() {
        String variablePart;
        if (this.exponent.numerator == 0) {
            variablePart = "";
        }
        else if (this.exponent.numerator == this.exponent.denominator) {
            variablePart = "x";
        }
        else {
            variablePart = "x^" + this.exponent.formatString(true, false);
        }

        return variablePart;
    }

    private String formatCoefficientPart(boolean isFirstTerm) {
        return this.coefficient.numerator == this.coefficient.denominator
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

    public double evaluate(double value) {
    	double returnValue = 0d;
    	
    	returnValue = Math.pow(value, exponent.doubleValue()) * coefficient.doubleValue();

    	return returnValue;
    }

    public Term add(Term toAdd) {
        if (!exponent.equals(toAdd.exponent)) {
            throw new IllegalArgumentException("Exponents must match in order to add terms.");
        }

        return new Term(coefficient.add(toAdd.coefficient), toAdd.exponent);
    }
}