sealed trait Stream[+A] {
    def forAll(p: A => Boolean): Boolean =
        this match {
            case Cons(h, t) => if (p(h())) t().forAll(p) else false
            case Empty => true
        }

    /*
     * *어려움:* 앞에서 작성한 함수들을 이용해서 startsWith를 구현하라.
     * 이 함수는 한 Stream이 다른 한 Stream의 선행 순차열(prefix: 접두사)인지 점검해야 한다.
     * 예를 들어 Stream(1,2,3) startsWith Stream(1,2)는 true가 되어야 한다.
     */
    def startsWith[A](s: Stream[A]): Boolean =
        Stream.unfold((this, s)){
            case (Cons(h1, t1), Cons(h2, t2)) => Some((h1() == h2(), (t1(), t2())))
            case (Empty, Cons(h2, t2)) => return false
            case _ => None
        }.forAll(bool => bool)
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

    def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
        f(z).getOrElse(return empty) match {
            case (a, s) => cons(a, unfold(s)(f))
        }
}

// Test
object Main {
    /* // Expected output:
     * true
     * true
     * false
     * true
     * false
     * false
     * false
     * true
     * false
     */
    def main(args: Array[String]): Unit = {
        println(Stream(1,2,3) startsWith Stream(1,2))
        println(Stream(1,2,3) startsWith Stream(1,2,3))
        println(Stream(1,2,3) startsWith Stream(1,2,3,4))
        println(Stream(1,2) startsWith Stream())
        println(Stream(1,2) startsWith Stream(2))
        println(Stream(1,2) startsWith Stream(2,3))
        println(Stream(1,2) startsWith Stream(2,3,4))
        println(Stream() startsWith Stream())
        println(Stream() startsWith Stream(1))
    }
}
