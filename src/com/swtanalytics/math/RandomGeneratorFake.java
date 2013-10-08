package com.swtanalytics.math;

import java.util.LinkedList;
import java.util.Queue;

public class RandomGeneratorFake implements RandomGenerator {
    public Queue<Integer> generateIntResults = new LinkedList<Integer>();

    public double generateDoubleResult;
    public int lastMinInclusiveArgument;
    public int lastMaxInclusiveArgument;

    @Override
    public int generateInt(int minInclusive, int maxInclusive) {
        lastMinInclusiveArgument = minInclusive;
        lastMaxInclusiveArgument = maxInclusive;
        return generateIntResults.remove();
    }

    @Override
    public double generateDouble() {
        return generateDoubleResult;
    }
}
