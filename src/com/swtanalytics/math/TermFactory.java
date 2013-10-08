package com.swtanalytics.math;

public interface TermFactory {
    public Term create(boolean forceWholeNumberCoefficient, int maxExponent);
}
