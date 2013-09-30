package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class FractionTest {

    @Test
    public void testSubtract() {

	// Create an input set which covers the corner cases of fractions.
	Fraction zero = new Fraction(0,4);
	Fraction third = new Fraction(1,3);
	Fraction negsmall = new Fraction(-2,7);
	Fraction negbig = new Fraction(-6,1);
	Fraction thirdminusnegsmall = new Fraction(13, 21);
	Fraction negbigminusthird = new Fraction(-19, 3);
	Fraction negsmallminusnegbig = new Fraction(40, 7);
	
	String BAD_SUBTRACT_MSG = "Fraction subtraction computed incorrectly.";

	Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(third).doubleValue(), third.doubleValue() * -1.0, 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(negsmall).doubleValue(), negsmall.doubleValue() * -1.0, 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, third.subtract(zero).doubleValue(), third.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, third.subtract(negsmall).doubleValue(), thirdminusnegsmall.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, negbig.subtract(third).doubleValue(), negbigminusthird.doubleValue(), 0.0);
	Assert.assertEquals(BAD_SUBTRACT_MSG, negsmall.subtract(negbig).doubleValue(), negsmallminusnegbig.doubleValue(), 0.0);
    }

    @Test
    public void testSimplify() {

	// Create an input set which covers the corner cases of fractions.
	Fraction zero = new Fraction(0,4);
	Fraction third = new Fraction(2,6);
	Fraction seven = new Fraction(7,1);
	Fraction minusfourth = new Fraction(-13, 52);
	Fraction same = new Fraction(-11, 12);
	Fraction minusminus = new Fraction(-12, -3);
	
	String BAD_SIMPLIFY_MSG = "Fraction simplified incorrectly.";

   	Assert.assertEquals(BAD_SIMPLIFY_MSG, zero.numerator, 0);
 
   	Assert.assertEquals(BAD_SIMPLIFY_MSG, third.numerator, 1);
   	Assert.assertEquals(BAD_SIMPLIFY_MSG, third.denominator, 3);
 
   	Assert.assertEquals(BAD_SIMPLIFY_MSG, seven.numerator, 7);
   	Assert.assertEquals(BAD_SIMPLIFY_MSG, seven.denominator, 1);
 
    	Assert.assertEquals(BAD_SIMPLIFY_MSG, minusfourth.numerator, -1);
   	Assert.assertEquals(BAD_SIMPLIFY_MSG, minusfourth.denominator, 4);

  	Assert.assertEquals(BAD_SIMPLIFY_MSG, same.numerator, -11);
  	Assert.assertEquals(BAD_SIMPLIFY_MSG, same.denominator, 12);

  	Assert.assertEquals(BAD_SIMPLIFY_MSG, minusminus.numerator, 4);
  	Assert.assertEquals(BAD_SIMPLIFY_MSG, minusminus.denominator, 1);
    }
    
}
