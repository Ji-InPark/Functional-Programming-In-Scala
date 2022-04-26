/**
 * 정수 목록의 각 요소에 1을 더해서 목록을 변환하는 함수를 작성하라.
 * (주의: 새 List를 돌려주는 순수 함수이어야 한다.)
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def Add1[A](a: List[A]): List[A] =
  foldRight(a, Nil:List[Int])((x,xs) => Cons(x + 1,xs))
