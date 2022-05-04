sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =
        this match {
            case Cons(h, t) => h() :: t().toList
            case Empty => Nil
        }

    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * *어려움:* tails를 일반화한 scanRight 함수를 작성하라.
     * 이 함수는 중간 결과들의 스트림을 돌려주는 foldRight와 비슷하다.
     * 예시:
     *
     * scala> Stream(1,2,3).scanRight(0)(_ + _).toList
     * res0: List[Int] = List(6,5,3,0)
     *
     * 이 예는 표현식 List(1+2+3+0, 2+3+0, 3+0, 0)과 동등해야 한다.
     * 독자의 구현은 중간 결과들을 재사용해야 한다.
     * 즉, 요소개 n개인 Stream을 훑는 데 걸리는 시간이 항상 n에 선형 비례해야 한다.
     * 이 함수를 unfold를 이용해서 작성할 수 있을까?
     * 있다면 어떻게?
     * 없다면 왜 그럴가?
     * 이 함수를 앞에서 작성한 다른 어떤 함수로 구현할 수는 있을까?
     */
    def scanRight[B](b: B)(f: (A, => B) => B): Stream[B] =
        this match {
            case Cons(h, t) => Stream.cons(foldRight[B](b)(f), t().scanRight(b)(f))
            case Empty => Stream.cons(b, Empty)
        }

    def tails: Stream[Stream[A]] = scanRight[Stream[A]](Stream.empty)(Stream.cons(_, _))
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
     * List(6, 5, 3, 0)
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
        println(Stream(1,2,3).scanRight(0)(_ + _).toList)
    
        for (
            stream <- Stream(1,2,3).tails.toList;
            i <- { println(stream.toList); stream.toList }
        ) yield println(i)
    }
}
