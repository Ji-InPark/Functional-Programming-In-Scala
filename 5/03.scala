sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =
        this match {
            case Cons(h, t) => h() :: t().toList
            case Empty => Nil
        }

    /*
     * 주어진 술어를 만족하는 선행 요소들을 모두 돌려주는 함수 takeWhile을 작성하라.
     *
     * 주의: 주어진 Stream의 정의를 따르려면, takeWhile을 호출했을 때 첫 번째 element만 evaluate되어야 합니다.
     */
    def takeWhile(p: A => Boolean): Stream[A] =
        this match {
            case Cons(h, t) if (p(h())) => Stream.cons(h(), t().takeWhile(p))
            case _ => Empty
        }
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
     * 02 *
     * 03 *
     * 14
     * 15
     * 16 *
     * 17
     * 18
     * 19
     * 20
     * 30 *
     */
    def main(args: Array[String]): Unit = {
        val xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("14"); 6 },
                Stream.cons(
                    { println("17"); 7 },
                    Stream.empty,
                ),
            ),
        )
        println("00 *")
        val as = xs.takeWhile(_ == 5)
        println("02 *")
        val bs = xs.takeWhile(_ > 0)
        println("03 *")
        as.toList.map(x => println(x + 10))
        println("16 *")
        bs.toList.map(x => println(x + 13))
        println("30 *")
    }
}
