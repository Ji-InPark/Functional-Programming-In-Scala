/**
 * 목록의 구조를 유지하면서 목록의 각 요소를 수정하는 작업을 일반화한 함수 map을 작성하라. 서명은 다음과 같다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def map[A, B](as: List[A])(f: A => B): List[B] = as match {
  case Nil => Nil
  case Cons(x,xs) => Cons(f(x), map(xs)(f))
}