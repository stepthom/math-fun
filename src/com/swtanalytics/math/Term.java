package com.swtanalytics.math;

public class Term implements Comparable<Term> {
    protected Fraction coefficient;
    protected Fraction exponent;

    public Term (Fraction c, Fraction e) {
        this.coefficient = c;
        this.exponent = e;
    }

    public String prettyPrint(boolean isFirstTerm) {
        // If first term, strip the positive sign if present
        String result = this.formatString(isFirstTerm);
        if (!isFirstTerm) {
            result = result.substring(0,1) + ' ' + result.substring(1, result.length());
        }

        return result;
    }

    protected String formatString(boolean stripPositiveFromCoefficient) {
        if (this.coefficient.numerator == 0) {
            return "";
        }

        String coefficientPart = formatCoefficientPart(stripPositiveFromCoefficient);
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
            variablePart = "x^" + this.exponent.formatString(true);
        }

        return variablePart;
    }

    private String formatCoefficientPart(boolean stripPositiveFromCoefficient) {
        return this.coefficient.numerator == this.coefficient.denominator
                ? ""
                : this.coefficient.formatString(stripPositiveFromCoefficient);
    }

    public String toString() {
        // Calling this will not strip the sign from a positive coefficient
        return formatString(false);
    }

    public int compareTo(Term t) {
        return this.exponent.compareTo(t.exponent);
    }
}