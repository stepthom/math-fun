package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class FindMinMaxTest {

    private static final double EPSILON = 1.0E-10;
    private Term[] terms;
    private double domainMin;
    private double domainMax;
    private double expectedMin;
    private double expectedMax;

    public FindMinMaxTest(Term[] terms, double domainMin, double domainMax, double expectedMin, double expectedMax) {
        this.terms = terms;
        this.domainMin = domainMin;
        this.domainMax = domainMax;
        this.expectedMin = expectedMin;
        this.expectedMax = expectedMax;
    }

    @Parameterized.Parameters
    public static Collection testCases() {
        return Arrays.asList(new Object[][]{
                {new Term[]{new Term(42, 0)}, -50, 50, -50, -50},
                {new Term[]{new Term(1, 1)}, -50, 50, -50, 50},
                {new Term[]{new Term(1, 1)}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(-1, 1)}, -50, 50, 50, -50},
                {new Term[]{new Term(-1, 1)}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY},
                {new Term[]{new Term(1, 2), new Term(1, 1)}, -50, 50, -0.5, 50},
                {new Term[]{new Term(1, 2), new Term(1, 1)}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -0.5, Double.NEGATIVE_INFINITY},
                {new Term[]{new Term(1, 2), new Term(1, 1)}, 0, 50, 0, 50},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, -50, 50, -50, 50},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, -2, 2, -2, 2},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, -1.1, 1.1, 1 / Math.sqrt(3), -1 / Math.sqrt(3)},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, -1, 1, 1 / Math.sqrt(3), -1 / Math.sqrt(3)},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, 0, 1, 1 / Math.sqrt(3), 0},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, 0, 2, 1 / Math.sqrt(3), 2},
        });
    }

    @Test
    public void test() {
        MathFunction function = new MathFunction();
        for (Term term : terms) {
            function.addTerm(term);
        }
        System.out.println(function.toString());

        assertEquals(expectedMin, function.findMinimum(domainMin, domainMax), EPSILON);
        assertEquals(expectedMax, function.findMaximum(domainMin, domainMax), EPSILON);
    }
}
