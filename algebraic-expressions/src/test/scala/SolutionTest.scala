import org.scalatest.{FlatSpec, Matchers}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import scala.io.Source

class SolutionTest extends FlatSpec with Matchers {

  val prefix = "tricky"

  def readFileToString(fileName: String): String =
    Source.fromURL(getClass.getResource(fileName)).getLines().mkString("\n")

  "Algebraic Expressions" should "work" in {
    val myOut = new ByteArrayOutputStream
    Console.withOut(new PrintStream(myOut)) {
      System.setIn(new ByteArrayInputStream(readFileToString(s"$prefix/test").getBytes()))

      Solution.main(Array.empty)
    }

    myOut.toString shouldEqual readFileToString(s"$prefix/expected")
  }

}
