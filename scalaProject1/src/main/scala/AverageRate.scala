object AverageRate {

    private object Helper {
        implicit class DoubleWithPower(val d1: Double) {
            def ^(d2: Double): Double = {
                Math.pow(d1, d2)
            }

            def root(): Double = {
                Math.sqrt(d1)
            }
        }

        def averageRateOfChange(f: Double => Double, a: Double, b: Double): Double = {
            (f(b) - f(a)) / (b - a)
        }
    }

    def main(args: Array[String]): Unit = {
        import Helper._

        val f = (x: Double) => x^2 - 1/x
        var a = 10.0
        var b = 20.0

        val avg_rate_of_change_of_f = averageRateOfChange(f, a, b)
        println("Average rate of change of f(x)=x^2+x between x=10 and x=20 is: " + avg_rate_of_change_of_f)

        val z = (t: Double) => (2*t - 1)/(t^2 - t + 3)
        a = 1.0
        b = 2.0

        val avg_rate_of_change_of_z = averageRateOfChange(z, a, b)
        println("Average rate of change of z(t)=(2t-1)/(t^2-t+3) between t=1 and t=2 is: " + avg_rate_of_change_of_z)

        val g = (s: Double) => Math.pow((s^2 + 1), 3)
        a = 0.0
        b = 3.0

        val avg_rate_of_change_of_g = averageRateOfChange(g, a, b)
        println("Average rate of change of g(s)=(s^2+1)^3 between s=0 and s=3 is: " + avg_rate_of_change_of_g)

        val t = (y: Double) => (y + 2 - y.root())/y.root()
        a = 4.0
        b = 16.0

        val avg_rate_of_change_of_t = averageRateOfChange(t, a, b)
        println("Average rate of change of t(y)=(y+2-sqrt(y))/sqrt(y) between y=4 and y=16 is: " + avg_rate_of_change_of_t)

        val y = (x: Double) => (7*x + 9).root()
        a = 0.0
        b = 1.0

        val avg_rate_of_change_of_y = averageRateOfChange(y, a, b)
        println("Average rate of change of y(x)=sqrt(7x+9) between x=0 and x=1 is: " + avg_rate_of_change_of_y)

        val r = (h: Double) => 3*h^4 - 2*h^2 + 12*h - 9
        a = 2.0
        b = 3.0

        val avg_rate_of_change_of_r = averageRateOfChange(r, a, b)
        println("Average rate of change of r(h)=3h^4-2h^2+12h-9 between h=2 and h=3 is: " + avg_rate_of_change_of_r)

        val k = (q: Double) => 1/(2*q + 3)
        a = 1.0
        b = 3.0

        val avg_rate_of_change_of_k = averageRateOfChange(k, a, b)
        println("Average rate of change of k(q)=1/(2q+3) between q=1 and q=3 is: " + avg_rate_of_change_of_k)
    }
}