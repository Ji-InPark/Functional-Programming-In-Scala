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
     * unfold를 이용해서 map, take, takeWhile, zipWith(제3장 참고), zipAll을 구현하라.
     * zipAll 함수는 스트림에 요소가 더 있는 한 순회를 계속해야 한다.
     * 각 스트림이 소진되었는지는 Option을 이용해서 지정한다.
     */
    def map[B](f: A => B): Stream[B] =
        Stream.unfold[B, Stream[A]](this){
            case Cons(h, t) => Some((f(h()), t()))
            case _ => None
        }

    def take(n: Int): Stream[A] =
        Stream.unfold[A, (Stream[A], Int)]((this, n)){
            case (Cons(h, t), i) if (i > 0) => Some((h(), (t(), i - 1)))
            case _ => None
        }

    def takeWhile(p: A => Boolean): Stream[A] =
        Stream.unfold[A, Stream[A]](this){
            case Cons(h, t) if (p(h())) => Some((h(), t()))
            case _ => None
        }

    def zipWith[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] =
        Stream.unfold[C, (Stream[A], Stream[B])]((this, s2)){
            case (Cons(h1, t1), Cons(h2, t2)) => Some((f(h1(), h2()), (t1(), t2())))
            case _ => None
        }

    def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] =
        Stream.unfold[(Option[A], Option[B]), (Stream[A], Stream[B])]((this, s2)){
            case (Cons(h1, t1), Cons(h2, t2)) => Some(((Some(h1()), Some(h2())), (t1(), t2())))
            case (Cons(h1, t1), Empty) => Some(((Some(h1()), None), (t1(), Empty)))
            case (Empty, Cons(h2, t2)) => Some(((None, Some(h2())), (Empty, t2())))
            case _ => None
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
    
    /*
     * TODO: Copy your implementation of `unfold` and paste it here!
     */
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
        f(z).getOrElse(return empty) match {
            case (a, s) => cons(a, unfold(s)(f))
        }
}

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
     * ***** take
     * 00 *
     * 01
     * 02 *
     * 03
     * 04 *
     * 15
     * 16
     * 27 *
     * ***** takeWhile
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
     * ***** zipWith
     * GO
     * 01
     * 02
     * END
     * 13
     * 14
     * 15
     * 16
     * List(11, 22, 33)
     * ***** zipAll
     * GO
     * 01
     * 02
     * GO
     * 13
     * 14
     * 15
     * List((Some(5),Some(5)), (Some(6),Some(6)), (Some(7),None))
     * GO
     * 01
     * 02
     * GO
     * 13
     * 14
     * 15
     * List((Some(5),Some(5)), (Some(6),Some(6)), (None,Some(6)))
     * GO
     * 01
     * 02
     * GO
     * 13
     * 14
     * List((Some(5),Some(5)), (Some(6),Some(6)))
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
     
        println("***** take")
     
        xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("03"); 6 },
                Stream.cons(
                    { println("-1"); 3 * 3 },
                    Stream.empty,
                ),
            ),
        )
        println("00 *")
        val headStream = xs.take(2)
        println("02 *")
        val head = headStream.toList
        println("04 *")
        head.map(x => println(x + 10))
        println("27 *")

        println("***** takeWhile")

        xs = Stream.cons(
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

        println("***** zipWith")

        xs = Stream.cons(
            { println("01"); 1 },
            Stream.cons(
                { println("13"); 2 },
                Stream.cons(
                    { println("15"); 3 },
                    Stream.empty,
                ),
            ),
        )
        
        ys = Stream.cons(
            { println("02"); 10 },
            Stream.cons(
                { println("14"); 20 },
                Stream.cons(
                    { println("16"); 30 },
                    Stream.empty,
                ),
            ),
        )

        println("GO")
        var ts = xs.zipWith(ys)(_ + _)
        println("END")

        println(ts.toList)

        println("***** zipAll")

        xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("13"); 6 },
                Stream.cons(
                    { println("15"); 7 },
                    Stream.empty,
                ),
            ),
        )
        
        ys = Stream.cons(
            { println("02"); 5 },
            Stream.cons(
                { println("14"); 6 },
                Stream.empty,
            ),
        )

        println("GO")
        var zs = xs.zipAll(ys)

        println("GO")
        println(zs.toList)

        xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("13"); 6 },
                Stream.empty,
            ),
        )
        
        ys = Stream.cons(
            { println("02"); 5 },
            Stream.cons(
                { println("14"); 6 },
                Stream.cons(
                    { println("15"); 6 },
                    Stream.empty,
                ),
            ),
        )

        println("GO")
        zs = xs.zipAll(ys)

        println("GO")
        println(zs.toList)

        xs = Stream.cons(
            { println("01"); 5 },
            Stream.cons(
                { println("13"); 6 },
                Stream.empty,
            ),
        )
        
        ys = Stream.cons(
            { println("02"); 5 },
            Stream.cons(
                { println("14"); 6 },
                Stream.empty,
            ),
        )

        println("GO")
        zs = xs.zipAll(ys)

        println("GO")
        println(zs.toList)
    }
}
