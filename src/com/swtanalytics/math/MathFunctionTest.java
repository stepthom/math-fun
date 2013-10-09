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
    public void findMinimumCanFindMinimumAtLowerBound() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(1, 1)));

        // Act
        double result = function.findMinimum(-50, 50);

        // Assert
        Assert.assertEquals(-50, result, 1e-10);
    }

    @Test
    public void findMinimumCanFindMinimumAtUpperBound() {
        // Arrange
        function.addTerm(new Term(new Fraction(-1, 1), new Fraction(1, 1)));

        // Act
        double result = function.findMinimum(-50, 50);

        // Assert
        Assert.assertEquals(-50, result, 1e-10);
    }

    @Test
    public void findMaximumCanFindMaximumAtLowerBound() {
        // Arrange
        function.addTerm(new Term(new Fraction(-1, 1), new Fraction(1, 1)));

        // Act
        double result = function.findMaximum(-50, 50);

        // Assert
        Assert.assertEquals(50, result, 1e-10);
    }

    @Test
    public void findMaximumCanFindMaximumAtUpperBound() {
        // Arrange
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(1, 1)));

        // Act
        double result = function.findMaximum(-50, 50);

        // Assert
        Assert.assertEquals(50, result, 1e-10);
    }

    @Test
    public void findMinimumCanFindMinimumInMiddle() {
        // Arrange
        // Function is f(x) = (x - 1)(x - 2)
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(-3, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(2, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMinimum(-50, 50);

        // Assert
        Assert.assertEquals(-.25, result, 1e-10);
    }

    @Test
    public void findMinimumCanFindMinimumNotInMiddle() {
        // Arrange
        // Function is f(x) = -(x - 1)(x - 2)
        function.addTerm(new Term(new Fraction(-1, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(3, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(-2, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMinimum(-50, 50);

        // Assert
        Assert.assertEquals(-2652, result, 1e-10);
    }

    @Test
    public void findMaximumCanFindMaximumInMiddle() {
        // Arrange
        // Function is f(x) = -(x - 1)(x - 2)
        function.addTerm(new Term(new Fraction(-1, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(3, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(-2, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMaximum(-50, 50);

        // Assert
        Assert.assertEquals(.25, result, 1e-10);
    }

    @Test
    public void findMaximumCanFindMaximumNotInMiddle() {
        // Arrange
        // Function is f(x) = (x - 1)(x - 2)
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(-3, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(2, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMaximum(-50, 50);

        // Assert
        Assert.assertEquals(2652, result, 1e-10);
    }

    @Test
    public void findMaximumOfFunctionWithMoreTerms() {
        // Arrange
        // Function is f(x) = (x - 1)(x - 2)(x - 3)(x - 4)
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(4, 1)));
        function.addTerm(new Term(new Fraction(-10, 1), new Fraction(3, 1)));
        function.addTerm(new Term(new Fraction(35, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(-50, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(24, 1), new Fraction(0, 1)));


        // Act
        double result = function.findMaximum(-50, 50);

        // Assert
        // 7590024 occurs at -50
        Assert.assertEquals(7590024, result, 1e-10);
    }

    @Test
    public void findMinimumOfFunctionWithMoreTerms() {
        // Arrange
        // Function is f(x) = (x - 1)(x - 2)(x - 3)(x - 4)
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(4, 1)));
        function.addTerm(new Term(new Fraction(-10, 1), new Fraction(3, 1)));
        function.addTerm(new Term(new Fraction(35, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(-50, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(24, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMinimum(-50, 50);

        // Assert
        Assert.assertEquals(-1, result, 1e-10);
    }

    @Test
    public void findLocalMaximumWithMoreTerms() {
        // Arrange
        // Function is f(x) = (x - 1)(x - 2)(x - 3)(x - 4)
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(4, 1)));
        function.addTerm(new Term(new Fraction(-10, 1), new Fraction(3, 1)));
        function.addTerm(new Term(new Fraction(35, 1), new Fraction(2, 1)));
        function.addTerm(new Term(new Fraction(-50, 1), new Fraction(1, 1)));
        function.addTerm(new Term(new Fraction(24, 1), new Fraction(0, 1)));

        // Act
        double result = function.findMaximum(2, 3);

        // Assert
        // There is a local maximum at .5625
        Assert.assertEquals(.5625, result, 1e-10);
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
