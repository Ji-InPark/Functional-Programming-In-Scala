/**
 * 같은 맥락에서 List의 첫 요소를 다른 값으로 대체하는 함수 setHead를 구현하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def setHead[T](as: List[T],value : T) as match {
    case Nil => Nil
    case Cons(x,xs) => Cons(value,xs)
  }
}