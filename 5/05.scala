sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =

    /*
     * foldRight를 이용해서 takeWhile을 구현하라.
     */
    def takeWhile(p: A => Boolean): Stream[A] =
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
        lazy val head = hd
        lazy val tail = tl
        Cons(() => head, () => tail)
    }

    def empty[A]: Stream[A] = Empty

    def apply[A](as: A*): Stream[A] =
        if (as.isEmpty) empty
        else cons(as.head, apply(as.tail: _*))
}

// Test
object Main {
    /* // Expected output:
     * 00 *
     * 01
     * 02 *
     * 03 *
     * 14
     * 15
     * 16 *
     * 17
     * 18
     * 19
     * 20
     * 30 *
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("14"); 6 },
                Stream.cons(
                    { println("17"); 7 },
                    Stream.empty,
                ),
            ),
        )
        println("00 *")
        val as = xs.takeWhile(_ == 5)
        println("02 *")
        val bs = xs.takeWhile(_ > 0)
        println("03 *")
        as.toList.map(x => println(x + 10))
        println("16 *")
        bs.toList.map(x => println(x + 13))
        println("30 *")
    }
}
