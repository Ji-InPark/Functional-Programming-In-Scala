/**
 * 주어진 술어(predicate)와 부합하는 List의 앞 요소들(prefix)을 제거하는 함수 dropWhile을 구현하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
  case Nil => Nil
  case Cons(x,xs) => if(f(x)) dropWhile(xs,f) else xs
}