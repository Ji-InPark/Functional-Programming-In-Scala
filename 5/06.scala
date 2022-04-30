sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * *어려움:* foldRight를 이용해서 headOption을 구현하라.
     *
     * 주의: 안 어렵습니다.
     */
    // def headOption: Option[A] = this match {
    //     case Empty => None
    //     case Cons(h, t) => Some(h())
    // }
    def headOption: Option[A] =
        foldRight[Option[A]](None)((a, _) => Some(a))
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
     * 11
     * -1
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("01"); 11 },
            Stream.cons(
                { println("14"); 6 },
                Stream.cons(
                    { println("17"); 7 },
                    Stream.empty,
                ),
            ),
        )
        val ys = Stream.empty
        println("00 *")
        println(xs.headOption.getOrElse(-1))
        println(ys.headOption.getOrElse(-1))
    }
}
