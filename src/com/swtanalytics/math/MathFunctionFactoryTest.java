package com.swtanalytics.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class MathFunctionFactoryTest {
    private MathFunctionFactory factory;
    private TermFactoryFake termFactory;
    private RandomGeneratorFake randomGenerator;

    @Before
    public void before() {
        termFactory = new TermFactoryFake();
        randomGenerator = new RandomGeneratorFake();
        factory = new MathFunctionFactory(termFactory, randomGenerator);
    }

    @Test
    public void functionCreatedHasRandomNumberOfTerms() {
        // Arrange
        randomGenerator.generateIntResults.add(2);

        Fraction fraction = new Fraction(1, 1);
        Term term1 = new Term(fraction, fraction);
        Term term2 = new Term(fraction, fraction);
        termFactory.results.add(term1);
        termFactory.results.add(term2);

        // Act
        MathFunction result = factory.create(false, false);

        // Assert
        List<Term> terms = new ArrayList<Term>(result.terms);
        Assert.assertEquals(2, terms.size());
        Assert.assertSame(term1, terms.get(0));
        Assert.assertSame(term2, terms.get(1));
    }
}
