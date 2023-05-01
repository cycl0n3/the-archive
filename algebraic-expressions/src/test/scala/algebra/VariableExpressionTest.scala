package algebra

import org.scalatest.{FlatSpec, Matchers}

class VariableExpressionTest extends FlatSpec with Matchers {

  "Variable" should "parse simple multiplier" in {
    Expression("x") shouldEqual Variable(1)
    Expression("3x") shouldEqual Variable(3)
    Expression("1001x") shouldEqual Variable(1001)
  }

  it should "parse simple exponent" in {
    Expression("x^2") shouldEqual Variable(1, 2)
    Expression("3x^2") shouldEqual Variable(3, 2)
    Expression("300x^21") shouldEqual Variable(300, 21)
  }

  it should "multiply" in {
    Expression("x") * Expression("4x") shouldEqual Variable(4, 2)
    Expression("10x") * Expression("4x") shouldEqual Variable(40, 2)
    Expression("10x") * 4 shouldEqual Variable(40)
    4 * Variable(10, 3) shouldEqual Variable(40, 3)
    Variable(5, 10) * Variable(4, 10) shouldEqual Variable(20, 20)
  }

  it should "divide" in {
    Expression("4x") / Expression("2x") shouldEqual Constant(2)
    Expression("4x") / Constant(2) shouldEqual Variable(2)
    Constant(4) / Constant(2) shouldEqual Constant(2)
    Expression("10x^2") / Expression("5x") shouldEqual Expression("2x")
  }

  it should "add" in {
    Expression("4x") + Expression("4x") shouldEqual Expression("8x")
    Expression("4x^2") + Expression("4x") shouldEqual Polynomial(0, Seq(Variable(4,2), Variable(4)))
    Expression("4x^2") + Expression("4x") shouldEqual Expression("4x^2 + 4x")
  }
}
