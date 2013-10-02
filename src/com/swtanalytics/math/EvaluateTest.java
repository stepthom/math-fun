package com.swtanalytics.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class EvaluateTest {

	@Test
	public void testNormal() {
		Fraction coefficient = new Fraction(4, 1);
		Fraction exponent = new Fraction(3, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(10d);
		assertTrue(result == 4000d);
	}
	
	@Test
	public void testFractionalCoefficient() {
		Fraction coefficient = new Fraction(1, 2);
		Fraction exponent = new Fraction(3, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(10d);
		assertTrue(result == 500d);		
	}

	@Test
	public void testFractionalExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(1, 2);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(4d);
		assertTrue(result == 4d);		
	}
	
	@Test
	public void testZeroCoefficient() {
		Fraction coefficient = new Fraction(0, 1);
		Fraction exponent = new Fraction(2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(4d);
		assertTrue(result == 0d);		
	}

	@Test
	public void testZeroExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(0, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(2d);
		assertTrue(result == 2d);		
	}
	
	@Test
	public void testNegativeCoefficient() {
		Fraction coefficient = new Fraction(-2, 1);
		Fraction exponent = new Fraction(2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(2d);
		assertTrue(result == -8d);		
	}
	
	@Test
	public void testNegativeExponent() {
		Fraction coefficient = new Fraction(2, 1);
		Fraction exponent = new Fraction(-2, 1);
		
		Term t1 = new Term(coefficient, exponent);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		
		double result = mf.evaluate(2d);
		assertTrue(result == 0.5d);		
	}
	
	@Test
	public void testMultipleTerms() {
		Fraction c1 = new Fraction(2, 1);
		Fraction e1 = new Fraction(2, 1);
				
		Fraction c2 = new Fraction(4, 1);
		Fraction e2 = new Fraction(1, 1);
		
		Term t1 = new Term(c1, e1);
		Term t2 = new Term(c2, e2);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		mf.addTerm(t2);
		
		double result = mf.evaluate(3d);
		assertTrue(result == 30d);		
	}

	@Test
	public void testMultipleTermsWithNegativeSecondTerm() {
		Fraction c1 = new Fraction(2, 1);
		Fraction e1 = new Fraction(2, 1);
				
		Fraction c2 = new Fraction(-4, 1);
		Fraction e2 = new Fraction(1, 1);
		
		Term t1 = new Term(c1, e1);
		Term t2 = new Term(c2, e2);
		
		MathFunction mf = new MathFunction();
		mf.addTerm(t1);
		mf.addTerm(t2);
		
		double result = mf.evaluate(3d);
		assertTrue(result == 6d);		
	}
}
