package com.swtanalytics.math;

import java.util.ArrayList;
import java.util.Collections;

public class MathFunction {

	protected ArrayList<Term> terms = new ArrayList<Term>();
	
	public MathFunction(){
		
	}
	
	public void addTerm(Term t){
		terms.add(t);
        Collections.sort(this.terms);
	}
	
	
	public String toString(){
		String result = "f(x) = ";
        boolean first_term = true;
		for (Term t: terms){

			result += (t.prettyPrint(first_term) + " ");
            first_term = false;
		}
		return result;
	}
	
	
}
