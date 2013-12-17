package com.swtanalytics.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MathFunctionTest {
    private MathFunction function;

    @Before
    public void before() {
        function = new MathFunction();
    }

    @Test
    public void isLinearFunctionReturnsTrueForSingleLinearTerm() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(1, 1)));

        // Act
        boolean result = function.isLinearFunction();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isLinearFunctionReturnsTrueForSingleConstantTerm() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(0, 1)));

        // Act
        boolean result = function.isLinearFunction();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isLinearFunctionReturnsTrueForConstantAndLinearTerms() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(0, 1)));
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(1, 1)));

        // Act
        boolean result = function.isLinearFunction();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isLinearFunctionReturnsFalseForOtherTerm() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(2, 1)));

        // Act
        boolean result = function.isLinearFunction();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void slopeIsLinearCoefficient() {
        // Arrange
        function.addTerm(new Term(new Fraction(37, 19), new Fraction(1, 1)));

        // Act
        Fraction result = function.computeSlope();

        // Assert
        Assert.assertEquals(new Fraction(37, 19), result);
    }

    @Test
    public void slopeIsZeroForConstantTerm() {
        // Arrange
        function.addTerm(new Term(new Fraction(37, 19), new Fraction(0, 1)));

        // Act
        Fraction result = function.computeSlope();

        // Assert
        Assert.assertEquals(new Fraction(0, 101), result);
    }
    
    @Test
    public void equalsTests() {
    	// Arrange
    	MathFunction f1 = new MathFunction();
    	MathFunction f2 = new MathFunction();
    	MathFunction f3 = null;
    	MathFunction f4 = null;

    	// Act
    	f1.addTerm(new Term(13,0));
    	f1.addTerm(new Term(0, 4));
       	f1.addTerm(new Term(new Fraction(4,2), new Fraction(-4,3)));
       	f1.addTerm(new Term(3,3));
       	f1.addTerm(new Term(1,5));

       	f2.addTerm(new Term(new Fraction(10,3), 4));
       	f2.addTerm(new Term(1,5));
    	f2.addTerm(new Term(3,3));
       	f2.addTerm(new Term(1, new Fraction(-4,3)));
       	f2.addTerm(new Term(1, new Fraction(-4,3)));
       	f2.addTerm(new Term(new Fraction(-20,6), 4));
       	f2.addTerm(new Term(0, 13));
       	f2.addTerm(new Term(13,0));

       	boolean equals1 = f1.equals(f2);
       	boolean equals2 = f2.equals(f1);
       	
       	// ensure other locals aren't being checked
       	f1.integrate();
       	f2.differentiate();
       	
       	boolean equals3 = f1.equals(f2);
       	boolean equals4 = f2.equals(f1);

       	f2.addTerm(new Term(1,3));
       	
       	boolean notEquals1 = f1.equals(f2);
       	boolean notEquals2 = f2.equals(f1);
       	
       	boolean notEquals3 = f1.equals(f3);
       	
       	f3 = new MathFunction();
       	boolean notEquals4 = f1.equals(f3);
       	
       	f3 = f2.integrate();
       	f4 = f3.differentiate();
       	boolean equals5 = f2.equals(f4);
       	boolean equals6 = f4.equals(f2);
       	
       	f1.addTerm(new Term(-13,0));  // drop constant
       	f3 = f1.differentiate();
       	f4 = f3.integrate();
       	boolean equals7 = f1.equals(f4);
       	boolean equals8 = f4.equals(f1);
       	
    	// Assert
    	Assert.assertTrue(equals1 && equals2 && equals3 && equals4 && 
    			equals5 && equals6 && equals7 && equals8);
    	Assert.assertFalse(notEquals1 || notEquals2 || notEquals3 || notEquals4);
    }
}
