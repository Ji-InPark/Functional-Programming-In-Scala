sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * (1) foldRight를 이용해서 map을 구현하라.
     */
    def map[B](f: A => B): Stream[B] =

    /*
     * (2) foldRight를 이용해서 filter를 구현하라.
     */
    def filter(p: A => Boolean): Stream[A] =

    /*
     * (3) foldRight를 이용해서 append를 구현하라. 이 메서드는 자신의 인수에 대해 엄격하지 않아야 한다.
     */
    def append[B >: A](a: Stream[B]): Stream[B] =

    /*
     * (4) foldRight를 이용해서 flatMap을 구현하라.
     */
    def flatMap[B](f: A => Stream[B]): Stream[B] =
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
