package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SolveTest {

    private static final double EPSILON = 1.0E-10;
    private Term[] terms;
    private double[] expected;

    public SolveTest(Term[] terms, double[] expected) {
        this.terms = terms;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection testCases() {
        return Arrays.asList(new Object[][]{
                {new Term[]{new Term(42, 0)}, new double[]{}},
                {new Term[]{new Term(0, 0)}, new double[]{}},
                {new Term[]{new Term(1, 1)}, new double[]{0}},
                {new Term[]{new Term(1, 2)}, new double[]{0}},
                {new Term[]{new Term(1, 3)}, new double[]{0}},
                {new Term[]{new Term(1, 1), new Term(5, 0)}, new double[]{-5}},
                {new Term[]{new Term(new Fraction(1, 2), 1), new Term(-10, 0)}, new double[]{20}},
                {new Term[]{new Term(1, 3), new Term(-1, 1)}, new double[]{-1, 0, 1}},
                {new Term[]{new Term(1, 4), new Term(-1, 2)}, new double[]{-1, 0, 1}},
                {new Term[]{new Term(1, 3), new Term(-1, 1), new Term(6, 0)}, new double[]{-2}},
                {new Term[]{new Term(1, 2), new Term(-5, 0)}, new double[]{-Math.sqrt(5), Math.sqrt(5)}},
                {new Term[]{new Term(1, 2), new Term(5, 0)}, new double[]{}},
                {new Term[]{new Term(1, 2), new Term(4, 1), new Term(-3, 0)}, new double[]{-2 - Math.sqrt(7), -2 + Math.sqrt(7)}},
                {new Term[]{new Term(-50, 5), new Term(4, 4), new Term(12, 3)}, new double[]{(1 - Math.sqrt(151)) / 25, 0, (1 + Math.sqrt(151)) / 25}},
                {new Term[]{new Term(-3, 6), new Term(7, 4)}, new double[]{-Math.sqrt(7.0/3), 0, Math.sqrt(7.0/3)}},
                {new Term[]{new Term(-41, 5)}, new double[]{0}},
                {new Term[]{new Term(-9, 5), new Term(27, 4), new Term(35, 2)}, new double[]{0, 3.34712220576668}},
                {new Term[]{new Term(-22, 9), new Term(-44, 8), new Term(-42, 5), new Term(26, 0)}, new double[]{0.80951814578781030844}},
                {new Term[]{new Term(1, 6), new Term(-6, 5), new Term(15, 4), new Term(-20, 3), new Term(15, 2), new Term(-6, 1), new Term(1, 0)}, new double[]{1}},
                {new Term[]{new Term(10, 10), new Term(34, 6), new Term(-44, 5), new Term(-20, 1), new Term(18, 0)}, new double[]{0.72573680828145349032, 1.0255529554167955547}},
                {new Term[]{new Term(4, 7), new Term(-30, 5), new Term(-16, 3), new Term(32, 2)}, new double[]{-2.8835295885675711490, 0, 0.87437087422201568461, 2.7649146586482788272}},
                {new Term[]{new Term(24, 9), new Term(-46, 5), new Term(-8, 1), new Term(-8, 0)}, new double[]{-1.1806289846042322777, -0.59601270428208455010, 1.2169827625444084391}},
        });
    }

    @Test
    public void test() {
        MathFunction function = new MathFunction();
        for (Term term : terms) {
            function.addTerm(term);
        }
        System.out.println(function.toString());
        List<Double> solutionsList = function.solve( MathContext.DECIMAL128 );
        System.out.println(solutionsList.toString());

        // Java is really annoying sometimes :-P
        double[] solutions = new double[solutionsList.size()];
        for (int i = 0; i < solutionsList.size(); i++) {
            solutions[i] = solutionsList.get(i);
        }

        assertArrayEquals(expected, solutions, EPSILON);
    }
}
