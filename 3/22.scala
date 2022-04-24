/**
 * 목록 두 개를 받아서 대응되는 요소들을 더한 값들로 이루어진 새 목록을 구축하는 함수를 작성하라.
 * 예를 들어 List(1, 2, 3)과 List(4, 5, 6)은 List(5, 7, 9)가 되어야 한다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def addEach(as1: List[Int], as2: List[Int]): List[Int] =
  (as1, as2) match {
    case (Cons(h, t), Nil) => Cons(h, addEach(t, Nil))
    case (Nil, Cons(h, t)) => Cons(h, addEach(t, Nil))
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(h1 + h2, addEach(t1, t2))
      //t1 전승 우승!
  }