package com.swtanalytics.math;

import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class EasyMain {

    public static final int NUM_MATH_FUNCTIONS_DEFAULT = 10;

    @Option(name="-n", usage="The number of functions to generate.")
    public int numMathFunctions = NUM_MATH_FUNCTIONS_DEFAULT;


    protected void parse_input(String[] args) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            // validate the input a bit.
            // There's probably a nicer way to do this with args4j
            if (this.numMathFunctions <= 0) {
                throw new CmdLineException("Option -n requires a positive integer");
            }

        } catch (CmdLineException e) {
            System.out.print(e.getMessage());
            System.out.print("\n");
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    // The main logic loop
    public void run() {
        for (int i=0; i<this.numMathFunctions;++i){
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

    public static void main(String[] args) throws IOException {
        EasyMain main = new EasyMain();
        main.parse_input(args);
        main.run();
    }

}
