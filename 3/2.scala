/**
 * List의 첫요소를 제거하는 함수 tail을 구현하라.
 * 이 함수가 상수 시간으로 실행됨을 주의할 것.
 * Nil인 list도 지원하도록 독자의 구현을 수정하는 여러가지 방법들도 고려해보라.
 * 이에 대해서는 다음 장에서 좀 더 살펴볼 것이다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def trail[A](as: List[A]) : List[A] = as match {
    case Nil => Nil
    case Cons(x,xs) => xs
  }
}