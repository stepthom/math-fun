package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        });
    }

    @Test
    public void test() {
        MathFunction function = new MathFunction();
        for (Term term : terms) {
            function.addTerm(term);
        }
        System.out.println(function.toString());
        List<Double> solutionsList = function.solve();

        // Java is really annoying sometimes :-P
        double[] solutions = new double[solutionsList.size()];
        for (int i = 0; i < solutionsList.size(); i++) {
            solutions[i] = solutionsList.get(i);
        }

        assertArrayEquals(expected, solutions, EPSILON);
    }
}
