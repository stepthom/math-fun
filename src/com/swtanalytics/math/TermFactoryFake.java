package com.swtanalytics.math;

import java.util.LinkedList;
import java.util.Queue;

public class TermFactoryFake implements TermFactory {
    public Queue<Term> results = new LinkedList<Term>();

    @Override
    public Term create(boolean forceWholeNumberCoefficient, int maxExponent) {
        return results.remove();
    }
}
