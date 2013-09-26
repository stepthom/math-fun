package com.swtanalytics.math;

import java.util.ArrayList;
import java.util.Collections;

public class MathFunction {

    protected ArrayList<Term> terms = new ArrayList<Term>();
	
    public MathFunction(){
		
    }
	
    public void addTerm(Term t){
	terms.add(t);
        Collections.sort(this.terms);
    }
	
	
    public String toString(){
	String result = "f(x) = ";
        boolean first_term = true;
	for (Term t: terms){

	    result += (t.prettyPrint(first_term) + " ");
            first_term = false;
	}
	return result;
    }

    public MathFunction differentiate() {
        MathFunction df = new MathFunction();
        for (Term t: terms) {

            // XXX This will make uncollapsed x^0 and x^1 terms in the
            //     Style of the original class.
            Fraction c;
            Fraction e;
            if (t.exponent.numerator == 0) {
                // Leave the zero terms in case its the only one.
                c = new Fraction(0, 1);
                e = new Fraction(0, 1);
            }
            else {
		c = t.coefficient.multiply(t.exponent);
		e = t.exponent.subtract(new Fraction(1,1));
            }
            Term dt = new Term(c, e);
            df.addTerm(dt);
        }

        return df;
    }
}
