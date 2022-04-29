sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * TODO: To run the test code, copy your implementation of `toList` here!
     */
    def toList: List[A] =

    /*
     * TODO: To run the test code, copy your implementation of `take` here!
     */
    def take(n: Int): Stream[A] =
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

    /*
    * ones를 조금 일반화해서, 주어진 값의 무한 Stream을 돌려주는 함수 constant를 작성하라. 
    */
    // val ones: Streeam[Int] = cons(1, ones)
    def constant[A](a: A): Stream[A] =
}

// Test
object Main {
    /* // Expected output:
     * 7
     * 7
     * 7
     * 7
     * 7
     * 7
     * 7
     */
    def main(args: Array[String]): Unit = {
        val cs = Stream.constant(7)
        val subcs = cs.take(7).toList
        subcs.map(println(_))
    }
}
