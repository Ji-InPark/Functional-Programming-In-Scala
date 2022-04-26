/**
 * foldRight를 이용해서 목록의 길이를 계산하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldRight(as: List[A], z: B)(f: (A, B) => B): B =
  as match {
    case Nil => z
    case  Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

def length[A](as: List[A]): Int = 
  foldRight(as, 1)((x, y) => x + 1) - foldRight(as, 1)((x, y) => x)