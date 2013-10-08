package com.swtanalytics.math;

public class Fraction {
    protected int numerator;
    protected int denominator;


    public Fraction(int n, int d) {
        if (d == 0) {
            throw new IllegalArgumentException("Argument 'd' is 0");
        }

    // Short circuit gcf if we have a 0 numerator
    if (n != 0) {
    	int g = gcf(n, d);
    	this.numerator = n / g;
    	this.denominator = d / g;
    }
    else {
    	this.numerator = n;
    	this.denominator = d;
    }

    // Fix the sign of the numerator and denominator if the gcf is
	// negative. We may only need to check and fix the denominator
	// sign here...
	if (this.numerator > 0 && this.denominator < 0) {
	    this.numerator *= -1;
	    this.denominator *= -1;
	}
    }

    public String toString() {
        int n = this.numerator;
        int d = Math.abs(this.denominator);
        if (this.denominator < 0) {
            n *= -1;
        }

        String result = String.format("%+d", n);

        // Collapse into integer
        if (d != 1) {
            result = String.format("%s/%d", result, d);
        }

        return result;
    }

    public String formatString(boolean stripPositive, boolean spaceAfterCoefficientSign) {
        int n = this.numerator;
        int d = Math.abs(this.denominator);
        if (this.denominator < 0) {
            n *= -1;
        }

        boolean isPositive = this.numerator * this.denominator > 0;
        String sign = isPositive
                ? "+"
                : "-";

        String numeratorPart;
        if (isPositive && stripPositive) {
            numeratorPart = String.format("%d", Math.abs(n));
        } else if (spaceAfterCoefficientSign) {
            numeratorPart = String.format("%s %d", sign, Math.abs(n));
        } else {
            numeratorPart = String.format("%s%d", sign, Math.abs(n));
        }

        // Collapse into integer
        if (d != 1) {
            return String.format("%s/%d", numeratorPart, d);
        }

        return numeratorPart;
    }

    public double doubleValue() {
        return (double) this.numerator / this.denominator;
    }

    public int compareTo(Fraction f) {
        Double result = f.doubleValue() - this.doubleValue();
        return result.intValue();
    }

    public Fraction subtract(Fraction f) {
        int n = this.numerator * f.denominator - f.numerator * this.denominator;
        int d = this.denominator * f.denominator;
        return new Fraction(n, d);
    }

    public Fraction multiply(Fraction f) {
        return new Fraction(this.numerator * f.numerator,
                this.denominator * f.denominator);
    }

    private int gcf(int a, int b) {
        int rem;
        int gcf = 0;
        do {
            rem = a % b;
            if (rem == 0)
                gcf = b;
            else {
                a = b;
                b = rem;
            }
        } while (rem != 0);

        return gcf;
    }

}

