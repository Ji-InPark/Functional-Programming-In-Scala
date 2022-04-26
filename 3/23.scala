/**
 * 연습문제 3.22의 함수를 정수나 덧셈에 국한되지 않도록 일반화하라.
 * 함수의 이름은 zipWith로 할 것.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def zipWith[A,B](a: List[A], b: List[A])(f: (A,A) => B): List[B] = (a,b) match {
  case (Nil, _) => Nil
  case (_, Nil) => Nil
  case (Cons(a1,a1s),Cons(b1,b1s)) => Cons(f(a1,b1),zipWith(a1s,b1s)(f))
}