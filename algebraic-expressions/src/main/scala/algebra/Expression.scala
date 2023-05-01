package algebra

sealed trait Expression {
  def *(t: Expression): Expression
  def /(t: Expression): Expression
  def +(t: Expression): Expression
  def -(t: Expression): Expression
}

case class Constant(i: Int) extends Expression {
  def *(e: Constant): Constant = Constant(i * e) // TODO: figure out how to not need this second definition
  def *(e: Expression): Expression = e match {
    case c: Constant => Constant(i * c)
    case v: Variable => Variable(i * v.coefficient, v.exponent)
    case p: Polynomial => p * i
  }

  def /(c: Constant): Constant = Constant(i / c)
  def /(e: Expression): Expression = e match {
    case c: Constant => Constant(i / c)
    case _: Variable => throw new Exception("Dividing constants by variables are not expected")
    case _: Polynomial => throw new Exception("Dividing constants by polynomials are not expected")
  }

  def +(c: Constant): Constant = Constant(i + c)
  def +(e: Expression): Expression = e match {
    case c: Constant => i + c
    case v: Variable => Polynomial(i, Seq(v))
    case p: Polynomial => p + i
  }

  def -(c: Constant): Constant = Constant(i - c)
  def -(e: Expression): Expression = e match {
    case c: Constant => Constant(i - c)
    case v: Variable => Polynomial(Math.negateExact(i), Seq(v))
    case p: Polynomial => (p * Constant(-1)) + i
  }

  override def toString: String = i.toString
}

case class Variable(coefficient: Int, exponent: Int = 1) extends Expression {
  def *(e: Expression): Expression = e match {
    case c: Constant => Variable(coefficient * c, exponent)
    case v: Variable => Variable(coefficient * v.coefficient, exponent + v.exponent)
    case p: Polynomial => p * this
  }

  def /(e: Expression): Expression = e match {
    case c: Constant => Variable(coefficient / c, exponent)
    case v: Variable if exponent - v.exponent == 0 => Constant(coefficient / v.coefficient)
    case v: Variable => Variable(coefficient / v.coefficient, exponent - v.exponent)
  }

  def +(e: Expression): Expression = e match {
    case c: Constant => Polynomial(c, Seq(this))
    case v: Variable if v.exponent == exponent => Variable(coefficient + v.coefficient, exponent)
    case v: Variable => Polynomial(0, Seq(this, v))
    case p: Polynomial => p + this
  }

  def -(e: Expression): Expression = e match {
    case p: Polynomial => (p * Constant(-1)) + this
  }

  override def toString: String = s"${if (coefficient < -1 || coefficient > 1) coefficient else if (coefficient == -1) "-" else ""}x${if (exponent > 1) "^"+exponent else ""}"
}

// TODO: should be a Seq of Variables
case class Polynomial(constant: Constant, exponents: Seq[Expression]) extends Expression {
  def terms: Seq[Expression] = exponents :+ constant

  def *(e: Expression): Polynomial = e match {
    case c: Constant => Polynomial(constant * c, exponents.map(_ * c))
    case v: Variable => Polynomial(terms.map(term => term * v))
    case p: Polynomial => Polynomial(terms.flatMap(a => p.terms.map(a * _)))
  }

  def /(e: Expression): Expression = e match {
    case c: Constant => Polynomial(constant / c, exponents.map(_ / c))
  }

  def +(e: Expression): Polynomial = e match {
    case c: Constant => Polynomial(constant + c, exponents)
    case v: Variable => Polynomial(constant, Polynomial.combine(exponents :+ v))
    case p: Polynomial => p.terms.foldLeft(this)((poly, term) => poly + term)
  }

  def -(e: Expression): Polynomial = e match {
    case c: Constant => Polynomial(constant - c, exponents)
    case v: Variable => Polynomial(constant, Polynomial.combine(exponents :+ Variable(Math.negateExact(v.coefficient), v.exponent)))
    case p: Polynomial => p.terms.foldLeft(this)((poly, term) => poly - term)
  }

  override def toString: String = {
    val e: Seq[Expression] = Polynomial.combine(exponents)
    e.tail.foldLeft(e.head.toString)((string, term) => term match {
      case v: Variable if v.coefficient < 0 => s"$string - ${Variable(Math.negateExact(v.coefficient), v.exponent)}"
      case v => s"$string + $v"
    }) + (if (constant == Constant(0)) "" else if (constant > 0) " + " + constant else " - " + Math.negateExact(constant))
  }
}

object Polynomial {
  def sort(expressions: Seq[Expression]): Seq[Expression] = expressions.sortWith((e1, e2) => (e1, e2) match {
    case (v1: Variable, v2: Variable) => v1.exponent > v2.exponent
    case (v1: Variable, c1: Constant) => c1 > v1.exponent
    case (c1: Constant, v1: Variable) => c1 > v1.exponent
  })

  // TODO: implement sum with implicit Equality
  def combine(expressions: Seq[Expression]): Seq[Expression] = sort(expressions.groupBy {
    case _: Constant => 0
    case v: Variable => v.exponent
  }
    .map(f => f._2.tail.foldLeft(f._2.head)(_ + _)).toSeq)

  def apply(expressions: Seq[Expression]): Polynomial = combine(expressions).find { case _: Constant => true; case _ => false } match {
    case Some(c: Constant) => new Polynomial(c, expressions.filter { case _: Constant => false; case _ => true })
    case _ => new Polynomial(Constant(0), expressions)
  }
}

object Expression extends ExpressionParser {
  def apply(s: String): Expression = {
    val value = parseAll(expr, s)
//    println(value)
    value match {
      case Success(result, inl) => result
      case _ => throw new Exception("womp")
    }
  }

  implicit def constantToInt(f: Constant): Int = f.i
  implicit def intToConstant(f: Int): Constant = Constant(f)
}
