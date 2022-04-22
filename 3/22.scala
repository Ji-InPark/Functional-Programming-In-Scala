/**
 * 목록 두 개를 받아서 대응되는 요소들을 더한 값들로 이루어진 새 목록을 구축하는 함수를 작성하라.
 * 예를 들어 List(1, 2, 3)과 List(4, 5, 6)은 List(5, 7, 9)가 되어야 한다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]