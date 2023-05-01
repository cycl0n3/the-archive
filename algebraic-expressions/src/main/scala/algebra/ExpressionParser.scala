package algebra

import scala.util.parsing.combinator.JavaTokenParsers

trait ExpressionParser extends JavaTokenParsers {
  def loop(acc: Expression, b: List[~[String, Expression]]): Expression = {
//    println(acc, b)
    b match {
      case h :: tail => loop(h._1 match {
        case "+" => acc + h._2
        case "-" => acc - h._2
        case "*" | "(" => acc * h._2
        case "/" => acc / h._2
      }, tail)
      case Nil => acc
    }
  }

  def expr: Parser[Expression] = term ~ rep("+" ~ term | "-" ~ term) ^^
    (b => loop(b._1, b._2))

  def term: Parser[Expression] = factor ~ rep("*" ~ factor | "/" ~ factor | "(" ~ expr <~ ")") ^^
    (b => loop(b._1, b._2))

  def coefficient: Parser[Variable] =
    """-?\d*""".r <~ "x" ^^ {
      case "" => Variable(1)
      case n => Variable(n.toInt)
    }
  def exponent: Parser[Variable] = coefficient ~ "^" ~"""\d+""".r ^^ {
    case x ~ _ ~ e => Variable(x.coefficient, e.toInt)
  }
  def constant: Parser[Constant] = """-?\d+""".r ^^ (n => Constant(n.toInt))

  // TODO: use in term
  def polynomial: Parser[Expression] = ("(" ~ expr <~ ")" | "-(" ~ expr <~ ")") ^^ {
    case "-("~e => e * -1
    case "("~e => e
  }

  def factor: Parser[Expression] = exponent | coefficient | constant | polynomial
}