sealed trait Stream[+A] {
    /*
     * *어려움:* 앞에서 작성한 함수들을 이용해서 startsWith를 구현하라.
     * 이 함수는 한 Stream이 다른 한 Stream의 선행 순차열(prefix: 접두사)인지 점검해야 한다.
     * 예를 들어 Stream(1,2,3) startsWith Stream(1,2)는 true가 되어야 한다.
     */
    def startsWith[A](s: Stream[A]): Boolean =
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
