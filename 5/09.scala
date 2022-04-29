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
     * n에서 시작해서 n + 1, n + 2, 등으로 이어지는 무한 정수 스트림을 생성하는 함수를 작성하라.
     */
    def from(n: Int): Stream[Int] =
}

// Test
object Main {
    /* // Expected output:
     * 6
     * 7
     * 8
     * 9
     * 10
     */
    def main(args: Array[String]): Unit = {
        val cs = Stream.from(6)
        val subcs = cs.take(5).toList
        subcs.map(println(_))
    }
}
