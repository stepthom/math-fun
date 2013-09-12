package com.swtanalytics.math;

import java.io.IOException;

public class EasyMain {
	
	public static void main(String[] args) throws IOException {

		// TODO: get input
		int numMathFunctions = 10;
		
		for (int i=0; i<numMathFunctions;++i){
			MathFunction mf = new MathFunction();
			int numTerms = 1 + (int)(Math.random() * 5);
			
			for (int j = 0; j<numTerms; ++j){
				int coefficient = (int)(Math.random() * 100) - 50;
				int exponent = (int)(Math.random() * 100);
				Term t = new Term(coefficient, exponent);
				mf.addTerm(t);
			}
			
			System.out.printf("Function %d:\n", i);
			System.out.print(mf);
			System.out.print("\n");
		}
	}
}
