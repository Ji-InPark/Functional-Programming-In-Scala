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
    val ones: Stream[Int] =

    def constant[A](a: A): Stream[A] =

    def from(n: Int): Stream[Int] =

    def fibs(): Stream[Int] =
}

// Test
object Main {
    /* // Expected output:
     * 1
     * 1
     * 1
     * **********
     * 7
     * 7
     * 7
     * 7
     * 7
     * 7
     * 7
     * **********
     * 0
     * 1
     * 1
     * 2
     * 3
     * 5
     * 8
     * 13
     * 21
     * 34
     * **********
     * state: 0
     * state: 2
     * begin
     * state: 1
     * state: 2
     * state: 3
     * state: 4
     * state: 5
     * lazy operation executed
     * 0.0
     * 10.0
     * 20.0
     * 30.0
     * 40.0
     * next
     * state: 3
     * state: 4
     * state: 5
     * 20.0
     * 30.0
     * 40.0
     */
    def main(args: Array[String]): Unit = {
        val subones = Stream.ones.take(3).toList
        subones.map(println(_))

        println("**********")

        val cs = Stream.constant(7)
        val subcs = cs.take(7).toList
        subcs.map(println(_))

        println("**********")

        val fs = Stream.fibs()
        val subfs = fs.take(10).toList
        subfs.map(println(_))

        println("**********")

        def stateLogic(s: Int): Option[(Double, Int)] = {
            println("state: " + s)
            if (s < 5) Some((s * 10.0, s + 1)) else None
        }
        val xs = Stream.unfold(0)(stateLogic)
        val ys = Stream.unfold(2)(stateLogic)
        println("begin")
        val subxs = xs.toList
        println("lazy operation executed")
        subxs.map(println(_))
        println("next")
        val subys = ys.toList
        subys.map(println(_))
    }
}
