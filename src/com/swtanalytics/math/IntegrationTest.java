package com.swtanalytics.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest {

	@Test
	public void testNormal() {
		Fraction coefficient = new Fraction(4, 1);
		Fraction exponent = new Fraction(2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();
		Fraction integralCoefficient = integral.terms.get(0).coefficient;
		Fraction integralExponent = integral.terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator == 4);
		assertTrue(integralCoefficient.denominator == 3);
		assertTrue(integralExponent.numerator == 3);
		assertTrue(integralExponent.denominator == 1);
	}

	@Test
	public void testZeroCoefficient() {
		Fraction coefficient = new Fraction(0, 1);
		Fraction exponent = new Fraction(2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();
		Fraction integralCoefficient = integral.terms.get(0).coefficient;
		Fraction integralExponent = integral.terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator == 0);
		assertTrue(integralCoefficient.denominator == 3);
		assertTrue(integralExponent.numerator == 3);
		assertTrue(integralExponent.denominator == 1);
	}

	@Test
	public void testZeroExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(0, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();
		Fraction integralCoefficient = integral.terms.get(0).coefficient;
		Fraction integralExponent = integral.terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator == 2);
		assertTrue(integralCoefficient.denominator == 1);
		assertTrue(integralExponent.numerator == 1);
		assertTrue(integralExponent.denominator == 1);
	}

	@Test
	public void testFractionalCoefficient() {
		Fraction coefficient = new Fraction(1, 2);
		Fraction exponent = new Fraction(3, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();
		Fraction integralCoefficient = integral.terms.get(0).coefficient;
		Fraction integralExponent = integral.terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator == 1);
		assertTrue(integralCoefficient.denominator == 8);
		assertTrue(integralExponent.numerator == 4);
		assertTrue(integralExponent.denominator == 1);
	}

	@Test
	public void testFractionalExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(1, 3);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction function = new MathFunction();
		function.addTerm(t1);
		
		MathFunction integral = function.integrate();
		Fraction integralCoefficient = integral.terms.get(0).coefficient;
		Fraction integralExponent = integral.terms.get(0).exponent;
		
		assertTrue(integralCoefficient.numerator == 3);
		assertTrue(integralCoefficient.denominator == 2);
		assertTrue(integralExponent.numerator == 4);
		assertTrue(integralExponent.denominator == 3);
	}
}
