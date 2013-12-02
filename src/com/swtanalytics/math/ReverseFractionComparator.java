package com.swtanalytics.math;

import java.util.Comparator;

public class ReverseFractionComparator implements Comparator<Fraction> {
    @Override
    public int compare(Fraction o1, Fraction o2) {
        return o2.compareTo(o1);
    }
}
