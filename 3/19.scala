/**
 * 목록에서 주어진 술어를 만족하지 않는 요소들을 제거하는 함수 filter를 작성하라.
 * 그리고 그 함수를 이용해서 List[Int]에서 모든 홀수를 제거하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def filter[A](as: List[A])(f: A => Boolean): List[A]