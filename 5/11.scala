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
     * 좀 더 일반화된 스트림 구축 함수 unfold를 작성하라.
     * 이 함수는 초기 상태 하나와 다음 상태 및 다음 값(생성된 스트림 안의)을 산출하는 함수 하나를 받아야 한다.
     */
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
}

// Test
object Main {
    /* // Expected output:
     * state: 0
     * state: 0 // Note: this is not a typo
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
     * 0.0
     * 10.0
     * 20.0
     * 30.0
     * 40.0
     */
    def main(args: Array[String]): Unit = {
        def stateLogic(s: Int): Option[(Double, Int)] = {
            println("state: " + s)
            if (s < 5) Some((s * 10.0, s + 1)) else None
        }
        val xs = Stream.unfold(0)(stateLogic)
        val ys = Stream.unfold(0)(stateLogic)
        println("begin")
        val subfs = xs.toList
        println("lazy operation executed")
        subfs.map(println(_))
        println("next")
        val subys = xs.toList
        subys.map(println(_))
    }
}
