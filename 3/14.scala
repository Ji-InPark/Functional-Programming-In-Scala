/**
 * append를 foldLeft나 foldRight를 이용해서 구현하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // 스칼라가 형식 추론을 할 수 있게 개별적 인수그룹에 넣어줌
  as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

def append[A](a1: List[A], a2: List[A]): List[A] =
  foldRight(a1, a2)((x, y) => Cons(x, y))
