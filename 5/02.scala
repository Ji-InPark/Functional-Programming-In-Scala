sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =

    /*
     * (1) Stream의 처음 n개의 요소를 돌려주는 함수 take(n)을 작성하라.
     */
    def take(n: Int): Stream[A] =

    /*
     * (2) Stream의 처음 n개의 요소를 건너뛴 스트림을 돌려주는 drop(n)을 작성하라.
     */
    def drop(n: Int): Stream[A] =
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
     * 01 *
     * 02
     * 03
     * 04 *
     * 05
     * 06 *
     * 17
     * 18
     * 28 *
     * 38
     * 39
     * 40 *
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("02"); 7 },
            Stream.cons(
                { println("03"); 4 + 4 },
                Stream.cons(
                    { println("05"); 3 * 3 },
                    Stream.empty,
                ),
            ),
        )
        println("00 *")
        val headStream = xs.take(2)
        val tailStream = xs.drop(1)
        println("01 *") // Due to the lazy evaluation, none of `println` in the Stream is invoked here.
        val head = headStream.toList // `println` in the Stream h is invoked here.
        println("04 *")
        val tail = tailStream.toList // `println` in the Stream t is invoked here.
        println("06 *")
        head.map(x => println(x + 10))
        println("28 *")
        tail.map(x => println(x + 30))
        println("40 *")
    }
}
