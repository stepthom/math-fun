package com.swtanalytics.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Fraction implements Comparable<Fraction> {
    protected final BigInteger numerator;
    protected final BigInteger denominator;
    
    // -1, 0, or 1, depending on whether the Fraction is less than, equal to, or greater than zero,
    // respectively.
    protected final int sign;    
    protected final boolean isWhole;

    protected static final BigInteger bigZero   = BigInteger.valueOf(  0 );
    protected static final BigInteger bigOne    = BigInteger.valueOf(  1 );

	private static final BigInteger bigMinInt = BigInteger.valueOf( Integer.MIN_VALUE );
	private static final BigInteger bigMaxInt = BigInteger.valueOf( Integer.MAX_VALUE );
	private static final BigInteger bigMinLong = BigInteger.valueOf( Long.MIN_VALUE );
	private static final BigInteger bigMaxLong = BigInteger.valueOf( Long.MAX_VALUE );

    
    public Fraction(BigInteger n, BigInteger d) {
    	int nSign = n.compareTo( bigZero );
    	int dSign = d.compareTo( bigZero );
    	
    	this.sign = nSign * dSign;

    	if (dSign == 0) {
            throw new IllegalArgumentException("Argument 'd' is 0");
    	}

    	if (nSign == 0) {
    		this.numerator = bigZero;
    		this.denominator = bigOne;
    		this.isWhole = true;
    	}
    	else {
	        BigInteger g = n.gcd( d );
	        BigInteger tempNumerator = n.divide( g );
	        BigInteger tempDenominator = d.divide( g );
	        
	        if (dSign == -1) {
				numerator = tempNumerator.negate();
				denominator = tempDenominator.negate();
	        }
	        else { // dSign == 1
				numerator = tempNumerator;
				denominator = tempDenominator;
	        }
	        
	        isWhole = denominator.equals(bigOne);
    	}
    }
    
    public Fraction(int n, int d) {
    	this( BigInteger.valueOf(n), BigInteger.valueOf(d) );
    }

    public Fraction(int whole)
    {
    	this( BigInteger.valueOf( whole ) );
    }

    public Fraction(BigInteger whole)
    {
    	this( whole, bigOne );
    }

    public String toString() {
    	return formatString( false, false );
    }

    public String formatString(boolean stripSignIfZeroOrGreater, boolean spaceAfterCoefficientSign) {
    	StringBuilder result = new StringBuilder();
    	
    	if (this.sign == -1) {
    		result.append('-');
    	}
    	else if (! stripSignIfZeroOrGreater) {
    		result.append('+');
    	}
    	
    	if (spaceAfterCoefficientSign && (result.length() > 0)) {
    		result.append(' ');
    	}

    	result.append( numerator.abs().toString() );
    	
    	if (! isWhole()) {
    		result.append( '/' );
    		result.append( denominator.toString() );
    	}

        return result.toString();
    }

    public boolean isWhole() {
    	return denominator.equals( bigOne );
    }

    public BigInteger wholePart() {
        return numerator.divide( denominator );
    }

    /**
     * @param mc Used in intermediate calculations to compute the return value.
     */
    public BigDecimal bigDecimalValue( MathContext mc ) {
    	BigDecimal bdNum   = new BigDecimal( this.numerator,   0, mc );
    	BigDecimal bdDenom = new BigDecimal( this.denominator, 0, mc );
    	
    	return bdNum.divide( bdDenom, mc );
    }
    
    public int compareTo(Fraction f) {
    	// We can efficiently handle the cases where the two fractions are in separate
    	// regions of the number line...
    	if (this.sign < f.sign) {
    		return -1;
    	}
    	else if (this.sign > f.sign) {
    		return 1;
    	}
    	
    	if ((this.sign == 0) && (f.sign == 0)) {
    		return 0;
    	}
    	
    	// If we got here, then both fractions are negative or both are positive.  We can multiply 
    	// both 'this' and 'f' by (this.denominator * f.denominator), for the following reasons:
    	// - Because this class ensures that denominators are positive, that multiplication won't
    	//   affect the sign of either fraction.
    	// - Because we're multiplying both fractions by the same value, the new fractions will have
    	//   the same relationship (less-than or greater-than) as the fractions on which they're 
    	//   based.
    	//
    	// Both of these new fractions will have a denominator of '1', meaning we can get our final 
    	// answer by simply comparing the new fractions' numerators.  Thus we avoid a potentially 
    	// slow and error-introducing floating-point conversion...
    	BigInteger a = this.numerator.multiply( f   .denominator );
    	BigInteger b = f   .numerator.multiply( this.denominator );
    	return a.compareTo( b );
    }

    public Fraction add(Fraction f) {
    	BigInteger n = this.numerator.multiply( f   .denominator ).add(
    			       f   .numerator.multiply( this.denominator ));
    	
    	BigInteger d = this.denominator.multiply( f.denominator );
    	
        return new Fraction(n, d);
    }

    public Fraction subtract(Fraction f) {
    	BigInteger n = this.numerator.multiply( f   .denominator ).subtract(
			           f   .numerator.multiply( this.denominator ));
	
    	BigInteger d = this.denominator.multiply( f.denominator );
	
    	return new Fraction(n, d);
    }

    public Fraction multiply(Fraction f) {
        return new Fraction(this.numerator  .multiply( f.numerator ), 
        					this.denominator.multiply( f.denominator) );
    }

    /**
     * @param o The object against which this is compared.  Valid types for @c o are @c Fraction,
     * @c Integer, @c Long, and @c BigInteger.  For the types other than @c Fraction, the provided  
     * scalar value is taken to be the value of a whole number against which this Fraction is
     * compared.  
     */
    @Override
    public boolean equals(Object o) {
    	if (this == o) {
    		return true;
    	}
    	else if (o instanceof Fraction) {
        	// Try the cheap comparisons first...
        	Fraction f = (Fraction) o;
        	if (this.sign != f.sign) {
        		return false;
        	}
        	    	
        	// Every mathematically distinct fraction maps to a unique (numerator, denominator) pair,
        	// thanks to this class's constructors...
        	if (this.isWhole && f.isWhole) {
        		return this.numerator.equals( f.numerator  );
        	}
        	else if (this.isWhole ^ f.isWhole) {
        		return false;
        	}
        	else {
        		return this.numerator  .equals(f.numerator  ) && 
          			   this.denominator.equals(f.denominator);
        	}
    	}
    	else if (o instanceof Integer) {
    		if (! this.isWhole) {
    			return false;
    		}
    		
    		if ((numerator.compareTo(bigMinInt) == -1) || (numerator.compareTo(bigMaxInt) == 1)) {
    			return false;
    		}
    		
    		Integer i = (Integer) o;
    	   	return numerator.intValue() == i.intValue();
    	}
    	else if (o instanceof Long) {
    		if (! this.isWhole) {
    			return false;
    		}
    		
    		if ((numerator.compareTo(bigMinLong) == -1) || (numerator.compareTo(bigMaxLong) == 1)) {
    			return false;
    		}
    		
    		Long l = (Long) o;
    	   	return numerator.longValue() == l.longValue();
    	}    	
    	else if (o instanceof BigInteger) {
    		if (! this.isWhole) {
    			return false;
    		}
    		
    		BigInteger bi = (BigInteger) o;
    	   	return numerator.equals( bi );
    	}
    	
    	return false;
    }

    @Override
    public int hashCode() {
    	// Every mathematically distinct Fraction maps to a unique (numerator, denominator) pair,
    	// thanks to this class's constructors.  So there are no concerns about equivalent Fractions
    	// getting distinct hashcodes using the following formula.
    	//
    	// NOTE: This hashing algorithm is not carefully tuned.
    	return numerator.hashCode() + denominator.hashCode();
    }
}