sealed trait Stream[+A] {
    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =

    /*
     * TODO: To run the test code, copy your implementation of `map` and paste it here!
     */
    def map[B](f: A => B): Stream[B] =

    /*
     * TODO: To run the test code, copy your implementation of `flatMap` and paste it here!
     */
    def flatMap[B](f: A => Stream[B]): Stream[B] =

    /*
     * unfold를 이용해서 tails를 구현하라.
     * tails는 주어진 입력 Stream과 그 후행 순차열(suffix, 접미사)들로 이루어진 스트림을 돌려준다.
     * 예를 들어 Stream(1,2,3)에 대해 이 함수는 원래의 Stream(Stream(1,2,3), Stream(2,3), Stream(3), Stream())을 돌려주어야 한다.
     */
    def tails: Stream[Stream[A]] =
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
}

// Test
object Main {
    /* // Expected output:
     * List(1, 2, 3)
     * 1
     * 2
     * 3
     * List(2, 3)
     * 2
     * 3
     * List(3)
     * 3
     */
    def main(args: Array[String]): Unit = {
        for (
            stream <- Stream(1,2,3).tails.toList;
            i <- { println(stream.toList); stream.toList }
        ) yield println(i)
    }
}
