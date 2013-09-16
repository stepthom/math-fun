package com.swtanalytics.math;

public class Term implements Comparable<Term> {
	
	protected int coefficient;
	protected int exponent;
	
	public Term (int c, int e){
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
		return String.format("%+dx^%d", this.coefficient, this.exponent);
	}

    public int compareTo(Term t) {
        return t.exponent - this.exponent;
    }

}
