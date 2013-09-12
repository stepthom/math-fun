package com.swtanalytics.math;

import java.util.ArrayList;

public class MathFunction {

	protected ArrayList<Term> terms = new ArrayList<Term>();
	
	public MathFunction(){
		
	}
	
	public void addTerm(Term t){
		terms.add(t);
	}
	
	
	public String toString(){
		String result = "f(x) = ";
		for (Term t: terms){
			result += (t + " ");
		}
		return result;
	}
	
	
}
