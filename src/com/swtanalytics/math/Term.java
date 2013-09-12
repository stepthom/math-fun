package com.swtanalytics.math;

public class Term {
	
	protected int coefficient;
	protected int exponent;
	
	public Term (int c, int e){
		this.coefficient = c;
		this.exponent = e;
	}
	
	public String toString(){
		return String.format("%+dx^%d", this.coefficient, this.exponent);
	}

}
