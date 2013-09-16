package com.swtanalytics.math;

public class Term implements Comparable<Term> {

	protected Fraction coefficient;
	protected Fraction exponent;
	
	public Term (Fraction c, Fraction e){
		this.coefficient = c;
		this.exponent = e;
	}

    public String prettyPrint(boolean isFirstTerm) {
            // Not smart about string construction. Just dumb manipulation

            String result = this.toString();
            if (!isFirstTerm) {
                result = result.substring(0,1) + ' '
                         + result.substring(1, result.length());
            }
            return result;
    }
	
	public String toString(){
        String exp = this.exponent.formatString(true);
		return String.format("%sx^%s", this.coefficient, exp);
	}

    public int compareTo(Term t) {
        return this.exponent.compareTo(t.exponent);
    }
}

