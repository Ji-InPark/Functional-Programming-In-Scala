sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    def append[B >: A](a: Stream[B]): Stream[B] =
        foldRight[Stream[B]](a)(Stream.cons(_, _))

    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =
        this match {
            case Cons(h, t) => h() :: t().toList
            case Empty => Nil
        }

    /*
     * unfold를 이용해서 tails를 구현하라.
     * tails는 주어진 입력 Stream과 그 후행 순차열(suffix, 접미사)들로 이루어진 스트림을 돌려준다.
     * 예를 들어 Stream(1,2,3)에 대해 이 함수는 원래의 Stream(Stream(1,2,3), Stream(2,3), Stream(3), Stream())을 돌려주어야 한다.
     *
     * 주의: 가장 뒤에 빈 Stream 하나가 존재해야 합니다.
     */
    def tails: Stream[Stream[A]] =
        Stream.unfold[Stream[A], Stream[A]](this)(s => s match {
            case Cons(h, t) => Some((s, t()))
            case _ => None
        }).append(Stream[Stream[A]](Empty))
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
        f(z).getOrElse(return empty) match {
            case (a, s) => cons(a, unfold(s)(f))
        }
}

// Test
object Main {
    /* // Expected output:
     * List()
     * List(1, 2, 3)
     * 1
     * 2
     * 3
     * List(2, 3)
     * 2
     * 3
     * List(3)
     * 3
     * List()
     */
    def main(args: Array[String]): Unit = {
        println(Stream(Empty).toList(0).toList)
    
        for (
            stream <- Stream(1,2,3).tails.toList;
            i <- { println(stream.toList); stream.toList }
        ) yield println(i)
    }
}
