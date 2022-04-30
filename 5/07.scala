sealed trait Stream[+A] {
    def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
        case Cons(h, t) => f(h(), t().foldRight(z)(f))
        case _ => z
    }

    /*
     * TODO: To run the test code, copy your implementation of `toList` and paste it here!
     */
    def toList: List[A] =

    /*
     * (1) foldRight를 이용해서 map을 구현하라.
     */
    def map[B](f: A => B): Stream[B] =

    /*
     * (2) foldRight를 이용해서 filter를 구현하라.
     */
    def filter(p: A => Boolean): Stream[A] =

    /*
     * (3) foldRight를 이용해서 append를 구현하라. 이 메서드는 자신의 인수에 대해 엄격하지 않아야 한다.
     */
    def append[B >: A](a: Stream[B]): Stream[B] =

    /*
     * (4) foldRight를 이용해서 flatMap을 구현하라.
     */
    def flatMap[B](f: A => Stream[B]): Stream[B] =
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
     * ***** map
     * GO
     * 01
     * 02 *
     * 03
     * 04
     * 15
     * 16
     * 17
     * ***** filter
     * BEGIN
     * 01
     * 02
     * 03
     * 04 *
     * END
     * BEGIN
     * 01
     * 02
     * 03 *
     * 04
     * 15
     * END
     * ***** append
     * GO
     * LAZY-1
     * ***** flatMap
     * GO
     * LAZY-1
     * GO
     * LAZY-2
     * LAZY-3
     * List(4, 3, 2, 1, 5, 4, 3, 2, 1, 6, 5, 4, 3, 2, 1)
     */
    def main(args: Array[String]): Unit = {
        println("***** map")

        var xs: Stream[Int] = Stream.cons(
            { println("01"); -5 },
            Stream.cons(
                { println("03"); -6 },
                Stream.cons(
                    { println("04"); -7 },
                    Stream.empty,
                ),
            ),
        )

        println("GO")
        var ys: Stream[Int] = xs.map(_ * -1 + 10)
        println("02 *")
        ys.toList.map(println(_))

        println("***** filter")
        
        xs = Stream.cons(
            { println("01"); 14 },
            Stream.cons(
                { println("02"); 15 },
                Stream.cons(
                    { println("03"); 16 },
                    Stream.empty,
                ),
            ),
        )

        println("BEGIN")

        ys = xs.filter(_ < 0)
        println("04 *")
        ys.toList.map(println(_))

        println("END")
        
        xs = Stream.cons(
            { println("01"); 14 },
            Stream.cons(
                { println("02"); 15 },
                Stream.cons(
                    { println("04"); 16 },
                    Stream.empty,
                ),
            ),
        )

        println("BEGIN")

        ys = xs.filter(_ % 2 != 0)
        println("03 *")
        ys.toList.map(println(_))

        println("END")

        println("***** append")

        xs = Stream.cons(
            { println("LAZY-1"); -1 },
            Stream.cons(
                { println("LAZY-2"); -2 },
                Stream.cons(
                    { println("LAZY-3"); -3 },
                    Stream.empty,
                ),
            ),
        )
        ys = Stream.cons(
            { println("LAZY-4"); -4 },
            Stream.cons(
                { println("LAZY-5"); -5 },
                Stream.cons(
                    { println("LAZY-6"); -6 },
                    Stream.empty,
                ),
            ),
        )

        println("GO")
        xs.append(ys)
    
        println("***** flatMap")
        
        def seq(n: Int): Stream[Int] =
            if (n > 0) Stream.cons(n, seq(n - 1))
            else Stream.empty

        xs = Stream.cons(
            { println("LAZY-1"); 4 },
            Stream.cons(
                { println("LAZY-2"); 5 },
                Stream.cons(
                    { println("LAZY-3"); 6 },
                    Stream.empty,
                ),
            ),
        )

        println("GO")
        ys = xs.flatMap(seq(_))

        println("GO")
        println(ys.toList)
    }
}
