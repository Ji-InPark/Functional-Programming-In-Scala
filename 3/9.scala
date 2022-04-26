/**
 * foldRight를 이용해서 목록의 길이를 계산하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def length[A](as: List[A]): Int = foldRight(as,0)((_, y) => y + 1)