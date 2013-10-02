package com.swtanalytics.math;

public class Fraction {
    protected int numerator;
    protected int denominator;


    public Fraction(int n, int d) {
        this.numerator = n;
        if (d == 0) {
            throw new IllegalArgumentException("Argument 'd' is 0");
        }
        this.denominator = d;
    }

    // This was a complete duplicate of formatString minus the stripPositive functionality
    // Refactored it to reuse formatString and assume no stripping
    public String toString() {
    	// Calling this will not strip the sign from a positive numerator
    	return formatString(false);
    }

    public String formatString(boolean stripPositive) {
        int n = this.numerator;
        int d = Math.abs(this.denominator);
        if (this.denominator < 0) {
            n *= -1;
        }

        String fmt;
        if (stripPositive) {
            fmt = "%d";
        } else {
            fmt = "%+d";
        }
        String result = String.format(fmt, n);

        // Collapse into integer
        if (d != 1) {
            result = String.format("%s/%d", result, d);
        }

        return result;
    }

    public double doubleValue() {
        return new Double(this.numerator) / this.denominator;
    }

    public int compareTo(Fraction f) {
        Double result = new Double(f.doubleValue() - this.doubleValue());
        return result.intValue();
    }

    public Fraction subtract(Fraction f) {
        int n = this.numerator*f.denominator - f.numerator*this.denominator;
        int d = this.numerator*f.denominator;
        return new Fraction(n, d);
    }

    public Fraction multiply(Fraction f) {
        return new Fraction(this.numerator*f.numerator,
                            this.denominator*f.denominator);
    }
}

