package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EvaluateTest {

    private static final double EPSILON = 1.0E-10;
    private Term[] terms;
    private double x;
    private double expected;

    public EvaluateTest(Term[] terms, double x, double expected) {
        this.terms = terms;
        this.x = x;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection testCases() {
        return Arrays.asList(new Object[][]{
                {new Term[]{new Term(4, 3)}, 10, 4000},
                {new Term[]{new Term(new Fraction(1, 2), 3)}, 10, 500},
                {new Term[]{new Term(2, new Fraction(1, 2))}, 4, 4},
                {new Term[]{new Term(2, new Fraction(1, 2))}, 0, 0},
                {new Term[]{new Term(2, new Fraction(1, 2))}, -4, Double.NaN},
                {new Term[]{new Term(0, 2)}, 4, 0},
                {new Term[]{new Term(2, 0)}, 2, 2},
                {new Term[]{new Term(-2, 2)}, 2, -8},
                {new Term[]{new Term(2, -2)}, 2, 0.5},
                {new Term[]{new Term(2, 2), new Term(4, 1)}, 3, 30},
                {new Term[]{new Term(2, 2), new Term(-4, 1)}, 3, 6},
                {new Term[]{new Term(1, 2)}, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(1, 2)}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(1, 3)}, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(1, 3)}, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                {new Term[]{new Term(1, new Fraction(1, 2))}, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {new Term[]{new Term(1, new Fraction(1, 2))}, Double.NEGATIVE_INFINITY, Double.NaN},
                {new Term[]{new Term(42, 0)}, Double.POSITIVE_INFINITY, 42},
                {new Term[]{new Term(42, 0)}, Double.NEGATIVE_INFINITY, 42},
                {new Term[]{new Term(1, -1)}, Double.POSITIVE_INFINITY, 0},
                {new Term[]{new Term(1, -1)}, Double.NEGATIVE_INFINITY, 0},
                {new Term[]{new Term(1, -2)}, Double.POSITIVE_INFINITY, 0},
                {new Term[]{new Term(1, -2)}, Double.NEGATIVE_INFINITY, 0},
                {new Term[]{new Term(1, new Fraction(-1, 2))}, Double.POSITIVE_INFINITY, 0},
                {new Term[]{new Term(1, new Fraction(-1, 2))}, Double.NEGATIVE_INFINITY, Double.NaN},
        });
    }

    @Test
    public void test() {
        MathFunction function = new MathFunction();
        for (Term term : terms) {
            function.addTerm(term);
        }
        System.out.println(function.toString());
        double result = function.evaluate(x);
        System.out.format("f(%f) = %f%n", x, result);
        assertEquals(expected, result, EPSILON);
    }
}
