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
}
