/**
 * sum, product와 목록의 길이를 계산하는 함수를 foldLeft를 이용해서 작성하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

@annotation.tailrec
def foldLeft[A, B](as: List[A], z: B)(F: (B, A) => B): B =
  as match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, F(z, h))(F)
  }

def sum(ints: List[Int]): Int = foldLeft(ints, 0)(_ + _)

