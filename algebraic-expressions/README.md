# Simplifying Algebraic Expressions

Diving into Scala's Parsing Combinators

## Task 
Given an algebraic expression, reduce the expression to a simplified form meeting the following criteria:

* All terms are in descending order of the power of variable `x`.

* Coefficients should be concatenated immediately to the left of your variable (e.g.: `5 * x` should be printed as `5x`).

* There are exactly 3 characters between any 2 consecutive terms of the expression: a space, followed by a `+` or `-` sign, followed by another space.

* In case there is no operator between expressions, assume that it implies multiplication. e.g. `(5x+2)(x+2)` should be treated as `(5x+2)*(x+2)`, `5(x+1)` should be treated as `5*(x+1)`.

* The simplified expression must not contain any parentheses.

* 1-coefficients and 1-powers are implied; if the coefficient or power of a certain `x` term is 1, do not output 1 (e.g.: `1x` or `1 * x` simplifies to `x`, and `x^1` simplifies to `x`).

* Do not print the powers of `x` having a coefficient of 0 (e.g.: output `5x^2 - 3`, not `5x^2 + 0x - 3`).

The original challenge comes from [HackerRank](https://www.hackerrank.com/challenges/simplify-the-algebraic-expressions/problem)