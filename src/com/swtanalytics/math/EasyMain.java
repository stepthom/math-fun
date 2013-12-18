package com.swtanalytics.math;

import java.io.IOException;
import java.math.MathContext;
import java.util.Vector;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

public class EasyMain {
    public static final int NUM_MATH_FUNCTIONS_DEFAULT = 10;
    private final MathFunctionFactory functionFactory;

    public EasyMain() {
        RandomGenerator randomGenerator = new RandomGeneratorUsingMathRandom();
        FractionFactory fractionFactory = new RandomFractionFactory(randomGenerator);
        TermFactory termFactory = new RandomTermFactory(fractionFactory);
        functionFactory = new MathFunctionFactory(termFactory, randomGenerator);
    }

    protected enum FunctionType
    {
    	NORMAL,
    	DIFFERENTIAL,
    	INTEGRAL
    }
    
    protected enum FunctionGenerationMode
    {
    	RANDOM,
    	FIT_POINTS
    }
    
    private FunctionGenerationMode genMode = null;
    
    // Only meaningful when genMode == FIT_POINTS.  This is a sequence of the form 
    // x_0, y_0 [, x_1, y_1, ...]
    Vector<Fraction> rawFitPointFractions; 

    @Option(name="-n", usage="The number of functions to generate.")
    public int numMathFunctions = NUM_MATH_FUNCTIONS_DEFAULT;
    
    @Option(name = "-points", 
    		usage="Generate only one function, which fits a specified set of points.\n" +
    		       "This argument uses the form:\n" + 
    				"   -points <x-frac1> <y-frac1> <x-frac2> <y-frac2> ...\n" +
    			    "Each fraction has the form:\n" +
    		        "   ['+'|'~']numerator['/'denominator]\n" + 
    			    "where numerator and denominator are non-negative integers.  " +
    		        "'~', not '-', indicates negation in this context.  " +
    			    "At least two points must be specified.",
    	    handler=StringArrayOptionHandler.class )
    public String[] fitPointStrings;

    @Option(name="-d", usage="Print differentials too.")
    public boolean isPrintDifferential = false;

    @Option(name="-f", usage="Experiment with Fractions.")
    public boolean isFractions = false;

    @Option(name="-i", usage="Print integrals too.")
    public boolean isPrintIntegral = false;

    @Option(name="-l", usage="Create only strictly first-degree linear functions.")
    public boolean forceLinearFunctions = false;

    @Option(name = "--domain-min", usage = "Domain minimum, for min/max calculations (defaults to -infinity)")
    public double domainMin = Double.NEGATIVE_INFINITY;

    @Option(name = "--domain-max", usage = "Domain maximum, for min/max calculations (defaults to +infinity)")
    public double domainMax = Double.POSITIVE_INFINITY;

    protected void parse_input(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            // validate the input a bit.
            // There's probably a nicer way to do this with args4j
            
            if (this.numMathFunctions < 0) {
            	throw new CmdLineException(parser, "Option -n requires a non-negative integer");
            }
            
            // Issue #52 specifies that doing curve-fitting is mutually exclusive with generating a random set
            // of functions.  There's probably a nicer way to get args4j to report / enforce the rule that 
            // the command lines allows [-n | -points], but that's an enhancement we can look into later if
            // we want to.  For now we'll just sniff for the default values being in effect...
            if (this.numMathFunctions == 0) {
                if (fitPointStrings.length == 0) {
                    throw new CmdLineException(parser, 
                    		"Must have either -n with a positive integer, or specify a non-empty " +
                    		"string for the -points option.");
                }
                
                if ((fitPointStrings.length % 2) != 0) {
                    throw new CmdLineException(parser, 
                    		"-points must be followed by an even number of Fractions, because " +
                    		"they come in (X_i, Y_i) pairs." );
                }

                rawFitPointFractions = new Vector<Fraction>(fitPointStrings.length);
                
                for ( String p : fitPointStrings ) {
                	Fraction f = new Fraction(p);
                	rawFitPointFractions.add(f);
                }
                
                genMode = FunctionGenerationMode.FIT_POINTS;
            }
            else {
            	if (fitPointStrings.length > 0) {
                    throw new CmdLineException(parser, 
                    		"If using the -fit option, one must also use the '-n 0' option.");
                }
            	
            	genMode = FunctionGenerationMode.RANDOM;
            }
        } catch (CmdLineException e) {
            System.out.print(e.getMessage());
            System.out.print("\n");
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    protected String getFunctionString(MathFunction mf, int i, FunctionType type) {
    	StringBuilder functionString = new StringBuilder();
    	
    	// Use indentation appropriate for printing an xml rendition of the output.
    	functionString.append("      ").append(mf);

        functionString.append("\n");

        return functionString.toString();
    }

    public void printFunction(MathFunction mf, int i, MathContext mc) {
    	System.out.println("  <function>");

    	printFunction(mf, i, FunctionType.NORMAL);

        if (this.isPrintDifferential) {
            printFunction(mf.differentiate(), i, FunctionType.DIFFERENTIAL);
        }

        if (this.isPrintIntegral) {
        	printFunction(mf.integrate(), i, FunctionType.INTEGRAL);
        }

        if (mf.isLinearFunction()) {
        	printSlope(mf.computeSlope());
        }
        
        printMinMax(mf);
        
        printIntercepts(mf, mc);

    	System.out.println("  </function>");
    }

    private void printSlope(Fraction slope) {
    	System.out.println("    <slope>");
    	System.out.print("      ");
    	System.out.println(slope.formatString(true, false));
    	System.out.println("    </slope>");
    }

    private void printMinMax(MathFunction function) {
    	MathContext mc = MathContext.DECIMAL128;
    	
        try {
            double minX = function.findMinimum(domainMin, domainMax, mc);
            double minY = function.evaluate(minX, mc);
            if (!Double.isNaN(minX) && !Double.isNaN(minY)) {
                System.out.format("    <min>(%f, %f)</min>%n", minX, minY);
            }
        } catch (UnsupportedOperationException e) {
        }

        try {
            double maxX = function.findMaximum(domainMin, domainMax, mc);
            double maxY = function.evaluate(maxX, mc);
            if (!Double.isNaN(maxX) && !Double.isNaN(maxY)) {
                System.out.format("    <max>(%f, %f)</max>%n", maxX, maxY);
            }
        } catch (UnsupportedOperationException e) {
        }
    }

    private void printIntercepts(MathFunction function, MathContext mc) {
        System.out.format("    <y-intercept>%f</y-intercept>%n", function.evaluate(0, mc));

        try {
            for (double solution : function.solve(mc))
            {
                System.out.format("    <x-intercept>%f</x-intercept>%n", solution);
            }
        } catch (UnsupportedOperationException e) {
        }
    }

    protected void printFunction(MathFunction mf, int i, FunctionType type) {
    	StringBuilder outputString = new StringBuilder();
    	
    	// Decorate with xml
    	switch (type) {
    	case NORMAL:
    		outputString.append("    <output>\n");
    		break;
    	case DIFFERENTIAL:
    		outputString.append("    <derivative>\n");
    		break;
    	case INTEGRAL:
    		outputString.append("    <integral>\n");
    		break;
    	}
    	
    	outputString.append(getFunctionString(mf, i, type));

    	// Decorate with xml
    	switch (type) {
    	case NORMAL:
    		outputString.append("    </output>\n");
    		break;
    	case DIFFERENTIAL:
    		outputString.append("    </derivative>\n");
    		break;
    	case INTEGRAL:
    		outputString.append("    </integral>\n");
    		break;
    	}
    	
    	System.out.print(outputString.toString());
    }

    // The main logic loop
    public void run() {
    	System.out.println("<functions>");
    	
    	MathContext mc = MathContext.DECIMAL128;

    	if (genMode == FunctionGenerationMode.RANDOM) {
        	for (int i=0; i<this.numMathFunctions;++i){
                MathFunction mf = functionFactory.create(!this.isFractions, this.forceLinearFunctions);
                printFunction(mf, i, mc);
            }
    	}
    	else {
    		System.out.println( "TODO: generate and analyze the fitted function." );
    	}

    	System.out.println("</functions>");
    }

    public static void main(String[] args) throws IOException {
        EasyMain main = new EasyMain();
        main.parse_input(args);
        main.run();
    }
}