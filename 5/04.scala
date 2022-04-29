sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` here!
     */
    def toList: List[A] =

    //// Use it if you need.
    // def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    //     case Cons(h, t) => f(h(), t().foldRight(z)(f))
    //     case _ => z
    // }

    /*
     * Stream의 모든 요소가 주어진 술어를 만족하는지 점검하는 forAll 함수를 구현하라.
     * 만족하지 않는 값을 만나면 즉시 순회를 마쳐야 한다.
     */
    def forAll(p: A => Boolean): Boolean =
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
     * 02
     * 13
     * 14 *
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("02"); 6 },
                Stream.cons(
                    { println("17"); 7 },
                    Stream.empty,
                ),
            ),
        )
        println("00 *")
        println(if (xs.forAll(_ == 5)) 3 else 13)
        println("14 *")
    }
}
