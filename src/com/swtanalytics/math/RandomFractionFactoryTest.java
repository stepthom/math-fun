package com.swtanalytics.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RandomFractionFactoryTest {
    private RandomGeneratorFake integerGenerator;
    private RandomFractionFactory fractionGenerator;

    @Before
    public void before() {
        integerGenerator = new RandomGeneratorFake();
        fractionGenerator = new RandomFractionFactory(integerGenerator);
    }

    @Test
    public void randomCoefficientRequestedIsNegative50Through49() {
        // Arrange
        integerGenerator.generateIntResults.add(10);

        // Act
        Fraction result = fractionGenerator.createCoefficient(true, .5);

        // Assert
        Assert.assertEquals(10, result.numerator.intValue());
        Assert.assertEquals(1, result.denominator.intValue());

        Assert.assertEquals(-50, integerGenerator.lastMinInclusiveArgument);
        Assert.assertEquals(49, integerGenerator.lastMaxInclusiveArgument);
    }

    @Test
    public void whenRandomGeneratorReturnsGreaterThanWholeProbabilityResultIsFractional() {
        // Arrange
        integerGenerator.generateDoubleResult = 1;
        integerGenerator.generateIntResults.add(1);
        integerGenerator.generateIntResults.add(2);

        // Act
        Fraction result = fractionGenerator.createCoefficient(false, .5);

        // Assert
        Assert.assertEquals(1, result.numerator.intValue());
        Assert.assertEquals(2, result.denominator.intValue());
    }

    @Test
    public void createExponentReturnsWholeNumberUpToMax() {
        // Arrange
        integerGenerator.generateIntResults.add(37);

        // Act
        Fraction result = fractionGenerator.createExponent(41);

        // Assert
        Assert.assertEquals(37, result.numerator.intValue());
        Assert.assertEquals(1, result.denominator.intValue());

        Assert.assertEquals(0, integerGenerator.lastMinInclusiveArgument);
        Assert.assertEquals(41, integerGenerator.lastMaxInclusiveArgument);
    }
}
