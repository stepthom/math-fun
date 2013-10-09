package com.swtanalytics.math;

import org.ejml.data.Complex64F;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class PolynomialRootFinderTest {
    private PolynomialRootFinder rootFinder;

    @Before
    public void setUp() {
        rootFinder = new PolynomialRootFinder();
    }

    @Test
    public void constantFunctionThrows() {
        // Arrange
        // Represents the equation f(x) = 1
        double[] coefficients = new double[] { 1 };

        // Act
        try {
            rootFinder.findRoots(coefficients);
            Assert.fail();
        } catch(IllegalArgumentException ex) {
        }
    }

    @Test
    public void linearFunctionSolves() {
        // Arrange
        // Represents the equation f(x) = x
        double[] coefficients = new double[] { 0, 1 };

        // Act
        Complex64F[] roots = rootFinder.findRoots(coefficients);

        // Assert
        List<Double> realResults = getRealResults(roots);
        Assert.assertEquals(1, realResults.size());
        Assert.assertEquals(0, realResults.get(0), 1e-10);
    }

    @Test
    public void quadraticFunctionSolves() {
        // Arrange
        // Represents the equation f(x) = x^2 - 3x + 2
        double[] coefficients = new double[] { 2, -3, 1 };

        // Act
        Complex64F[] roots = rootFinder.findRoots(coefficients);

        // Assert
        List<Double> realResults = getRealResults(roots);
        Assert.assertEquals(2, realResults.size());
        Assert.assertEquals(1, realResults.get(0), 1e-10);
        Assert.assertEquals(2, realResults.get(1), 1e-10);
    }

    @Test
    public void higherOrderEquationSolves() {
        // Arrange
        // Represents the equation f(x) = x^4 - 10x^3 + 35x^2 - 50x + 24
        //                              = (x - 1)(x - 2)(x - 3)(x - 4)
        double[] coefficients = new double[] { 24, -50, 35, -10, 1 };

        // Act
        Complex64F[] roots = rootFinder.findRoots(coefficients);

        // Assert
        List<Double> realResults = getRealResults(roots);
        Assert.assertEquals(4, realResults.size());
        Assert.assertEquals(1, realResults.get(0), 1e-10);
        Assert.assertEquals(2, realResults.get(1), 1e-10);
        Assert.assertEquals(3, realResults.get(2), 1e-10);
        Assert.assertEquals(4, realResults.get(3), 1e-10);
    }

    private List<Double> getRealResults(Complex64F[] roots) {
        List<Double> result = new ArrayList<Double>();

        for (Complex64F root : roots) {
            if (root.isReal()) {
                result.add(root.getReal());
            }
        }

        Collections.sort(result);
        return result;
    }
}
