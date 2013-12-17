package com.swtanalytics.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class IntegrationTest {

	@Test
	public void testNormal() {
		Fraction coefficient = new Fraction(4, 1);
		Fraction exponent = new Fraction(2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();

        List<Term> terms = new ArrayList<Term>(integral.getTerms());
        Fraction integralCoefficient = terms.get(0).coefficient;
		Fraction integralExponent = terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator.intValue() == 4);
		assertTrue(integralCoefficient.denominator.intValue() == 3);
		assertTrue(integralExponent.numerator.intValue() == 3);
		assertTrue(integralExponent.denominator.intValue() == 1);
	}

	@Test
	public void testZeroExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(0, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();

        List<Term> terms = new ArrayList<Term>(integral.getTerms());
		Fraction integralCoefficient = terms.get(0).coefficient;
		Fraction integralExponent = terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator.intValue() == 2);
		assertTrue(integralCoefficient.denominator.intValue() == 1);
		assertTrue(integralExponent.numerator.intValue() == 1);
		assertTrue(integralExponent.denominator.intValue() == 1);
	}

	@Test
	public void testFractionalCoefficient() {
		Fraction coefficient = new Fraction(1, 2);
		Fraction exponent = new Fraction(3, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();

        List<Term> terms = new ArrayList<Term>(integral.getTerms());
		Fraction integralCoefficient = terms.get(0).coefficient;
		Fraction integralExponent = terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator.intValue() == 1);
		assertTrue(integralCoefficient.denominator.intValue() == 8);
		assertTrue(integralExponent.numerator.intValue() == 4);
		assertTrue(integralExponent.denominator.intValue() == 1);
	}

	@Test
	public void testFractionalExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(1, 3);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();

        List<Term> terms = new ArrayList<Term>(integral.getTerms());
		Fraction integralCoefficient = terms.get(0).coefficient;
		Fraction integralExponent = terms.get(0).exponent;
				
		assertTrue(integralCoefficient.numerator.intValue() == 3);
		assertTrue(integralCoefficient.denominator.intValue() == 2);
		assertTrue(integralExponent.numerator.intValue() == 4);
		assertTrue(integralExponent.denominator.intValue() == 3);
	}
}
