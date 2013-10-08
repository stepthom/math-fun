package com.swtanalytics.math;

import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.EigenDecomposition;

public class PolynomialRootFinder {
    /**
     * <p>Note: The following code was found at:
     * <a>https://code.google.com/p/efficient-java-matrix-library/wiki/PolynomialRootExample</a>
     * </p>
     *
     * <p>
     * Given a set of polynomial coefficients, compute the roots of the polynomial.  Depending on
     * the polynomial being considered the roots may contain complex numbers.  When complex numbers are
     * present they will come as pairs of complex conjugates.
     * </p>
     *
     * @param coefficients Coefficients of the polynomial.
     * @return The roots of the polynomial
     */
    public Complex64F[] findRoots(double... coefficients) {
        if (coefficients.length <= 1) {
            throw new IllegalArgumentException("Must supply at least two coefficients.");
        }

        int N = coefficients.length - 1;

        // Construct the companion matrix
        DenseMatrix64F c = new DenseMatrix64F(N, N);

        double a = coefficients[N];
        for( int i = 0; i < N; i++ ) {
            c.set(i, N-1, -coefficients[i] / a);
        }

        for( int i = 1; i < N; i++ ) {
            c.set(i, i-1, 1);
        }

        // use generalized eigenvalue decomposition to find the roots
        EigenDecomposition<DenseMatrix64F> evd =  DecompositionFactory.eig(N, false);

        evd.decompose(c);

        Complex64F[] roots = new Complex64F[N];

        for( int i = 0; i < N; i++ ) {
            roots[i] = evd.getEigenvalue(i);
        }

        return roots;
    }
}
