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

This package will also provide utitlies for analyzing the functions in various
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
- `-d`: Print the deriviate of each function, too. (Default: off)
- `-f`: Generate fractional coefficients and exponents. (Default: off)




Test
====

To run the JUnit tests:

```
ant test
```


