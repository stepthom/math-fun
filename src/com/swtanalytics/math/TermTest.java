package com.swtanalytics.math;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TermTest {
    @Test
    public void whenCoefficientIsNonZeroPrettyPrintIsCoefficientAndVariableParts() {
        // Arrange
        Fraction coefficient = new Fraction(2, 1);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(true);

        // Assert
        Assert.assertEquals("2x^2", result);
    }

    @Test
    public void whenCoefficientIsNegativePrettyPrintHasLeadingNegativeSign() {
        // Arrange
        Fraction coefficient = new Fraction(-2, 1);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(true);

        // Assert
        Assert.assertEquals("-2x^2", result);
    }

    @Test
    public void whenCoefficientIsZeroPrettyPrintIsEmpty() {
        // Arrange
        Fraction coefficient = new Fraction(0, 1);
        Fraction exponent = new Fraction(1, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(true);

        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void whenCoefficientIsOnePrettyPrintIsVariablePartOnly() {
        // Arrange
        Fraction coefficient = new Fraction(1, 1);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(true);

        // Assert
        Assert.assertEquals("x^2", result);
    }

    @Test
    public void whenCoefficientIsOneAndIsNotFirstTermPrettyPrintIsVariablePartOnly() {
        // Arrange
        Fraction coefficient = new Fraction(1, 1);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(false);

        // Assert
        Assert.assertEquals("x^2", result);
    }

    @Test
    public void whenCoefficientIsZeroAndIsNotFirstTermPrettyPrintReturnsEmptyString() {
        // Arrange
        Fraction coefficient = new Fraction(0, 1);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(false);

        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void whenCoefficientReducesToOnePrettyPrintIsVariablePartOnly() {
        // Arrange
        Fraction coefficient = new Fraction(37, 37);
        Fraction exponent = new Fraction(2, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.formatString(true);

        // Assert
        Assert.assertEquals("x^2", result);
    }

    @Test
    public void whenExponentIsZeroPrettyPrintHasNoVariablePart() {
        // Arrange
        Fraction coefficient = new Fraction(77, 1);
        Fraction exponent = new Fraction(0, 1);
        Term term = new Term(coefficient, exponent);

        // Act
        String result = term.prettyPrint(true);

        // Assert
        Assert.assertEquals("77", result);
    }

    @Test
    public void addTerms() {
        // Arrange
        Fraction coefficient1 = new Fraction(1, 1);
        Fraction coefficient2 = new Fraction(2, 1);
        Fraction exponent = new Fraction(1, 1);
        Term term1 = new Term(coefficient1, exponent);
        Term term2 = new Term(coefficient2, exponent);

        // Act
        Term result = term1.add(term2);

        // Assert
        Assert.assertEquals(3, result.coefficient.numerator.intValue());
        Assert.assertEquals(1, result.coefficient.denominator.intValue());
        Assert.assertEquals(1, result.exponent.numerator.intValue());
        Assert.assertEquals(1, result.exponent.denominator.intValue());
    }
}