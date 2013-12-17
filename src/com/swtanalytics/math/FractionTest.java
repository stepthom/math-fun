package com.swtanalytics.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class FractionTest {
    private static final Fraction zero = new Fraction(0, 4);
    private static final Fraction oneThird = new Fraction(1, 3);
    private static final Fraction twoThirds = new Fraction(2, 3);
    private static final Fraction smallNegative = new Fraction(-2, 7);
    private static final Fraction bigNegative = new Fraction(-6, 1);
    private static final Fraction seven = new Fraction(7, 1);

    @Test
    public void zeroPlusZeroEqualsZero() {
        Assert.assertEquals(zero, zero.add(zero));
    }

    @Test
    public void zeroPlusXEqualsX() {
        Assert.assertEquals(oneThird, zero.add(oneThird));
    }

    @Test
    public void xPlusZeroEqualsX() {
        Assert.assertEquals(oneThird, oneThird.add(zero));
    }

    @Test
    public void twoPositivesAddProperly() {
        Assert.assertEquals(twoThirds, oneThird.add(oneThird));
    }

    @Test
    public void twoNegativesAddProperly() {
        Fraction smallNegativePlusBigNegative = new Fraction(-44, 7);
        Assert.assertEquals(smallNegativePlusBigNegative, smallNegative.add(bigNegative));
    }

    @Test
    public void negativePlusPositiveComputesCorrectly() {
        Fraction expected = new Fraction(1, 21);
        Assert.assertEquals(expected, smallNegative.add(oneThird));
    }

    @Test
    public void positivePlusNegativeComputesCorrectly() {
        Fraction expected = new Fraction(1, 21);
        Assert.assertEquals(expected, oneThird.add(smallNegative));
    }

    @Test
    public void compareToIsNotZeroForTwoSmallUnequalFractions() {
        // There was previously a bug where two small (less than 1, greater than 0)
        // fractions would always be considered "equal"
        Assert.assertNotEquals(0, oneThird.compareTo(twoThirds));
    }

    @Test
    public void twoFractionsThatReduceTheSameAreEqual() {
        Fraction twoSixths = new Fraction(2, 6);
        Assert.assertEquals(twoSixths, oneThird);
    }

    @Test
    public void whenCoefficientIsZeroAndFlagIsSetDoNotPrintSign() {
        // Act
        Fraction fraction = new Fraction(0, 17);
        String result = fraction.formatString(true, false);

        //Assert
        Assert.assertEquals("0", result);
    }

    @Test
    public void testSubtract() {
        // Create an input set which covers the corner cases of fractions.
        Fraction oneThirdMinusSmallNegative = new Fraction(13, 21);
        Fraction bigNegativeMinusOneThird = new Fraction(-19, 3);
        Fraction smallNegativeMinusBigNegative = new Fraction(40, 7);

        String BAD_SUBTRACT_MSG = "Fraction subtraction computed incorrectly.";
        
        // The choice of 128-bit precision is somewhat arbitrary, but it's used to demonstrate that we can get more precision
        // than Java's built-in 'double' type.
        MathContext mc = MathContext.DECIMAL128; 
        
        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        		            zero.subtract(oneThird).bigDecimalValue( mc ).doubleValue(), 
        		            oneThird.bigDecimalValue( mc ).doubleValue() * -1.0, 
        		            0.0);
        
        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        					zero.subtract(smallNegative).bigDecimalValue( mc ).doubleValue(), 
        					smallNegative.bigDecimalValue( mc ).doubleValue() * -1.0, 
        					0.0);

        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        				    oneThird.subtract(zero).bigDecimalValue( mc ).doubleValue(), 
        				    oneThird.bigDecimalValue( mc ).doubleValue(), 
        				    0.0);
        
        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        					oneThird.subtract(smallNegative).bigDecimalValue( mc ).doubleValue(), 
        					oneThirdMinusSmallNegative.bigDecimalValue( mc ).doubleValue(), 
        					0.0);
        
        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        					bigNegative.subtract(oneThird).bigDecimalValue( mc ).doubleValue(), 
        					bigNegativeMinusOneThird.bigDecimalValue( mc ).doubleValue(), 
        					0.0);
        
        Assert.assertEquals(BAD_SUBTRACT_MSG, 
        					smallNegative.subtract(bigNegative).bigDecimalValue( mc ).doubleValue(), 
        					smallNegativeMinusBigNegative.bigDecimalValue( mc ).doubleValue(), 
        					0.0);
    }

    @Test
    public void testSimplify() {
        // Create an input set which covers the corner cases of fractions.
        Fraction minusfourth = new Fraction(-13, 52);
        Fraction same = new Fraction(-11, 12);
        Fraction minusminus = new Fraction(-12, -3);

        String BAD_SIMPLIFY_MSG = "Fraction simplified incorrectly.";

        Assert.assertEquals(BAD_SIMPLIFY_MSG, zero.numerator, BigInteger.valueOf(0));

        Assert.assertEquals(BAD_SIMPLIFY_MSG, oneThird.numerator, BigInteger.valueOf(1));
        Assert.assertEquals(BAD_SIMPLIFY_MSG, oneThird.denominator, BigInteger.valueOf(3));

        Assert.assertEquals(BAD_SIMPLIFY_MSG, seven.numerator, BigInteger.valueOf(7));
        Assert.assertEquals(BAD_SIMPLIFY_MSG, seven.denominator, BigInteger.valueOf(1));

        Assert.assertEquals(BAD_SIMPLIFY_MSG, minusfourth.numerator, BigInteger.valueOf(-1));
        Assert.assertEquals(BAD_SIMPLIFY_MSG, minusfourth.denominator, BigInteger.valueOf(4));

        Assert.assertEquals(BAD_SIMPLIFY_MSG, same.numerator, BigInteger.valueOf(-11));
        Assert.assertEquals(BAD_SIMPLIFY_MSG, same.denominator, BigInteger.valueOf(12));

        Assert.assertEquals(BAD_SIMPLIFY_MSG, minusminus.numerator, BigInteger.valueOf(4));
        Assert.assertEquals(BAD_SIMPLIFY_MSG, minusminus.denominator, BigInteger.valueOf(1));
    }
    
    @Test 
    public void testCanHandleBigNumbers() {
    	// Confirm that Fraction objects can handle values larger than Java primitive int's could
    	// accommodate.
    	Fraction maxPosIntFraction = new Fraction( Integer.MAX_VALUE );
    	Fraction minNegIntFraction = new Fraction( Integer.MIN_VALUE );
    	
    	String BAD_CAN_HANDLE_BIG_NUMBERS_MSG = "Encountered unexpected numerical overflow or underflow.";
    	
    	Fraction potentialIntOverflow = maxPosIntFraction.multiply( new Fraction(2) );
    	Assert.assertEquals( BAD_CAN_HANDLE_BIG_NUMBERS_MSG, 
    						 maxPosIntFraction.compareTo( potentialIntOverflow ), -1 );

    	Fraction potentialIntUnderflow = minNegIntFraction.multiply( new Fraction(2) );
    	Assert.assertEquals( BAD_CAN_HANDLE_BIG_NUMBERS_MSG,
    						 minNegIntFraction.compareTo( potentialIntUnderflow ), 1 );
    }

    
    @Test
    public void testValidConversions() {
    	Assert.assertEquals( new Fraction(42).intValue(), 42 );
    	Assert.assertEquals( new Fraction(42L).longValue(), 42L );

    	float epsilon = 0.01f;
    	
    	Assert.assertEquals( new Fraction(42).floatValue(MathContext.DECIMAL128), 42.0f, epsilon );
    	Assert.assertEquals( new Fraction(42).doubleValue(MathContext.DECIMAL128), 42.0d, epsilon );
    }    
    
    @Test(expected= java.lang.ArithmeticException.class)
    public void testInvalidIntConversion() {
    	new Fraction( new Long(Long.MAX_VALUE)).intValue();
    }
    
    @Test(expected= java.lang.ArithmeticException.class)
    public void testInvalidLongConversion() {
    	Fraction f1 = new Fraction( Long.MAX_VALUE );
    	Fraction f2 = f1.multiply(new Fraction(2, 1));
    	f2.longValue();
    }

    @Test
    public void testDivide() {
    	Fraction f1 = new Fraction(2, 3);
    	Fraction f2 = new Fraction(3, 2);
    	
    	Assert.assertEquals( f1.divide(f1), new Fraction( 1 ));
    	Assert.assertEquals( f1.divide(f2), new Fraction( 4, 9 ));
    }
    
    @Test 
    public void testNegate() {
    	Assert.assertEquals( new Fraction( 2, 3).negate(), new Fraction(-2, 3) );
    	Assert.assertEquals( new Fraction(-2, 3).negate(), new Fraction( 2, 3) );
    	Assert.assertEquals( new Fraction( 0, 1).negate(), new Fraction( 0, 1) );
    }
    
    @Test 
    public void testInvert() {
    	Assert.assertEquals( new Fraction( 2, 3).invert(), new Fraction( 3, 2) );
    	Assert.assertEquals( new Fraction(-2, 3).invert(), new Fraction(-3, 2) );
    }

    @Test(expected= java.lang.ArithmeticException.class)
    public void testInvertError() {
    	new Fraction(0, 1).invert();
    }
    
    @Test
    public void testSign() {
    	Assert.assertEquals( new Fraction( 2, 3).sign(),  1 );
    	Assert.assertEquals( new Fraction( 0, 1).sign(),  0 );
    	Assert.assertEquals( new Fraction(-2, 3).sign(), -1 );
    }
    
    @Test
    public void testAbs() {
    	Assert.assertEquals( new Fraction( 2, 3).abs(), new Fraction(2, 3) );
    	Assert.assertEquals( new Fraction(-2, 3).abs(), new Fraction(2, 3) );
    	Assert.assertEquals( new Fraction( 0, 1).abs(), new Fraction(0, 1) );
    }
    
    @Test
    public void testGeometricAbs() {
    	Fraction f1 = new Fraction( 10, 3 );
    	Fraction f2 = new Fraction( 12, 3 );
    	
    	Assert.assertEquals( f1.geometricAbs(f2), new Fraction(2, 3) );
    	Assert.assertEquals( f2.geometricAbs(f1), new Fraction(2, 3) );
    }
    
    @Test
    public void testFloor() {
    	Assert.assertEquals( new Fraction( 20, 3).floor(), BigInteger.valueOf( 6) );
    	Assert.assertEquals( new Fraction( 21, 3).floor(), BigInteger.valueOf( 7) );
    	Assert.assertEquals( new Fraction(-20, 3).floor(), BigInteger.valueOf(-7) );
    	Assert.assertEquals( new Fraction(-21, 3).floor(), BigInteger.valueOf(-7) );
    	Assert.assertEquals( new Fraction(  0, 3).floor(), BigInteger.valueOf( 0) );
    }

    @Test
    public void testCeil() {
        Assert.assertEquals( new Fraction( 20, 3).ceil(), BigInteger.valueOf( 7) );
       	Assert.assertEquals( new Fraction( 21, 3).ceil(), BigInteger.valueOf( 7) );
    	Assert.assertEquals( new Fraction(-20, 3).ceil(), BigInteger.valueOf(-6) );
    	Assert.assertEquals( new Fraction(-21, 3).ceil(), BigInteger.valueOf(-7) );
    	Assert.assertEquals( new Fraction(  0, 3).ceil(), BigInteger.valueOf( 0) );
    }
    
    @Test
    public void testRound() {
    	Assert.assertEquals( new Fraction(  9, 3).round(), BigInteger.valueOf( 3) );
    	Assert.assertEquals( new Fraction( 10, 3).round(), BigInteger.valueOf( 3) );
    	Assert.assertEquals( new Fraction( 11, 3).round(), BigInteger.valueOf( 4) );
    	Assert.assertEquals( new Fraction( -9, 3).round(), BigInteger.valueOf(-3) );
    	Assert.assertEquals( new Fraction(-10, 3).round(), BigInteger.valueOf(-3) );
    	Assert.assertEquals( new Fraction(-11, 3).round(), BigInteger.valueOf(-4) );
    	Assert.assertEquals( new Fraction(  0, 3).round(), BigInteger.valueOf( 0) );
    }
    
    @Test
    public void testPow() {
    	float epsilon = 0.01f;
    	
    	Fraction x = new Fraction(2, 1);
    	Fraction y = new Fraction(10, 1);
    	BigDecimal p = x.pow(y, MathContext.DECIMAL128);
    	
    	Assert.assertEquals( p.doubleValue(), 1024.0, epsilon );
    }

    @Test(expected= java.lang.NumberFormatException.class)
    public void testPowInternalOverflow() {
    	// We don't use exactly Double.MAX_VALUE, because it seems that when we convert that to
    	// a BigDecimal, we get something that lies slightly outside the range of a double.  That
    	// causes this test to fail for a different reason than we intend.
    	Fraction base = new Fraction( new BigDecimal( Double.MAX_VALUE / 2.0, MathContext.DECIMAL128 ).toBigInteger(), 
    							      BigInteger.valueOf( 1 ) );
    	
    	Fraction exp = new Fraction( 10000, 1 );
    	base.pow( exp, MathContext.DECIMAL128 );
    }

    @Test
    public void testEquals() {
    	Fraction whole        = new Fraction( 2, 1 );
    	Fraction notWhole     = new Fraction( 3, 4 );
    	Fraction wholeVeryNeg = new Fraction( BigInteger.valueOf( Integer.MIN_VALUE - 1) );
    	
    	Assert.assertEquals   ( whole,    new Integer(2) );
    	Assert.assertNotEquals( notWhole, new Integer(2) );
    	Assert.assertNotEquals( whole,    new Integer(3) );
    	
    	Assert.assertEquals   ( whole,    new Long(2) );
    	Assert.assertNotEquals( notWhole, new Long(2) );
    	Assert.assertNotEquals( whole,    new Long(3) );
    	
    	Assert.assertEquals   ( whole,    BigInteger.valueOf(2) );
    	Assert.assertNotEquals( notWhole, BigInteger.valueOf(2) );
    	Assert.assertNotEquals( whole,    BigInteger.valueOf(3) );

    	Assert.assertEquals   ( whole,    whole );
    	Assert.assertEquals   ( whole,    new Fraction(2, 1) );
    	Assert.assertEquals   ( whole,    new Fraction(4, 2) );
    	Assert.assertEquals   ( notWhole, notWhole );
    	Assert.assertNotEquals( whole,    notWhole );
    	
    	// These exercise value-specific code paths within Fraction.equals...
    	Assert.assertNotEquals( wholeVeryNeg, Integer.MIN_VALUE );
    	Assert.assertNotEquals( wholeVeryNeg, Long   .MIN_VALUE );

    	// These are to make sure that false is returned, rather than an exception being thrown...
    	Assert.assertNotEquals( whole, null );
    	Assert.assertNotEquals( whole, new String("Hello, World!") );
    }
    
    @Test
    public void testStringConstructor() {
    	Assert.assertEquals( new Fraction("1"),    new Fraction( 1   ));
    	Assert.assertEquals( new Fraction("+1"),   new Fraction( 1   ));
    	Assert.assertEquals( new Fraction("-1"),   new Fraction(-1   ));
    	Assert.assertEquals( new Fraction("~1"),   new Fraction(-1   ));
    	Assert.assertEquals( new Fraction("0"),    new Fraction( 0   ));
    	Assert.assertEquals( new Fraction("1/2"),  new Fraction( 1, 2));
    	Assert.assertEquals( new Fraction("+1/2"), new Fraction( 1, 2));
    	Assert.assertEquals( new Fraction("~2/3"), new Fraction(-2, 3));
    	
    	Fraction f;
    	
    	f= new Fraction( 1 );
    	Assert.assertEquals( f, new Fraction(f.toString()));
    	
    	f= new Fraction( 1, 2 );
    	Assert.assertEquals( f, new Fraction(f.toString()));
    	
    	f= new Fraction( -1 );
    	Assert.assertEquals( f, new Fraction(f.toString()));    	
    	
    	f= new Fraction( -1, 2 );
    	Assert.assertEquals( f, new Fraction(f.toString()));    	
    	
    	f= new Fraction( 0 );
    	Assert.assertEquals( f, new Fraction(f.toString()));    	
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor1() {
    	new Fraction( "" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor2() {
    	new Fraction( "X" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor3() {
    	new Fraction( "/0" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor4() {
    	new Fraction( "/2" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor5() {
    	new Fraction( "/-2" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor6() {
    	new Fraction( "-1/-2" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor7() {
    	new Fraction( "1/0" );
    }
    
    @Test(expected= java.lang.NumberFormatException.class)
    public void testInvalidStringConstructor8() {
    	new Fraction( " 1/2 " );
    }
}
