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

    @Before
    public void before() {
    }

    @Test
    public void functionCreatedHasRandomNumberOfTerms() {
        RandomGeneratorFake randomGenerator = new RandomGeneratorFake();
        TermFactoryFake termFactory = new TermFactoryFake();
        MathFunctionFactory factory = new MathFunctionFactory(termFactory, randomGenerator);

        // Arrange
        randomGenerator.generateIntResults.add(2);

        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(2, 1);
        Term term1 = new Term(fraction1, fraction1);
        Term term2 = new Term(fraction2, fraction2);
        termFactory.results.add(term1);
        termFactory.results.add(term2);

        // Act
        MathFunction result = factory.create(false, false);

        // Assert
        List<Term> terms = new ArrayList<Term>(result.getTerms());
        Assert.assertEquals(2, terms.size());
        Assert.assertSame(term2, terms.get(0));
        Assert.assertSame(term1, terms.get(1));
    }

    @Test
    public void forceLinearFunctionCreatesStrictlyLinearFunction() {
        RandomGenerator randomGenerator = new SeededRandomGenerator(31337);
        FractionFactory fractionFactory = new RandomFractionFactory(randomGenerator);
        TermFactory termFactory = new RandomTermFactory(fractionFactory);
        MathFunctionFactory factory = new MathFunctionFactory(termFactory, randomGenerator);

        for (int i = 0; i < 1000; ++i)
        {
            MathFunction function = factory.create(false, true);
            Assert.assertTrue(function.isLinearFunction());
            Assert.assertEquals(new Fraction(1, 1), function.degree());
        }
    }
}
