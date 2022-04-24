/**
 * 연습문제 3.22의 함수를 정수나 덧셈에 국한되지 않도록 일반화하라.
 * 함수의 이름은 zipWith로 할 것.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def zipWith[A](as: List[A], bs: List[A])(f: (A,A) => A): List[A] = as match {
  case Nil => Nil
  case Cons(x,xs) => bs match {
    case Nil => Nil
    case Cons(y,ys) => Cons(f(x,y), zipWith(xs,ys)(f))
  }
}