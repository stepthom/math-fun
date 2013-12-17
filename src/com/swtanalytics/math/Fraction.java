package com.swtanalytics.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Fraction implements Comparable<Fraction> {
    protected final BigInteger numerator;
    protected final BigInteger denominator;
    
    // -1, 0, or 1, depending on whether the Fraction is less than, equal to, or greater than zero,
    // respectively.
    protected final int sign_val;    
    
    protected final boolean isWholeFlag;

    private static final BigInteger bigZero = BigInteger.valueOf( 0 );
    private static final BigInteger bigOne = BigInteger.valueOf( 1 );
    private static final BigInteger bigTwo = BigInteger.valueOf( 2 );

    private static final BigInteger bigMinInt = BigInteger.valueOf( Integer.MIN_VALUE );
    private static final BigInteger bigMaxInt = BigInteger.valueOf( Integer.MAX_VALUE );

    private static final BigInteger bigMinLong = BigInteger.valueOf( Long.MIN_VALUE );
    private static final BigInteger bigMaxLong = BigInteger.valueOf( Long.MAX_VALUE );

    private static final BigDecimal bigMinFloat = BigDecimal.valueOf( Float.MIN_VALUE );
    private static final BigDecimal bigMaxFloat = BigDecimal.valueOf( Float.MAX_VALUE );

    private static final BigDecimal bigMinDouble = BigDecimal.valueOf( Double.MIN_VALUE );
    private static final BigDecimal bigMaxDouble = BigDecimal.valueOf( Double.MAX_VALUE );
    
    public Fraction(BigInteger n, BigInteger d) {
    	int nsign_val = n.compareTo( bigZero );
    	int dsign_val = d.compareTo( bigZero );
    	
    	this.sign_val = nsign_val * dsign_val;

    	if (dsign_val == 0) {
            throw new IllegalArgumentException("Argument 'd' is 0");
    	}

    	if (nsign_val == 0) {
    		this.numerator = bigZero;
    		this.denominator = bigOne;
    		this.isWholeFlag = true;
    	}
    	else {
	        BigInteger g = n.gcd( d );
	        BigInteger tempNumerator = n.divide( g );
	        BigInteger tempDenominator = d.divide( g );
	        
	        if (dsign_val == -1) {
				numerator = tempNumerator.negate();
				denominator = tempDenominator.negate();
	        }
	        else { // dsign_val == 1
				numerator = tempNumerator;
				denominator = tempDenominator;
	        }
	        
	        isWholeFlag = denominator.equals(bigOne);
    	}
    }
    
    public Fraction(Integer n, Integer d) {
    	this( BigInteger.valueOf(n), BigInteger.valueOf(d) );
    }
    
    public Fraction(Long n, Long d) {
    	this( BigInteger.valueOf(n), BigInteger.valueOf(d) );
    }

    public Fraction(Integer whole)
    {
    	this( BigInteger.valueOf( whole ) );
    }

    public Fraction(Long whole)
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
    	
    	if (this.sign_val == -1) {
    		result.append('-');
    	}
    	else if (! stripSignIfZeroOrGreater) {
    		result.append('+');
    	}
    	
    	if (spaceAfterCoefficientSign && (result.length() > 0)) {
    		result.append(' ');
    	}

    	result.append( numerator.abs().toString() );
    	
    	if (! isWholeFlag) {
    		result.append( '/' );
    		result.append( denominator.toString() );
    	}

        return result.toString();
    }

    public boolean isWhole() {
    	return this.isWholeFlag;
    }

    public BigInteger wholePart() {
        return numerator.divide( denominator );
    }
    
    private static boolean canBeInt( BigInteger val ) {
    	return (val.compareTo(bigMinInt) > 0) &&
     		   (val.compareTo(bigMaxInt) < 0);
    }
    
    private static boolean canBeLong( BigInteger val ) {
    	return (val.compareTo(bigMinLong) > 0) &&
     		   (val.compareTo(bigMaxLong) < 0);
    }
    
    private static boolean canBeFloat( BigDecimal val ) {
    	return ( val.compareTo(bigMinFloat) > 0) &&
      		   ( val.compareTo(bigMaxFloat) < 0);
    }
    
    private static boolean canBeDouble( BigDecimal val ) {
    	return ( val.compareTo(bigMinDouble) > 0) &&
      		   ( val.compareTo(bigMaxDouble) < 0);
    }
    
    /**
     * If this Fraction represents a whole number which fits in a Java @c int, this returns that
     * int.  Otherwise an exception is thrown.
     */
    public int intValue() {
    	if (! isWholeFlag) {
    		throw new ArithmeticException( "Can't cast a non-whole Fraction to a scalar type." );
    	}

    	if (! canBeInt(numerator)) {
    		throw new ArithmeticException( "Fraction's value lies outside the range supported by 'int'" );
    	}
    	
    	return numerator.intValue();
    }
    
    /**
     * If this Fraction represents a whole number which fits in a Java @c long, this returns that
     * long.  Otherwise an exception is thrown.
     */
    public long longValue() {
    	if (! isWholeFlag) {
    		throw new ArithmeticException( "Can't cast a non-whole Fraction to a scalar type." );
    	}

    	if (! canBeLong(numerator)) {
    		throw new ArithmeticException( "Fraction's value lies outside the range supported by 'long'" );
    	}
    	
    	return numerator.longValue();
    }
    
    /**
     * If this Fraction represents a whole number which fits in a Java @c float, this returns that
     * float.  Otherwise an exception is thrown.
     */
    public float floatValue(MathContext mc) {
    	BigDecimal ratio = bigDecimalValue( mc );
    	
    	if (! canBeFloat(ratio)) {
    		throw new ArithmeticException( "Fraction's value lies outside the range supported by 'float'" );
    	}
    	
    	return ratio.floatValue();
    }
    
    /**
     * If this Fraction represents a whole number which fits in a Java @c double, this returns that
     * double.  Otherwise an exception is thrown.
     */
    public double doubleValue(MathContext mc) {
    	BigDecimal ratio = bigDecimalValue( mc );
    	
    	if (! canBeDouble(ratio)) {
    		throw new ArithmeticException( "Fr	action's value lies outside the range supported by 'double'" );
    	}
    	
    	return ratio.doubleValue();
    }
    
    public BigDecimal bigDecimalValue( MathContext mc ) {
    	BigDecimal bdNum   = new BigDecimal( this.numerator,   0, mc );
    	BigDecimal bdDenom = new BigDecimal( this.denominator, 0, mc );
    	
    	return bdNum.divide( bdDenom, mc );
    }
    
    public int compareTo(Fraction f) {
    	// We can efficiently handle the cases where the two fractions are in separate
    	// regions of the number line...
    	if (this.sign_val < f.sign_val) {
    		return -1;
    	}
    	else if (this.sign_val > f.sign_val) {
    		return 1;
    	}
    	
    	if ((this.sign_val == 0) && (f.sign_val == 0)) {
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

    public Fraction divide(Fraction f) {
        return new Fraction(this.numerator  .multiply( f.denominator ), 
        					this.denominator.multiply( f.numerator) );
    }

    public Fraction negate() {
        return new Fraction(this.numerator.negate(), this.denominator );
    }

    public Fraction invert() {
    	if (this.sign_val == 0) {
    		throw new ArithmeticException( "Cannot invert a zero-value Fraction." );
    	}
    	
        return new Fraction(this.denominator, this.numerator );
    }
    
    public int sign() {
    	return this.sign_val;
    }
    
    public Fraction abs() {
    	if (sign_val == -1) {
    		return this.negate();
    	}
    	else {
    		return this;
    	}
    }
    
    public Fraction geometricAbs( Fraction basis ) {
    	return this.subtract( basis ).abs();
    }
    
    /**
     * @return The most-positive integer whose value is no greater than this Fraction.
     * For example, Fraction(11, 5) --> 2.  Fraction (-11, 5) --> -3. 
     */
    BigInteger floor() {
    	if (this.isWholeFlag) {
    		return numerator;
    	}
    	else {
    		if (this.sign_val == 1) {
    			return numerator.divide(denominator);
    		}
    		else { // this.sign_val == -1
    			return numerator.divide(denominator).subtract(bigOne);
    		}
    	}
    }
    
    /**
     * @return The least-positive integer whose value is no less than this Fraction.
     * For example, Fraction(11, 5) --> 3.  Fraction (-11, 5) --> -2. 
     */
    BigInteger ceil() {
    	if (this.isWholeFlag) {
    		return numerator;
    	}
    	else {
    		if (this.sign_val == 1) {
    			return numerator.divide(denominator).add(bigOne);
    		}
    		else { // this.sign_val == -1
    			return numerator.divide(denominator);
    		}
    	}
    }
    
    /**
     * @return The integer whose value is closest to this Fraction.
     * For Fractions whose non-integer component is exactly 0.5, this rounds away from zero.
     */
    BigInteger round() {
    	if (this.isWholeFlag) {
    		return numerator;
    	}
    	
    	BigInteger[] results = numerator.divideAndRemainder( denominator );
    	BigInteger quotient  = results[0];
    	BigInteger remainder = results[1];

    	// This is an integer version of checking if (abs(remainder) / denominator) >= 0.5.
    	boolean roundAwayFromZero = (remainder.abs().multiply( bigTwo ).compareTo( denominator ) >= 0);
    	
    	if (this.sign_val == -1) {
    		if (roundAwayFromZero) {
        		return quotient.subtract(bigOne);
    		}
    		else {
        		return quotient;
    		}
    	}
    	else { // this.sign_val == 1
    		if (roundAwayFromZero) {
        		return quotient.add(bigOne);
    		}
    		else {
        		return quotient;
    		}
    	}
    }
    
    BigDecimal pow( Fraction exponent, MathContext mc ) {
    	double base = this.doubleValue(mc);
    	double exp = exponent.doubleValue(mc);
    	double p = Math.pow( base, exp );
    	return BigDecimal.valueOf(p);
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
        	if (this.sign_val != f.sign_val) {
        		return false;
        	}
        	    	
        	// Every mathematically distinct fraction maps to a unique (numerator, denominator) pair,
        	// thanks to this class's constructors...
        	if (this.isWholeFlag && f.isWholeFlag) {
        		return this.numerator.equals( f.numerator  );
        	}
        	else if (this.isWholeFlag ^ f.isWholeFlag) {
        		return false;
        	}
        	else {
        		return this.numerator  .equals(f.numerator  ) && 
          			   this.denominator.equals(f.denominator);
        	}
    	}
    	else if (o instanceof Integer) {
    		if (! this.isWholeFlag) {
    			return false;
    		}
    		
    		if ((numerator.compareTo(bigMinInt) == -1) || (numerator.compareTo(bigMaxInt) == 1)) {
    			return false;
    		}
    		
    		Integer i = (Integer) o;
    	   	return numerator.intValue() == i.intValue();
    	}
    	else if (o instanceof Long) {
    		if (! this.isWholeFlag) {
    			return false;
    		}
    		
    		if ((numerator.compareTo(bigMinLong) == -1) || (numerator.compareTo(bigMaxLong) == 1)) {
    			return false;
    		}
    		
    		Long l = (Long) o;
    	   	return numerator.longValue() == l.longValue();
    	}    	
    	else if (o instanceof BigInteger) {
    		if (! this.isWholeFlag) {
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