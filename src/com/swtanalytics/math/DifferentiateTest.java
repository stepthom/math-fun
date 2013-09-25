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
        new Term(new Fraction(1,4), new Fraction(0,1)),
	new Term(new Fraction(2,1), new Fraction(1,1)),
	new Term(new Fraction(1,3), new Fraction(8,1)),
	new Term(new Fraction(3,1), new Fraction(4,9)),
	new Term(new Fraction(4,1), new Fraction(-1,1)),
    };
    
    // Manually compute the derivatives of the corner cases above
    // and put in reverse order
    Term[] DERIVATIVES = {
	new Term(new Fraction(-4,1), new Fraction(-2,1)),
	new Term(new Fraction(12,9), new Fraction(-5,1)),
 	new Term(new Fraction(8,3), new Fraction(7,1)),
 	new Term(new Fraction(2,1), new Fraction(0,1)),
 	new Term(new Fraction(0,1), new Fraction(0,1)),
    };
    
    static final String BAD_DIFFERENTIATE_MSG = "Derivative computed incorrectly by MathFunction";

    @Test
    public void testDifferentiate() {

        // Create a new math function and add the test terms to it.
        MathFunction mf = new MathFunction();
        for (Term t : TERMS) {
            mf.addTerm(t);
        }

	// Compute the derivatives
	MathFunction difs = mf.differentiate();

	ArrayList<Term> d = difs.terms;

	// Make sure that we have the right number of manually computed terms...
	Assert.assertEquals("Uh oh! The computed and your manul Differentiate arrays are not of the same size.",
			   d.size(), DERIVATIVES.length);

 	// Compare computed derivatives with manually derived ones.
 	for (int i=0; i<d.size(); i++) {
 	    Assert.assertEquals(BAD_DIFFERENTIATE_MSG, d.get(i).coefficient, DERIVATIVES[i].coefficient);
 	    Assert.assertEquals(BAD_DIFFERENTIATE_MSG, d.get(i).exponent, DERIVATIVES[i].exponent);
 	}
    }
}
