sealed trait Stream[+A] {
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
