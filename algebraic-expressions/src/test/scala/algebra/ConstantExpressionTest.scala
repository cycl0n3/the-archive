package algebra

import org.scalatest.{FlatSpec, Matchers}

class ConstantExpressionTest extends FlatSpec with Matchers {

  // TODO: Add implicit conversions for constants
  "Constant" should "do simple addition" in {
    Expression("3 + 7") shouldEqual Expression("10")
  }

  it should "do simple subtraction" in {
    Expression("3 - 1") shouldEqual Expression("2")
  }

  it should "do simple addition and subtraction" in {
    Expression("7 + 3 - 1 + 100") shouldEqual Expression("109")
  }

  it should "do simple multiplication" in {
    Expression("1 * 2") shouldEqual Expression("2")
  }

  it should "multiply do it all" in {
    Expression("1 + 2 * 2 * 100 - 30") shouldEqual Expression("371")
  }

  it should "handle parenthesis" in {
    Expression("1 + 2 * 2 * (100 - 100)") shouldEqual Expression("1")
  }

  it should "do division" in {
    Expression("6 / 2") shouldEqual Expression("3")
  }

  it should "handle multiple parenthesis" in {
    Expression("(1 + 3) / (2 * (100 - 98))") shouldEqual Expression("1")
  }
}
