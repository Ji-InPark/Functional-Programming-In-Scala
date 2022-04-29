sealed trait Stream[+A] {
    /*
     * unfold를 이용해서 map, take, takeWhile, zipWith(제3장 참고), zipAll을 구현하라.
     * zipAll 함수는 스트림에 요소가 더 있는 한 순회를 계속해야 한다.
     * 각 스트림이 소진되었는지는 Option을 이용해서 지정한다.
     */
    def map[B](f: A => B): Stream[B] =
    def take(n: Int): Stream[A] =
    def takeWhile(p: A => Boolean): Stream[A] =
    def zipWith[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] =
    def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] =
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
     * TODO: Copy your implementation of `unfold` and paste it here!
     */
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
}
