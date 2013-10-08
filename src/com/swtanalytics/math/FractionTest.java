package com.swtanalytics.math;

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
        Assert.assertEquals("0/17", result);
    }

    @Test
    public void testSubtract() {
        // Create an input set which covers the corner cases of fractions.
        Fraction oneThirdMinusSmallNegative = new Fraction(13, 21);
        Fraction bigNegativeMinusOneThird = new Fraction(-19, 3);
        Fraction smallNegativeMinusBigNegative = new Fraction(40, 7);

        String BAD_SUBTRACT_MSG = "Fraction subtraction computed incorrectly.";

        Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(oneThird).doubleValue(), oneThird.doubleValue() * -1.0, 0.0);
        Assert.assertEquals(BAD_SUBTRACT_MSG, zero.subtract(smallNegative).doubleValue(), smallNegative.doubleValue() * -1.0, 0.0);
        Assert.assertEquals(BAD_SUBTRACT_MSG, oneThird.subtract(zero).doubleValue(), oneThird.doubleValue(), 0.0);
        Assert.assertEquals(BAD_SUBTRACT_MSG, oneThird.subtract(smallNegative).doubleValue(), oneThirdMinusSmallNegative.doubleValue(), 0.0);
        Assert.assertEquals(BAD_SUBTRACT_MSG, bigNegative.subtract(oneThird).doubleValue(), bigNegativeMinusOneThird.doubleValue(), 0.0);
        Assert.assertEquals(BAD_SUBTRACT_MSG, smallNegative.subtract(bigNegative).doubleValue(), smallNegativeMinusBigNegative.doubleValue(), 0.0);
    }

    @Test
    public void testSimplify() {
        // Create an input set which covers the corner cases of fractions.
        Fraction minusfourth = new Fraction(-13, 52);
        Fraction same = new Fraction(-11, 12);
        Fraction minusminus = new Fraction(-12, -3);

        String BAD_SIMPLIFY_MSG = "Fraction simplified incorrectly.";

        Assert.assertEquals(BAD_SIMPLIFY_MSG, zero.numerator, 0);

        Assert.assertEquals(BAD_SIMPLIFY_MSG, oneThird.numerator, 1);
        Assert.assertEquals(BAD_SIMPLIFY_MSG, oneThird.denominator, 3);

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
