/**
 * 연습문제 3.22의 함수를 정수나 덧셈에 국한되지 않도록 일반화하라.
 * 함수의 이름은 zipWith로 할 것.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def zipWith[A, B, C](as1: List[A], as2: List[B])(f: (A, B) => C): List[C] =
  (as1, as2) match {
    case (_, Nil) => Nil
    case (Nil, _) => Nil
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(f(h1, h2), zipWith(t1, t2)(f))
    //t1 전승 우승!
  }