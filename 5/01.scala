sealed trait Stream[+A] {
    /*
     * Stream을 List로 변환하되 평가를 강제해서 REPL로 목록의 요소들을 볼 수 있게 하는 함수를 작성하라.
     * 표준 라이브러리에 있는 정규 List 형식으로 변환하면 된다.
     * 이 함수와 Stream에 대해 작용하는 다른 함수들을 Stream 특질 안에 넣어도 좋다. (vice versa)
     */
    def toList: List[A] =
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
     * 01 *
     * 02
     * 03
     * 13 *
     * 14
     * 15
     * 16
     * 17 *
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("02"); 2 * 2 },       // () => 4
            Stream.cons(
                2 + 3,                      // () => 5
                Stream.cons(
                    { println("03"); 6 },   // () => 6
                    Stream.empty,           // Empty
                ),
            ),
        )
        println("01 *")
        val l = xs.toList
        println("13 *")
        l.map(x => println(x + 10))
        println("17 *")
    }
}
