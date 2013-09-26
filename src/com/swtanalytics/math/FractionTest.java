package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class FractionTest {

    // Create an input set which covers the corner cases of fractions.
    Fraction zero = new Fraction(0,4);
    Fraction third = new Fraction(1,3);
    Fraction negsmall = new Fraction(-2,7);
    Fraction negbig = new Fraction(-6,1);
    Fraction thirdminusnegsmall = new Fraction(13, 21);
    Fraction negbigminusthird = new Fraction(-19, 3);
    Fraction negsmallminusnegbig = new Fraction(40, 7);
    
    
    static final String BAD_SUBTRACT_MSG = "Fraction subtraction computed incorrectly.";

    @Test
    public void testSubtract() {

// 	System.out.print("Original function is: \n");
// 	System.out.print(TERMS[0]);
// 	System.out.print("\nComputed derivative is: \n");
// 	System.out.print(d.get(0));
// 	System.out.print("\nManual derivative is: \n");
// 	System.out.print(DERIVATIVES[0]);
// 	System.out.print("\n");

	Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(third).doubleValue(), third.doubleValue() * -1.0, 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(negsmall).doubleValue(), negsmall.doubleValue() * -1.0, 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, third.subtract(zero).doubleValue(), third.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, third.subtract(negsmall).doubleValue(), thirdminusnegsmall.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, negbig.subtract(third).doubleValue(), negbigminusthird.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, negsmall.subtract(negbig).doubleValue(), negsmallminusnegbig.doubleValue(), 0.0);
    }
}
