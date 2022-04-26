/**
 * 그러나 모든 것이 효율적이지는 않다.
 * 한 List의 마지막 요소를 제외한 모든 요소로 이루어진 List를 돌려주는 함수 init을 구현하라.
 * 예를 들어 List(1, 2, 3, 4)에 대해 init은 List(1, 2, 3)을 돌려주어야 한다.
 * 이 함수를 tail처럼 상수 시간으로 구현할 수 없는 이유는 무엇일까?
 => 
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def init[A](l: List[A]): List[A] = 
  l match {
    case Cons(h, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
  }