sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =

    /*
     * TODO: To run the test code, copy your implementation of `take` and paste it here!
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
     * TODO: Copy your implementation of `unfold` and paste it here!
     */
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =

     /*
     * unfold를 이용해서 fibs, from, constant, ones를 작성하라.
     */
    def ones[A](a: A): Stream[A] =
    def constant[A](a: A): Stream[A] =
    def from(n: Int): Stream[Int] =
    def fibs(): Stream[Int] =
}
