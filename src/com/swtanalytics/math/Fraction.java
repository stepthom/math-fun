package com.swtanalytics.math;

public class Fraction {
    protected int numerator;
    protected int denominator;


    public Fraction(int n, int d) {
        this.numerator = n;
        this.denominator = d;
    }

    public String toString() {
        int n = this.numerator;
        int d = Math.abs(this.denominator);
        if (this.denominator < 0) {
            n *= -1;
        }

        String result = String.format("%+dx", n);

        // TODO: Collapse into integer?
        result = String.format("%s/%dx", result, d);

        return result;
    }

    public int doubleValue() {
        return this.numerator / this.denominator;
    }

    public int compareTo(Fraction f) {
        Double result = new Double(this.doubleValue() - f.doubleValue());
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

