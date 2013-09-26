package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class DifferentiateTest {

    // Create an input set which covers the corner cases of exponents
    // (0, 1, any number, a fraction and a negative number) and throw
    // in some different coefficient values. Verify the derivatives of
    // these.
    Term[] TERMS = {
    	new Term(new Fraction(4,1), new Fraction(-1,1)),
   	new Term(new Fraction(1,4), new Fraction(0,1)),
       	new Term(new Fraction(3,1), new Fraction(4,9)),
	new Term(new Fraction(2,1), new Fraction(1,1)),
	new Term(new Fraction(1,3), new Fraction(8,1)),
    };
    
    // Manually compute the derivatives of the corner cases above.
    Term[] DERIVATIVES = {
     	new Term(new Fraction(-4,1), new Fraction(-2,1)),
      	new Term(new Fraction(0,1), new Fraction(0,1)),
       	new Term(new Fraction(12,9), new Fraction(-5,9)),
  	new Term(new Fraction(2,1), new Fraction(0,1)),
	new Term(new Fraction(8,3), new Fraction(7,1)),
    };
    
    static final String BAD_DIFFERENTIATE_MSG = "Derivative computed incorrectly by MathFunction";

    @Test
    public void testDifferentiate() {

	// Loop over the terms in the TERMS array, compute its
	// derivative and compare with the corresponding manually
	// computed derivative in the DERIVATIVES array.
 	for (int i=0; i<TERMS.length; i++) {
	    MathFunction mf = new MathFunction();
	    mf.addTerm(TERMS[i]);

	    MathFunction difs = mf.differentiate();
	    ArrayList<Term> d = difs.terms;

 	    Assert.assertTrue(BAD_DIFFERENTIATE_MSG, d.get(0).coefficient.compareTo(DERIVATIVES[i].coefficient)==0);
 	    Assert.assertTrue(BAD_DIFFERENTIATE_MSG, d.get(0).exponent.compareTo(DERIVATIVES[i].exponent)==0);
 	}
    }
}
