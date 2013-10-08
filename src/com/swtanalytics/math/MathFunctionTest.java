package com.swtanalytics.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MathFunctionTest {
    @Test
    public void whenLeadingTermHasZeroCoefficientToString() {
        // Arrange
        MathFunction function = new MathFunction();
        function.addTerm(new Term(new Fraction(0, 1), new Fraction(7, 1)));
        function.addTerm(new Term(new Fraction(1, 1), new Fraction(6, 1)));

        // Act
        String result = function.toString();

        // Assert
        Assert.assertEquals("f(x) = x^6", result);
    }
}
