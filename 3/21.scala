/**
 * flatMap을 이용해서 filter를 구현하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def filter[A](as: List[A])(f: A => Boolean): List[A] =
  flatMap(as)((x)=> if f(x) => Cons(x,Nil) else Nil)