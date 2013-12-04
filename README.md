math-fun
========

Just testing out some fun with mathematics.

Initially, the goal of this package will be to randomly generate (and print to stdout) a set of
functions of the form:

```
f(x) = Ax^a + Bx^b + ...
```

That is, a function will be a set of terms, where each term has a coefficient
and an exponent.

This package will also provide utilities for analyzing the functions in various
ways, e.g., differentiation, integration, finding local and global extrema, etc.

Compile
=======

Use Ant:

```
ant
```

A jar file will be produced in the `dist` directory.


Use
===

```
java -jar dist/math-fun.jar [options]
```

where the options are:
- `-n NUM`: The number of functions to randomly generate and display. (Default: 10)
- `-d`: Print the derivative of each function, too. (Default: off)
- `-i`: Print the integral of each function as well. (Default: off)
- `-f`: Generate fractional coefficients and exponents. (Default: off)
- `-x`: Format output as XML. (Default: off)
- `-l`: Create only strictly first-degree linear functions. (Default: off)
- `--domain-min NUM`: Domain minimum, for min/max calculations. (Default: -Infinity)
- `--domain-max NUM`: Domain maximum, for min/max calculations. (Default: +Infinity)




Test
====

To run the JUnit tests:

```
ant test
```


