package com.swtanalytics.math;

import java.math.MathContext;
import java.util.Vector;

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
    public void testEquals() {
    	// f1 = x^2 + (-3)x + 5
    	MathFunction f1 = new MathFunction();
    	f1.addTerm( new Term( new Fraction( 1), new Fraction(2 )) );
    	f1.addTerm( new Term( new Fraction(-3), new Fraction(1 )) );
    	f1.addTerm( new Term( new Fraction( 5), new Fraction(0 )) );

    	// f2 = x^2 + (-3)x + 5
    	MathFunction f2 = new MathFunction();
    	f2.addTerm( new Term( new Fraction( 1), new Fraction(2 )) );
    	f2.addTerm( new Term( new Fraction(-3), new Fraction(1 )) );
    	f2.addTerm( new Term( new Fraction( 5), new Fraction(0 )) );

    	// f3 = x + 7
    	MathFunction f3 = new MathFunction();
    	f3.addTerm( new Term( new Fraction( 1 ), new Fraction( 1 )) );
    	f3.addTerm( new Term( new Fraction( 7 ), new Fraction( 0 )) );
    	
        Assert.assertEquals( f1, f2 );
        Assert.assertNotEquals( f1, f3 );
    }
    
    @Test
    public void testFunctionMultiply() {
    	// f1 = x^2 + (-3)x + 5
    	MathFunction f1 = new MathFunction();
    	f1.addTerm( new Term( new Fraction( 1), new Fraction(2 )) );
    	f1.addTerm( new Term( new Fraction(-3), new Fraction(1 )) );
    	f1.addTerm( new Term( new Fraction( 5), new Fraction(0 )) );

    	// f2 = x + 7
    	MathFunction f2 = new MathFunction();
    	f2.addTerm( new Term( new Fraction( 1 ), new Fraction( 1 )) );
    	f2.addTerm( new Term( new Fraction( 7 ), new Fraction( 0 )) );

    	// f1*f2 expected: (x^3 + 7x^2) + (-3x^2 + -21x) + (5x + 35) = x^3 + 4x^2 -16x +35 
    	MathFunction f1f2_expected = new MathFunction();
    	f1f2_expected.addTerm( new Term( new Fraction(  1), new Fraction(3)) );
    	f1f2_expected.addTerm( new Term( new Fraction(  4), new Fraction(2)) );
    	f1f2_expected.addTerm( new Term( new Fraction(-16), new Fraction(1)) );
    	f1f2_expected.addTerm( new Term( new Fraction( 35), new Fraction(0)) );
    	
    	MathFunction f1f2_computed = f1.multiply( f2 );    	
        Assert.assertEquals( f1f2_expected, f1f2_computed );
        
        // f3 = 0
        // f1*f3 expected: 0
        MathFunction f3;
        f3 = new MathFunction();
        MathFunction f1f3_expected = new MathFunction();
    	MathFunction f1f3_computed = f1.multiply( f3 );
        Assert.assertEquals( f1f3_expected, f1f3_computed );
    }
    
    @Test 
    public void testFunctionAdd() {
    	// f1 = x^2 + (-3)x + 5
    	MathFunction f1 = new MathFunction();
    	f1.addTerm( new Term( new Fraction( 1), new Fraction(2 )) );
    	f1.addTerm( new Term( new Fraction(-3), new Fraction(1 )) );
    	f1.addTerm( new Term( new Fraction( 5), new Fraction(0 )) );

    	// f2 = x + 7
    	MathFunction f2 = new MathFunction();
    	f2.addTerm( new Term( new Fraction( 1 ), new Fraction( 1 )) );
    	f2.addTerm( new Term( new Fraction( 7 ), new Fraction( 0 )) );
    	
    	// f3 = x^2 + (-2)x + 12
    	MathFunction f3 = new MathFunction();
    	f3.addTerm( new Term( new Fraction( 1 ),  new Fraction( 2 )) );
    	f3.addTerm( new Term( new Fraction( -2 ), new Fraction( 1 )) );
    	f3.addTerm( new Term( new Fraction( 12 ), new Fraction( 0 )) );
    	
    	Assert.assertEquals( f3, f1.add( f2 ));
    }

    private void checkFunctionForPoints( Vector<Fraction> points ) {    	
    	// Confirm that we can construct the function.
    	MathFunction f = new MathFunction( points );
    	System.out.println("@@@ f = " + f);

    	// Neville's method is constrained to producing polynomials whose terms have only
    	// non-negative, integer exponents.
    	int actualDegree = f.degree().intValue();
    	
    	int maxExpectedDegree = (points.size()/2) - 1;
    	Assert.assertTrue( actualDegree >= 0 );
    	Assert.assertTrue( actualDegree <= maxExpectedDegree );
    	
    	MathContext mc = MathContext.DECIMAL128;
    	
    	// This value was chosen simply because it seemed reasonable.
    	final double epsilon = 1e-10; 
    	
    	for (int i = 0; i < (points.size()/2); ++i) {
    		double x = points.elementAt(2*i).doubleValue(mc);
    		double y_expected = points.elementAt((2*i) + 1).doubleValue(mc);
    		double y_obtained = f.evaluate( x, mc );
    		
    		Assert.assertEquals(y_expected, y_obtained, epsilon);
    	}
    }
    
    @Test
    public void testFitPointsConstructor() {
    	Vector<Fraction> points = new Vector<Fraction>();
    	
    	
    	// Test a constant function.
    	// (0, 42)
    	points.clear();
    	points.add( new Fraction(0) );
    	points.add( new Fraction(42) );
    	
    	// (1, 42)
    	points.add( new Fraction(1) );
    	points.add( new Fraction(42) );
    	
    	checkFunctionForPoints( points );
    	
    	// TODO: Test a linear function.
    	
    	// TODO: Test a complicated function, by evaluating it at its defined points, and 
    	// confirming its degree
    }
    
    @Test(expected= java.lang.IllegalArgumentException.class)
    public void testFitPointsConstructorRejectsDuplicateXValues() {
    	Vector<Fraction> points = new Vector<Fraction>();
    	
    	// (0, 0)
    	points.add( new Fraction(0) );
    	points.add( new Fraction(0) );
    	
    	// (0, 1)
    	points.add( new Fraction(0) );
    	points.add( new Fraction(1) );

    	new MathFunction( points );
    }
}
