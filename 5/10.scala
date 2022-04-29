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
     * 무한 피보나치 수 0, 1, 1, 2, 3, 5, 8, ...으로 이루어진 무한 스트림을 생성하는 함수 fibs를 작성하라.
     */
    def fibs(): Stream[Int] = {
    }
}

// Test
object Main {
    /* // Expected output:
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
     */
    def main(args: Array[String]): Unit = {
        val fs = Stream.fibs()
        val subfs = fs.take(10).toList
        subfs.map(println(_))
    }
}
