package com.swtanalytics.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DifferentiateTest {

    private MathFunction function;
    private MathFunction expectedDerivative;

    public DifferentiateTest(Term[] functionTerms, Term[] expectedDerivativeTerms) {
        function = new MathFunction();
        for (Term term : functionTerms) {
            function.addTerm(term);
        }
        expectedDerivative = new MathFunction();
        for (Term term : expectedDerivativeTerms) {
            expectedDerivative.addTerm(term);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {
                        new Term[]{new Term(4, -1)},
                        new Term[]{new Term(-4, -2)},
                },
                {
                        new Term[]{new Term(new Fraction(1, 4), 0), new Term(4, -1)},
                        new Term[]{new Term(-4, -2)},
                },
                {
                        new Term[]{new Term(3, new Fraction(4, 9)), new Term(new Fraction(1, 4), 0), new Term(4, -1)},
                        new Term[]{new Term(new Fraction(12, 9), new Fraction(-5, 9)), new Term(-4, -2)},
                },
                {
                        new Term[]{new Term(2, 1), new Term(3, new Fraction(4, 9)), new Term(new Fraction(1, 4), 0), new Term(4, -1)},
                        new Term[]{new Term(2, 0), new Term(new Fraction(12, 9), new Fraction(-5, 9)), new Term(-4, -2)},
                },
                {
                        new Term[]{new Term(new Fraction(1, 3), 8), new Term(2, 1), new Term(3, new Fraction(4, 9)), new Term(new Fraction(1, 4), 0), new Term(4, -1)},
                        new Term[]{new Term(new Fraction(8, 3), 7), new Term(2, 0), new Term(new Fraction(12, 9), new Fraction(-5, 9)), new Term(-4, -2)},
                },
        });
    }

    @Test
    public void test() {
        assertEquals(new ArrayList<Term>(expectedDerivative.getTerms()), new ArrayList<Term>(function.differentiate().getTerms()));
    }
}
